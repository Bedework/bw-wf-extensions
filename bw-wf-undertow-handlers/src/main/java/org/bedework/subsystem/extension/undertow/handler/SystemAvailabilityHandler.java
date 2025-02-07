/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.subsystem.extension.undertow.handler;

import org.bedework.util.logging.BwLogger;
import org.bedework.util.logging.Logged;

import io.undertow.UndertowLogger;
import io.undertow.server.ExchangeCompletionListener;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.MimeMappings;
import org.xnio.IoUtils;
import org.xnio.channels.Channels;
import org.xnio.channels.StreamSinkChannel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpServletResponse;

/**
 * User: mike Date: 10/29/23 Time: 23:51
 */
public class SystemAvailabilityHandler
        implements HttpHandler, Logged {
  private HttpHandler next;

  private boolean serverOnline = true;
  private volatile Path unavailableFile;

  private Date readOnlyFrom;
  private Date readOnlyTo;
  private boolean serverReadOnly = false;
  private volatile Path readOnlyFile;
  private String readWriteContexts;
  private List<String> readWriteContextsList;

  public SystemAvailabilityHandler(
          final Boolean serverOnline,
          final Boolean serverReadOnly,
          final String readOnlyFrom,
          final String readOnlyTo,
          final Path readOnlyFile) {
    this.serverOnline = serverOnline;
    this.serverReadOnly = serverReadOnly;
    setReadOnlyFrom(readOnlyFrom);
    setReadOnlyTo(readOnlyTo);
    setReadOnlyFile(readOnlyFile);
  }

  public HttpHandler setNext(final HttpHandler val) {
    next = val;
    return this;
  }

  public boolean getServerOnline() {
    return serverOnline;
  }

  public void setServerOnline(final boolean val) {
    serverOnline = val;
    if (debug()) {
      debug("Set serverOnline to " + val);
    }
  }

  public boolean getServerReadOnly() {
    return serverReadOnly;
  }

  public void setServerReadOnly(final boolean val) {
    serverReadOnly = val;
    if (debug()) {
      debug("Set serverReadOnly to " + val);
    }
  }

  public String getReadOnlyFrom() {
    if (readOnlyFrom == null) {
      return null;
    }
    return readOnlyFrom.toString();
  }

  public void setReadOnlyFrom(final String val) {
    if (val == null) {
      readOnlyFrom = null;
      return;
    }
    readOnlyFrom = parseDate(val);
  }

  public String getReadOnlyTo() {
    if (readOnlyTo == null) {
      return null;
    }
    return readOnlyTo.toString();
  }

  public void setReadOnlyTo(final String val) {
    if (val == null) {
      readOnlyTo = null;
      return;
    }
    readOnlyTo = parseDate(val);
  }

  public Path getReadOnlyFile() {
    return readOnlyFile;
  }

  public void setReadOnlyFile(final Path val) {
    if (val == null) {
      readOnlyFile = null;
      return;
    }
    readOnlyFile = checkFile(val);
  }

  @Override
  public void handleRequest(final HttpServerExchange exchange)
          throws Exception {
    if (!checkReadOnlyTime()) {
      next.handleRequest(exchange);
    } else {
      exchange.setStatusCode(
              HttpServletResponse.SC_SERVICE_UNAVAILABLE);
      exchange.endExchange();
    }
  }

  public boolean checkReadOnlyTime() {
    if ((readOnlyFrom == null) && (readOnlyTo == null)) {
      return false;
    }

    final Date now = new Date();

    if ((readOnlyFrom != null) && readOnlyFrom.after(now)) {
      return false;
    }

    if ((readOnlyTo != null) && readOnlyTo.before(now)) {
      return false;
    }

    return true;
  }

  private Date parseDate(final String val) {
    final String inputFormat = "HH:mm";
    final SimpleDateFormat inputParser =
            new SimpleDateFormat(inputFormat, Locale.US);

    try {
      return inputParser.parse(val);
    } catch (final java.text.ParseException e) {
      error("Bad date format " + val);
      throw new RuntimeException("Bad date format " + val);
    }
  }

  private Path checkFile(final Path val) {
    final var file = val.toFile();

    if (!file.isFile()) {
      error(String.format("%s is not a file",
                          val.toString()));
      throw new RuntimeException(
              String.format("%s is not a file",
                            val.toString()));
    }

    if (!file.exists()) {
      error(String.format("%s does not exist",
                          val.toString()));
      throw new RuntimeException(
              String.format("%s does not exist",
                            val.toString()));
    }

    return val;
  }

  private void serveFile(final HttpServerExchange exchange,
                         final Path file) {
    final String fileName = file.toString();
    final int index = fileName.lastIndexOf(".");
    if (index > 0) {
      final String contentType =
              MimeMappings.DEFAULT.getMimeType(
                      fileName.substring(index + 1));
      if (contentType != null) {
        exchange.getResponseHeaders()
                .put(Headers.CONTENT_TYPE, contentType);
      }
    }
    exchange.dispatch(new Runnable() {
      @Override
      public void run() {
        final FileChannel fileChannel;
        try {
          try {
            fileChannel = FileChannel.open(file,
                                           StandardOpenOption.READ);
          } catch (FileNotFoundException e) {
            UndertowLogger.REQUEST_IO_LOGGER.ioException(e);
            exchange.endExchange();
            return;
          }
        } catch (IOException e) {
          UndertowLogger.REQUEST_IO_LOGGER.ioException(e);
          exchange.endExchange();
          return;
        }
        final long size;
        try {
          size = Files.size(file);
        } catch (final IOException e) {
          throw new RuntimeException(e);
        }
        exchange.getResponseHeaders()
                .put(Headers.CONTENT_LENGTH, size);
        final StreamSinkChannel response = exchange.getResponseChannel();
        exchange.addExchangeCompleteListener(
                new ExchangeCompletionListener() {
                  @Override
                  public void exchangeEvent(
                          HttpServerExchange exchange,
                          NextListener nextListener) {
                    IoUtils.safeClose(fileChannel);
                    nextListener.proceed();
                  }
                });

        try {
          trace(String.format(
                  "Serving file %s (blocking)", fileChannel));
          Channels.transferBlocking(response, fileChannel, 0,
                                    Files.size(file));
          trace(String.format(
                  "Finished serving %s, shutting down (blocking)",
                     fileChannel));
          response.shutdownWrites();
          trace(String.format(
                  "Finished serving %s, flushing (blocking)",
                     fileChannel));
          Channels.flushBlocking(response);
          trace(String.format(
                  "Finished serving %s (complete)", fileChannel));
          exchange.endExchange();
        } catch (final IOException io) {
          trace(String.format(
                  "Failed to serve %s: %s", fileChannel, io));
          exchange.endExchange();
          IoUtils.safeClose(response);
        } finally {
          IoUtils.safeClose(fileChannel);
        }
      }
    });
  }

  /* ==============================================================
   *                   Logged methods
   * ============================================================== */

  private final BwLogger logger = new BwLogger();

  @Override
  public BwLogger getLogger() {
    if ((logger.getLoggedClass() == null) &&
            (logger.getLoggedName() == null)) {
      logger.setLoggedClass(getClass());
    }

    return logger;
  }
}
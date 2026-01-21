package com.evoucher.evoucherbe.config;

import com.evoucher.evoucherbe.common.models.LoggedInClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggedInClientContext {

  private static final ThreadLocal<LoggedInClient> CONTEXT = new ThreadLocal<>();

  private static final Logger LOGGER = LoggerFactory.getLogger(LoggedInClientContext.class);

  public static void setLoggedInClient(LoggedInClient client) {
    LOGGER.info("Set Logged in Client: {}", client);
    CONTEXT.set(client);
  }

  public static LoggedInClient getLoggedInClient() {
    return CONTEXT.get();
  }

  public static String loggedInClientID() throws RuntimeException {
    if (CONTEXT.get() == null) {
      return null;
    }
    LOGGER.info("get LoggedIn Client: {}", CONTEXT.get().getId());
    return CONTEXT.get().getId();
  }

  public static boolean isAnonymous() {
    return CONTEXT.get() == null;
  }

  public static void clear() {
    CONTEXT.remove();
  }
}

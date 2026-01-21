package com.evoucher.externalserviceapi.common.config;


import com.evoucher.externalserviceapi.common.exception.ClientNotLoggedInException;
import com.evoucher.externalserviceapi.common.model.LoggedInClient;
import com.evoucher.externalserviceapi.common.utils.MessageUtils;

public class LoggedInClientContext {

  private static final ThreadLocal<LoggedInClient> CONTEXT = new ThreadLocal<>();

  public static void setLoggedInClient(LoggedInClient client) {
    CONTEXT.set(client);
  }

  public static String loggedInClientToken() throws ClientNotLoggedInException {
    if (CONTEXT.get() == null) {
      throw new ClientNotLoggedInException(MessageUtils.getMessage("external.login.again"));
    }
    return CONTEXT.get().getToken();
  }

  public static void clear() {
    CONTEXT.remove();
  }
}

package net.marci.module.http.controller;

import lombok.extern.slf4j.Slf4j;
import net.marci.module.http.dto.ServerResponse;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.concurrent.Callable;

@Slf4j
@CrossOrigin(origins = { "*" })
abstract public class BaseController {

  public <T> ServerResponse execute(String component, String service, Callable<T> executor) {
    ServerResponse response = new ServerResponse(component, service);
    try {
      T result = executor.call();
      response.setFinishTimestamp(System.currentTimeMillis());
      log.info("Execute {}:{} successfully", component, service);
      response.setStatus(ServerResponse.Status.OK);
      response.setMessage(MessageFormat.format("Execute {0}:{1} successfully", component, service));
      response.setBody(result);
      return response;
    } catch (Exception ex) {
      log.error("Error executing {}:{}", component, service, ex);
      response.setStatus(ServerResponse.Status.ERROR);
      response.setMessage(MessageFormat.format("Error executing {0}:{1}", component, service));
      if (Objects.nonNull(ex.getCause())) {
        response.setBody(ex.getCause().getMessage());
      } else {
        response.setBody(ex.getMessage());
      }
      response.setFinishTimestamp(System.currentTimeMillis());
      return response;
    }
  }
}
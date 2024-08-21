package net.marci.module.http.controller;

import net.marci.module.http.RPCService;
import net.marci.module.http.dto.RPCRequest;
import net.marci.module.http.dto.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Bình Nguyễn
 * @Email jackjack2000.kahp@gmail.com
 * @RPC Stands for Remote Procedure Call. It's a protocol
 * @Usage Use for private api cases
 */
@RestController
@RequestMapping("/rpc")
public class RPCController extends BaseController {

  @Autowired
  private RPCService service;

  @PostMapping("/call")
  public @ResponseBody ServerResponse call(@RequestBody RPCRequest request) {
    Callable<Object> executor = () -> {
      List<Object> argHolder = new ArrayList<>();
      return service.processRequest(request, argHolder);
    };
    final String component = request.getComponent();
    final String service = request.getService();
    return execute(component, service, executor);
  }
}
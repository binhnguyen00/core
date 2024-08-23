package net.binhnguyen.module.http.controller;

import net.binhnguyen.module.http.DummyService;
import net.binhnguyen.module.http.dto.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;

@RestController
@RequestMapping("/dummy")
public class DummyController extends BaseController {

  @Autowired
  DummyService dummyService;

  @PostMapping("/hello")
  @ResponseStatus(HttpStatus.OK)
  public @ResponseBody ServerResponse hello() {
    Callable<String> executor = () -> dummyService.helloWorld();
    return this.execute("DummyService", "helloWorld", executor);
  }
}
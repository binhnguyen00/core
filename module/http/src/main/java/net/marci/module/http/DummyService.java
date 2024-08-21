package net.marci.module.http;

import org.springframework.stereotype.Service;

@Service("DummyService")
public class DummyService {

  public String helloWorld() {
    return "Welcome to Marci Server";
  }
}
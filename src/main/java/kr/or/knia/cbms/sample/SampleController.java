package kr.or.knia.cbms.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

  @GetMapping("/test/{name}")
  public String test(@PathVariable String name) {
    return "annyoung ".concat(name);
  }

  @GetMapping("/error")
  public void testError() {
    throw new RuntimeException("test");
  }
}

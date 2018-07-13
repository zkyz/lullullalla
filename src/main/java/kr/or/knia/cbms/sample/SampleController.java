package kr.or.knia.cbms.sample;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

  @GetMapping("/greeting/{name}")
  public String test(@PathVariable String name) {
    return "Hello ".concat(name);
  }

  @GetMapping("/error")
  public void error() {
    throw new RuntimeException("self occure exception");
  }

  /**
   * json response sampling.
   * @return
   */
  @PostMapping("/json")
  public Map<String, Object> json() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", "green");
    map.put("age", 14);
    return map;
  }
}

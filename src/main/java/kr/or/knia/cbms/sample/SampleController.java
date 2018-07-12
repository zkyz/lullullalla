package kr.or.knia.cbms.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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

  @PostMapping("/json")
  public Map<String, Object> json() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", "green");
    map.put("age", 14);
    return map;
  }
}

package kr.or.knia.lullullalla;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@SpringBootApplication
public class LullullallaApplication {
	
	@GetMapping("/test/{name}")
	public String test(@PathVariable String name) {
		return "hello+ ".concat(name);
	}

	public static void main(String... args) {
		SpringApplication application = new SpringApplication(LullullallaApplication.class);
		application.setBannerMode(Mode.OFF);
		application.run(args);
	}
}

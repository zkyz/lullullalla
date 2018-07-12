package kr.or.knia.cbms;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CbmsApplication {

  /**
   * sample start.
   * 
   * @param args some spring default settings
   */
  public static void main(String... args) {
    SpringApplication application = new SpringApplication(CbmsApplication.class);
    application.setBannerMode(Mode.OFF);
    application.run(args);
  }
}

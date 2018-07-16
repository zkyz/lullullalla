package kr.or.knia.cbms.config.mybatis.typehandler;

import kr.or.knia.cbms.config.mybatis.PageableForOracleInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisInterceptorsConfiguration {
  @Bean
  public PageableForOracleInterceptor pageableForOracleInterceptor() {
    return new PageableForOracleInterceptor();
  }
}

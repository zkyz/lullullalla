package kr.or.knia.cbms.sample.config;

import kr.or.knia.cbms.config.mybatis.typehandler.MybatisInterceptorsConfiguration;
import kr.or.knia.cbms.core.bbs.Article;
import kr.or.knia.cbms.core.bbs.BbsMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@MybatisTest
@ActiveProfiles("test")
public class MybatisTests {

  @Autowired
  private BbsMapper mapper;

  @Test
  public void _01_createdMapper() {
    assertThat(mapper).isNotNull();
  }

  @Test
  public void _02_mapperExecution() {
    final List<Article> list = mapper.getList("", "");
    assertThat(list).isNotNull();
  }
}

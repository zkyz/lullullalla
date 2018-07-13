package kr.or.knia.cbms.core.bbs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BbsService {
  Page<Article> list(Article search, Pageable page);

  Article item(Integer id);

  Article save(Article article);

  void delete(Integer id);
}

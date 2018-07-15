package kr.or.knia.cbms.core.bbs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BbsServiceImpl implements BbsService {
  
  @Autowired
  private BbsRepository repository;

  @Override
  public Page<Article> list(Article search, Pageable pageable) {
    return repository.findByCategory(search.getCategory(), pageable);
  }

  @Override
  public Article item(Integer id) {
    return null;
  }

  @Override
  public Article save(Article article) {
    return null;
  }

  @Override
  public void delete(Integer id) {
  }

}

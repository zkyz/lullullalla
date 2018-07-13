package kr.or.knia.cbms.core.bbs;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BbsController {
  
  private BbsService service;
  
  public BbsController(final BbsService service) {
    this.service = service;
  }

  @GetMapping({"/{category}/bbs/list"})
  public Page<Article> list(Article search, Pageable page) {
    return service.list(search, page);
  }
  
  @GetMapping("/{category}/bbs/{id}")
  public Article item(@PathVariable Integer id) {
    return service.item(id);
  }

  @PostMapping("/{category}/bbs/{id}")
  public Article save(@Valid Article article) {
    return service.save(article);
  }
  
  @DeleteMapping("/{category}/bbs/{id}")
  public void delete(@PathVariable Integer id) {
    service.delete(id);
  }
}

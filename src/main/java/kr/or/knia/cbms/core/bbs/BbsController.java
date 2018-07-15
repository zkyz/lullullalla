package kr.or.knia.cbms.core.bbs;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/{category}/bbs")
public class BbsController {

  private final BbsService service;

  @Autowired
  public BbsController(BbsService service) {
    this.service = service;
  }

  @GetMapping("/list")
  public Page<Article> list(Article search,
                            @SortDefault.SortDefaults({
                              @SortDefault(sort = "id", direction = Sort.Direction.DESC),
                              @SortDefault(sort = "created", direction = Sort.Direction.DESC)
                            }) Pageable page) {
    return service.list(search, page);
  }
  
  @GetMapping("/{id}")
  public Article item(@PathVariable Integer id) {
    return service.item(id);
  }

  @PostMapping("/{id}")
  public Article save(@Valid Article article) {
    return service.save(article);
  }
  
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Integer id) {
    service.delete(id);
  }
}

package kr.or.knia.cbms.core.bbs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BbsRepository extends JpaRepository<Article, Integer> {
  Page<Article> findByCategory(String category, Pageable pageable);
}

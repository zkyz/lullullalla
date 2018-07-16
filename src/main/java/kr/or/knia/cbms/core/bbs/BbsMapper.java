package kr.or.knia.cbms.core.bbs;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface BbsMapper {
  List<Article> getList(String type, String keyword);
}

package kr.or.knia.cbms.core.bbs;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Entity
public class Article {
  private Integer id;

  @NotEmpty
  private String category;

  @NotEmpty
  private String subject;

  private String content;

  private LocalDateTime created;

  private LocalDateTime updated;
}

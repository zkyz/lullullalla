package kr.or.knia.cbms.core.bbs;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CBMS_BBS")
@SequenceGenerator(name = Article.SQ_BBS_ID, sequenceName = Article.SQ_BBS_ID, allocationSize = 1)
public class Article {
  public static final String SQ_BBS_ID = "SQ_CBMS_BBS_ID";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SQ_BBS_ID)
  private Integer id;

  @NotEmpty
  private String category;

  @NotEmpty
  private String subject;

  private LocalDateTime created;

  @LastModifiedDate
  private LocalDateTime updated;
}

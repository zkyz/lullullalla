package kr.or.knia.cbms.core.bbs;

import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "BBS")
@Data
@SequenceGenerator(name = Article.SQ_BBS_ID, sequenceName = Article.SQ_BBS_ID)
public class Article {
  public static final String SQ_BBS_ID = "SQ_BBS_ID";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Article.SQ_BBS_ID)
  private Integer id;

  @NotEmpty
  private String category;

  @NotEmpty
  private String subject;

  private LocalDateTime created;

  @LastModifiedDate
  private LocalDateTime updated;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  private String content;

}

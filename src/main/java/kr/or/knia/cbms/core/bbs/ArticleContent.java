package kr.or.knia.cbms.core.bbs;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CBMS_BBS_CONTENT")
public class ArticleContent {

  @Id
  private Integer bbsId;

  @Lob
  private String content;
}

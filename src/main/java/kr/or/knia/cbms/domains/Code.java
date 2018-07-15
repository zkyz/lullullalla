package kr.or.knia.cbms.domains;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "CBMS_CODE")
public class Code {
  @Data
  @Embeddable
  public static class CodeId implements Serializable {
    @Column(name = "GRP")
    private String group;
    private String code;
  }

  @EmbeddedId
  private CodeId id;

  private String value;

  @Column(name = "ORD")
  private Integer order;
}

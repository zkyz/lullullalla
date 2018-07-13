package kr.or.knia.cbms.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
@SequenceGenerator(name = User.SEQ_NAME, sequenceName = "SQ_USER_SEQ")
public class User {
  public static final String SEQ_NAME = "SQ_USER_SEQ";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = User.SEQ_NAME)
  private Integer seq;

  @Column(unique = true)
  private String id;
  
  @JsonIgnore
  private String password;

  @NotEmpty
  private String name;

  private String email;
  
  private String mobile;
  
  private String tel;
}

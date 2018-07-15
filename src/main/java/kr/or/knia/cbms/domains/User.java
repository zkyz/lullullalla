package kr.or.knia.cbms.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "CBMS_USER")
public class User {
  @Id
  private String id;

  @JsonIgnore
  private String password;

  @NotEmpty
  private String name;

  private String email;
  
  private String mobile;
  
  private String tel;

  // private Set<UserRoles.Role> roles;

  private LocalDateTime approved;
}

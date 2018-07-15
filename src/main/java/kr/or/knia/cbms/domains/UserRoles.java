package kr.or.knia.cbms.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;

/*
@Data
@Entity
@Table(name = "CBMS_USER_ROLES")
@IdClass(UserRoles.Keys.class)
*/
public class UserRoles {
/*
  @Id
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "USER_ID")
  private User user;

  @Id
  @Enumerated(EnumType.STRING)
  private Role role;
*/

  public enum Role implements GrantedAuthority {
    ADMIN,
    JUDGE,
    OFFICER,
    WORKER,
    JOINER;

    @Override
    public String getAuthority() {
      return this.toString();
    }
  }

  @Data
  public class Keys implements Serializable {
    private final String user;
    private final String role;
  }
}

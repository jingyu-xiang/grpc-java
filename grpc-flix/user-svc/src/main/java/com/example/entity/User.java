package com.example.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Entity @Table(name ="ms_user")
@Getter @Setter @ToString @RequiredArgsConstructor
public class User {
  @Id
  @Column(name = "login_id")
  private String loginId;
  private String name;
  private String genre;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    User user = (User) o;
    return loginId != null && Objects.equals(loginId, user.loginId);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}

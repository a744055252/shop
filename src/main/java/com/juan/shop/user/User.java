package com.juan.shop.user;

import com.juan.shop.common.AbstractEntity;
import com.juan.shop.role.Role;
import com.juan.shop.user.model.Gender;
import com.juan.shop.user.model.Status;
import com.juan.shop.user.model.Type;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * @author liguanhuan
 */
@Data
@Entity
public class User extends AbstractEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String password;

    private String name;

    private Gender gender;

    private Status status;

    private Type type;

    @Transient
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == Status.qiyong;
    }
}

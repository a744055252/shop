package com.juan.shop.role;

import com.juan.shop.common.AbstractEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author liguanhuan
 */
@Data
@Entity
public class Role extends AbstractEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    /** 权限识别 */
    private String auth;

    @Override
    public String getAuthority() {
        return auth;
    }
}

package com.juan.shop.user;

import com.juan.shop.common.AbstractEntity;
import com.juan.shop.role.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author liguanhuan
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class UserRole extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** {@link User#getId()} */
    private long userId;

    /** {@link Role#getId()} */
    private long roleId;

}

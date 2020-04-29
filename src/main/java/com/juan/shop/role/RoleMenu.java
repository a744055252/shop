package com.juan.shop.role;

import com.juan.shop.common.AbstractEntity;
import com.juan.shop.menu.Menu;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author liguanhuan
 */
@Data
@Entity
public class RoleMenu extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** {@link Role#getId()} */
    private long roleId;

    /** {@link Menu#getId()} */
    private long menuId;

}

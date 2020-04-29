package com.juan.shop.menu;

import com.juan.shop.auth.Auth;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author guanhuan_li
 */
@Data
@Entity
public class MenuAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** menuId {@link Menu#getId()} */
    private long menuId;

    /** authId {@link Auth#getId()} */
    private long authId;
}

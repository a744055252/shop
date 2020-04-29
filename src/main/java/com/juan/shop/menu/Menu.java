package com.juan.shop.menu;

import com.juan.shop.common.AbstractEntity;
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
public class Menu extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** 菜单名 每一级都是一个菜单*/
    private String name;
}

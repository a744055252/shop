package com.juan.shop.menu;

import java.util.Optional;

/**
 * @author guanhuan_li
 */
public interface IMenuService {

    Optional<Menu> get(long id);

}

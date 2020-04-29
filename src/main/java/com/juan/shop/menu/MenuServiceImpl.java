package com.juan.shop.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author guanhuan_li
 */
@Slf4j
@Service
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private MenuRepository menuRepository;


    @Override
    public Optional<Menu> get(long id) {
        return menuRepository.findById(id);
    }
}

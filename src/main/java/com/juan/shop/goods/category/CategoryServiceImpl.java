package com.juan.shop.goods.category;

import exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author guanhuan_li
 */
@Service
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public String getName(Long categoryId) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        Category category = categoryOpt.orElseThrow(() ->
                LogicException.valueOfUnknow("分类id：" + categoryId + " 不存在！"));
        return category.getName();
    }
}

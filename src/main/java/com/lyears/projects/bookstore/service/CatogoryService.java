package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Category;
import com.lyears.projects.bookstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/11/6 19:24
 */
@Service
public class CatogoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAllCategory()
    {
        return categoryRepository.findAll();
    }

    /**
     *  添加一个分类
     * @param category
     */
    public void addCategory(Category category)
    {
        categoryRepository.save(category);
    }

    public void editCategory(Category category,int id)
    {
        Category one = categoryRepository.findOne(id);
        one.setName(category.getName());
        categoryRepository.save(one);
    }
}

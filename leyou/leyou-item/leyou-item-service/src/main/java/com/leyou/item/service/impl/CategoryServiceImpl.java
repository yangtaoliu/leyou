package com.leyou.item.service.impl;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 通过父节点pid查询子节点列表
     * @param pid
     * @return
     */
    @Override
    public List<Category> queryCategoriesByPid(Long pid) {

        Category record = new Category();

        record.setParentId(pid);

        return this.categoryMapper.select(record);
    }
}

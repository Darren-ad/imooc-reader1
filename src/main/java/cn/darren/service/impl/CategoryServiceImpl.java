package cn.darren.service.impl;

import cn.darren.entity.Category;
import cn.darren.mapper.CategoryMapper;
import cn.darren.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("categoryService")
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> selectAll() {
        List<Category> categories = categoryMapper.selectList(new QueryWrapper<Category>());
        return categories;
    }
}

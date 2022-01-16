package cn.darren.mapper;

import cn.darren.entity.Test;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface TestMapper extends BaseMapper<Test> {
    public void insertSample();
}

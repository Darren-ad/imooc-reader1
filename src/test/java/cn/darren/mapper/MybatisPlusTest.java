package cn.darren.mapper;

import cn.darren.entity.Test;
import cn.darren.mapper.TestMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MybatisPlusTest {

    @Resource
    private TestMapper testMapper;

    @org.junit.Test
    public void testInsert(){
        Test test = new Test();
        test.setContent("MybatisPlus测试");
        testMapper.insert(test);
    }


    @org.junit.Test
    public void testUpdate(){
        Test test = testMapper.selectById(42);
        test.setContent("Mybatis-Plus测试1");
        testMapper.updateById(test);
    }

    @org.junit.Test
    public void testDelete(){
        testMapper.deleteById(42);
    }

    @org.junit.Test
    public void testSelect(){
        QueryWrapper<Test> queryWrapper = new QueryWrapper<Test>();
        queryWrapper.eq("id", 41);
        queryWrapper.gt("id", 35);
        List<Test> list = testMapper.selectList(queryWrapper);
        System.out.println(list);
    }
}

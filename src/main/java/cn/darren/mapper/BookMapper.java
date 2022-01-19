package cn.darren.mapper;

import cn.darren.entity.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface BookMapper extends BaseMapper<Book> {

    /**
     * 更新图书评分/评价数量
     */
    public void updateEvaluation();
}

package cn.darren.service.impl;

import cn.darren.entity.Book;
import cn.darren.entity.Evaluation;
import cn.darren.entity.Member;
import cn.darren.mapper.BookMapper;
import cn.darren.mapper.EvaluationMapper;
import cn.darren.mapper.MemberMapper;
import cn.darren.service.EvaluationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("evaluationService")
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class EvaluationServiceImpl implements EvaluationService {
    
    @Resource
    private EvaluationMapper evaluationMapper;
    @Resource
    private BookMapper bookMapper;
    @Resource
    private MemberMapper memberMapper;

    /**
     * 按图书编号查询有效短评
     * @param bookId 图书编号
     * @return 评论列表
     */
    @Override
    public List<Evaluation> selectByBookId(Long bookId) {
        Book book = bookMapper.selectById(bookId);
        QueryWrapper<Evaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id",bookId);
        queryWrapper.eq("state", "enable");
        queryWrapper.orderByDesc("create_time");
        List<Evaluation> evaluationList = evaluationMapper.selectList(queryWrapper);
        for(Evaluation eva:evaluationList){
            Member member = memberMapper.selectById(eva.getMemberId());
            eva.setMember(member);
            eva.setBook(book);
        }
        return evaluationList;
    }
}

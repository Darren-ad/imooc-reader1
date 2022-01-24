package cn.darren.service.impl;

import cn.darren.entity.Book;
import cn.darren.entity.Evaluation;
import cn.darren.entity.MemberReadState;
import cn.darren.mapper.BookMapper;
import cn.darren.mapper.EvaluationMapper;
import cn.darren.mapper.MemberReadStateMapper;
import cn.darren.service.BookService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("bookService")
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class BookServiceImpl implements BookService {

    @Resource
    private BookMapper bookMapper;
    @Resource
    private MemberReadStateMapper memberReadStateMapper;
    @Resource
    private EvaluationMapper evaluationMapper;
    
    /**
     * 分页查询图书
     * @param categoryId 分类编号
     * @param order      排序方式
     * @param page       页号
     * @param rows       每页记录数
     * @return 分页对象
     */
    @Override
    public IPage<Book> paging(Long categoryId, String order, Integer page, Integer rows) {
        Page<Book> p = new Page<Book>(page, rows);
        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
        if(categoryId != null && categoryId != -1){
            bookQueryWrapper.eq("category_id", categoryId);
        }
        if(order != null){
            if(order.equals("quantity")){
                bookQueryWrapper.orderByDesc("evaluation_quantity");
            }else if(order.equals("score")){
                bookQueryWrapper.orderByDesc("evaluation_score");
            }
        }
        IPage<Book> pageObject = bookMapper.selectPage(p, bookQueryWrapper);
        return pageObject;
    }

    /**
     * 通过图书编号查询图书对象
     *
     * @param id 图书编号
     * @return 图书对象
     */
    @Override
    public Book selectById(Long id) {
        Book book = bookMapper.selectById(id);
        return book;
    }

    /**
     * 更新图书评分/评价数量
     */
    @Override
    @Transactional //开启声明式事务
    public void updateEvaluation() {
        bookMapper.updateEvaluation();
    }

    /**
     * 创建新的图书
     *
     * @param book
     * @return
     */
    @Override
    @Transactional
    public Book createBook(Book book) {
        bookMapper.insert(book);//在插入成功后，由于主键是自增的，在执行完insert以后，由Mybatis-Plus会将编号自动的回填到book中
        return book;
    }

    /**
     * 更新图书
     *
     * @param book 新图书数据
     * @return 更新后的数据
     */
    @Override
    @Transactional
    public Book updateBook(Book book) {
         bookMapper.updateById(book);
        return book;
    }

    /**
     * 删除图书及相关数据
     *
     * @param bookId 图书编号
     */
    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        bookMapper.deleteById(bookId);
        QueryWrapper<MemberReadState> memberReadStateQueryWrapper = new QueryWrapper<>();
        memberReadStateQueryWrapper.eq("book_id",bookId);
        memberReadStateMapper.delete(memberReadStateQueryWrapper);
        QueryWrapper<Evaluation> evaluationQueryWrapper = new QueryWrapper<>();
        evaluationQueryWrapper.eq("book_id", bookId);
        evaluationMapper.delete(evaluationQueryWrapper);
    }
}

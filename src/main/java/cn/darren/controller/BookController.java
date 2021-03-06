package cn.darren.controller;

import cn.darren.entity.*;
import cn.darren.mapper.MemberMapper;
import cn.darren.service.BookService;
import cn.darren.service.CategoryService;
import cn.darren.service.EvaluationService;
import cn.darren.service.MemberService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class BookController {

    @Resource
    private CategoryService categoryService;
    @Resource
    private BookService bookService;
    @Resource
    private EvaluationService evaluationService;
    @Resource
    private MemberService memberService;

    @GetMapping("/")
    public ModelAndView showIndex(){
        ModelAndView modelAndView = new ModelAndView("/index");
        List<Category> categories = categoryService.selectAll();
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    /**
     * 分页查询图书列表
     * @param categoryId 分类编号
     * @param order 排序方式
     * @param p 页号
     * @return 分页对象
     */
    @GetMapping({"/books"})
    @ResponseBody
    public IPage<Book> selectBook(Long categoryId, String order, Integer p){
        if(p == null){
            p = 1;
        }
        IPage<Book> pageObject = bookService.paging(categoryId, order, p, 10);
        return pageObject;
    }

    /**
     * 通过图书编号查询图书对象
     * @param id
     * @return
     */
    @GetMapping("/book/{id}")
    public ModelAndView showDetail(@PathVariable("id") Long id, HttpSession session){
        Book book = bookService.selectById(id);
        List<Evaluation> evaluationList = evaluationService.selectByBookId(id);
        ModelAndView modelAndView = new ModelAndView("/detail");
        Member member = (Member) session.getAttribute("loginMember");
        if(member != null){
            //获取会员阅读状态
            MemberReadState memberReadState = memberService.selectMemberReadState(member.getMemberId(), id);
            modelAndView.addObject("memberReadState", memberReadState);
        }
        modelAndView.addObject("book", book);
        modelAndView.addObject("evaluationList", evaluationList);
        return modelAndView;
    }
}

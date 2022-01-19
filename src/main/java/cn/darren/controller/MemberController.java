package cn.darren.controller;

import cn.darren.entity.Evaluation;
import cn.darren.entity.Member;
import cn.darren.exception.BussinessException;
import cn.darren.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MemberController {
    
    @Resource
    private MemberService memberService;
    
    @GetMapping("register.html")
    public ModelAndView showRegister(){
        return new ModelAndView("/register");
    }
    
    @PostMapping("/registe")
    @ResponseBody
    public Map registe(String vc, String username, String password, String nickname, HttpServletRequest request){
        //正确的验证码
        String verifyCode = (String) request.getSession().getAttribute("kaptchaVerifyCode");
        //验证码对比
        Map result = new HashMap();
        if(vc == null || verifyCode == null || !vc.equalsIgnoreCase(verifyCode)){
            result.put("code", "vc01");
            result.put("msg", "验证码错误");
        }else{
            try {
                memberService.createMember(username, password,nickname);
                result.put("code", "0");
                result.put("msg", "success");
            } catch (BussinessException ex) {
                result.put("code", ex.getCode());
                result.put("msg", ex.getMsg());
            }
        }
        return result;
    }
    
    @GetMapping("/login.html")
    public ModelAndView showLogin(){
        return new ModelAndView("/login");
    }
    
    @PostMapping("/check_login")
    @ResponseBody
    public Map checkLogin(String username, String password, String vc, HttpSession session){
        //正确验证码
        String verifyCode = (String)session.getAttribute("kaptchaVerifyCode");
        //验证码比对
        Map result = new HashMap();
        if(vc == null || verifyCode == null || !vc.equalsIgnoreCase(verifyCode)){
            result.put("code", "VC01");
            result.put("msg", "验证码错误");
        }else{
            try{
                Member member = memberService.checkLogin(username, password);
                session.setAttribute("loginMember", member);
                result.put("code", "0");
                result.put("msg", "success");
            }catch (BussinessException ex){
                result.put("code", ex.getCode());
                result.put("msg", ex.getMsg());
            }
        }
        return result;
    }

    /**
     * 更新 想看/看过 阅读状态
     * @param memberId  会员编号
     * @param bookId    图书编号
     * @param readState 阅读状态
     * @return  处理结果
     */
    @PostMapping("/update_read_state")
    @ResponseBody
    public Map updateReadState(Long memberId, Long bookId, Integer readState){
        Map result = new HashMap();
        try{
            memberService.updateMemberReadState(memberId, bookId, readState);
            result.put("code", "0");
            result.put("msg", "success");
        }catch (BussinessException ex){
            result.put("code", ex.getCode());
            result.put("msg", ex.getMsg());
        }
        return result;
    }

    /**
     * 写短评
     * @param memberId 会员编号
     * @param bookId 图书编号
     * @param score 评分
     * @param content 短评内容
     * @return 处理结果
     */
    @PostMapping("/evaluate")
    @ResponseBody
    public Map evaluate(Long memberId, Long bookId, Integer score, String content){
        Map result = new HashMap();
        try{
            memberService.evaluate(memberId, bookId, score, content);
            result.put("code", "0");
            result.put("msg", "success");
        }catch (BussinessException ex){
            result.put("code", ex.getCode());
            result.put("msg", ex.getMsg());
        }
        return result;
    }


    /**
     * 短评点赞
     * @param evaluationId 短评编号
     * @return 处理结果
     */
    @PostMapping("/enjoy")
    @ResponseBody
    public Map evaluate(Long evaluationId){
        Map result = new HashMap();
        try{
            Evaluation evaluation = memberService.enjoy(evaluationId);
            result.put("code", "0");
            result.put("msg", "success");
            result.put("Evaluation", evaluation);
        }catch (BussinessException ex){
            result.put("code", ex.getCode());
            result.put("msg", ex.getMsg());
        }
        return result;
    }
}

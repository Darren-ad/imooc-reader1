package cn.darren.controller;

import cn.darren.exception.BussinessException;
import cn.darren.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
}

package cn.darren.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {

    @RequestMapping("/demo")
    public ModelAndView test01(){
        return new ModelAndView("/test");
    }
}

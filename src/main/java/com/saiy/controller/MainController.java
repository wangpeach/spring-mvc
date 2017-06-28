package com.saiy.controller;

import com.saiy.models.Users;
import com.saiy.services.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by rjora on 2016/12/29 0029.
 */
@Controller
@RequestMapping("/test")
@SessionAttributes(value = "user")
public class MainController {

    @Resource
    private TestService testService;

    @RequestMapping(method = RequestMethod.GET)
    public String home(ModelMap model) {
        model.addAttribute("test", "hello world, 用户数:" + testService.count());
        System.out.println("come in");
        return "index";
    }

    @RequestMapping(path = "sayhi")
    public String sayhi(@RequestParam("id") int id, ModelMap model) {
        Users user = testService.getUser(id);
        model.addAttribute("user", user);
        model.addAttribute("test", "hello " + user.getUsersName());
        return "forward:home.jsp";
    }

    @RequestMapping(path = "session")
    @ResponseBody
    public Users outSession(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        System.out.println(user.getUsersName());
        return user;
    }
}

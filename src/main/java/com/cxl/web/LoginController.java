package com.cxl.web;

import com.cxl.domain.User;
import com.cxl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class LoginController {

    private UserService userService;

    @RequestMapping(value = "/index.html")
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "loginCheck.html")
    public ModelAndView loginCheck(HttpServletRequest httpServletRequest, LoginCommand loginCommand) {
        boolean isValidUser = userService.hasMatchCount(loginCommand.getUserName(), loginCommand.getPassword());
        if (isValidUser) {
            return new ModelAndView("login", "error", "用户名或密码错误");
        }

        User user = userService.findUserByUserName(loginCommand.getUserName());
        user.setLastIp(httpServletRequest.getLocalAddr());
        user.setLastVisit(new Date());
        userService.loginSuccess(user);
        httpServletRequest.getSession().setAttribute("user", user);
        return new ModelAndView("main");
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}

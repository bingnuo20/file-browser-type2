package com.dev10000.filebrowsertype2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 登录控制器
 */
@Controller
public class LoginController {

    /** 用户名 */
    @Value("${sys.login.username}")
    private String username;

    /** 面膜 */
    @Value("${sys.login.password}")
    private String password;

    /**
     * 映射登录界面
     * @param request 请求实例
     * @return html
     */
    @RequestMapping("login")
    public String index(HttpServletRequest request) {
        return "login";
    }

    /**
     * 用户登录
     * @param userCode 用户名
     * @param userPwd 密码
     * @param request 请求实例
     * @return html
     */
    @RequestMapping("doLogin")
    public String index(String userCode,String userPwd,HttpServletRequest request) {
        //如果登录成功
        if (userCode.equals(username) && userPwd.equals(userPwd)) {
            HttpSession session = request.getSession();
            session.setAttribute("loginFlag",1);
            return "redirect:/";
        } else {
            return "redirect:login";
        }
    }
}

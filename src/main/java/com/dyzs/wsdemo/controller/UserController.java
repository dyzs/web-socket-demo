package com.dyzs.wsdemo.controller;


import com.dyzs.wsdemo.bean.WsUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping("/login")
    public String login(WsUser user, HttpSession httpSession) {
        httpSession.setAttribute("user", user.name);
        return "login call";
    }

    @RequestMapping("/getUserName")
    public String getUserName(HttpSession httpSession) {
        return (String) httpSession.getAttribute("user");
    }

    @RequestMapping("/getUserAvatar")
    public String getUserAvatar() {
        return "";
    }
}

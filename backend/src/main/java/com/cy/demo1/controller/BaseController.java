package com.cy.demo1.controller;

//import com.cy.demo1.service.ex.*;
//import com.cy.demo1.util.JsonResult;
//import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;

/*控制层类的基类*/
public class BaseController {
    public static final int OK = 200;

    /**
     * 获取session对象中的uid
     * @param session session对象
     * @return 当前登录的用户uid的值
     */
    protected final Integer getuidFromSession(HttpSession session) {
        return Integer.valueOf(session.getAttribute("uid")
                .toString());
    }

    /**
     * 获取当前登录用户的username
     * @param session session对象
     * @return 当前登录用户的用户名
     */
    protected final String getUsernameFromSession(HttpSession session){
        return session.getAttribute("username").toString();
    }
}

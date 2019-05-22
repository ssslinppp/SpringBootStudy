package com.ssslinppp.controller;

import com.ssslinppp.model.User;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 */
@RestController
@RequestMapping("/session")
public class LoginController {

    @RequestMapping("/show")
    String uid(HttpSession session) {
        UUID uid = (UUID) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID();
        }
        session.setAttribute("uid", uid);

        return session.getId();
    }

    @RequestMapping("/login")
    User login(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (ObjectUtils.isEmpty(user)) {
            request.getSession().setAttribute("user", User.getDemo());
        }
        return (User) request.getSession().getAttribute("user");
    }
}

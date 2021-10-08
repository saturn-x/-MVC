package controller;

import annotation.GetPath;
import annotation.PostPath;
import base.User;
import base.UserName;
import framework.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class Index {
    //建立一个哈希表当做数据库
    private final HashMap<Object, Object> userDatabase = new HashMap<>() {
        {
            List<User> users = List.of( //
                    new User("bob123123", "bob123", "This is bob.", "1@qq.com"),
                    new User("tom123123", "tom123", "This is Tom", "2@qq.com"));
            users.forEach(user -> {
                put(user.username, user);
            });
        }
    };
/*
该类存放访问路径，以及访问返回的modelandview
 */
    //访问主页
    @GetPath("/")
    public ModelAndView getIndex(HttpSession session, HttpServletRequest req){
        //查看是否已经登陆
        User user = (User) session.getAttribute("user");
        return new ModelAndView("index.html", "user", user);
    }
    @GetPath("/signin")
    public ModelAndView signin() {
        return new ModelAndView("/signin.html");
    }
    @PostPath("/signin")
    public ModelAndView doSignin(UserName bean, HttpServletResponse response, HttpSession session)
            throws IOException {
        //根据账户获取 对应的信息
        System.out.println("有账户正在登陆····");
        System.out.println("打印该账户"+bean.username+" "+bean.password);
        User user = (User) userDatabase.get(bean.username);
        if (user == null || !user.password.equals(bean.password)) {
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.write("{\"error\":\"Bad email or password\"}");
            pw.flush();
        } else {
            //登陆成功，设置session
            session.setAttribute("user", user);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.write("{\"result\":true}");
            pw.flush();
        }
        return null;
    }

}

package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.entity.User;
import com.mapper.UserMapper;
import com.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by admin on 2018/7/15.
 */
@Controller
public class LoginController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("login.html")
    public String login(HttpSession session, String name, String password){
//        System.out.println("do loginAction!");
        return "login";
    }

    @RequestMapping(value="loginValid.html", method= RequestMethod.POST)
    public String loginValid(HttpSession session, User user){
        System.out.println("loginValid");
        //根据用户名进行查询用户
        User queryUser = userMapper.queryUserByUsername(user.getUsername());
        //进行验证
        if(queryUser == null){
            //用户不存在

            return "redirect:login.html";
        }else if(user.getPassword().equals(queryUser.getPassword())){
            //用户存在
            session.setAttribute("user", queryUser);

            return "redirect:home.html";


        }else{
            //用户存在，密码不正确
            return "redirect:login.html";
        }
    }


    @RequestMapping("loginValidation.html")
    public void loginValidation(HttpServletResponse response, HttpSession session, String name, String value){

        JSONObject jsonObject = new JSONObject();
        if(name.equals("username")){//进行用户名验证

            User queryUser = userMapper.queryUserByUsername(value);

            if(queryUser == null){
                System.out.println(name);
                jsonObject.put("type", name);
            }else{
                session.setAttribute("user", queryUser);
            }
        }else{//进行密码验证
            User existUser = (User) session.getAttribute("user");
            if(existUser != null){
                User queryUser = userMapper.queryUserByUsername(existUser.getUsername());
                System.out.println(queryUser.getPassword());
                if(!value.equals(queryUser.getPassword())){
                    System.out.println(name);
                    jsonObject.put("type", name);
                }
            }
        }

        ResponseUtils.renderJson(response, jsonObject);
    }

}

package com.jueCeShu.controller;

import com.jueCeShu.bean.Result;
import com.jueCeShu.bean.User;
import com.jueCeShu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping(value = "/user",method = RequestMethod.GET)
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册
     *
     * @param user 参数封装
     * @return Result
     */
    @PostMapping(value = "/regist")
    public Result regist(User user) {
        userService.regist(user);

        return userService.regist(user);
    }

    /**
     * 登录
     *
     * @param user 参数封装
     * @return Result
     */
    //@RequestMapping
    @PostMapping(value = "/login")//@RequestParam("user")
    public ModelAndView login(User user, Map<String,Object> map) {
        System.out.println(user);
        //ModelAndView model =new ModelAndView();
        if (!((user.getUsername().isEmpty()||user.getUsername()==null)&&
                (user.getPassword().isEmpty()||user.getPassword()==null))) {
            //可以进行数据库比对

            Result result = userService.login(user);
            System.out.println(result);
            if (result.isSuccess() == true) {
                //登录成功，跳转到我的页面
                System.out.println("跳转到我的页面");
                return new ModelAndView("upload.html");//"mypage";
            }
        }
        //登录失败，重新登录
        map.put("msg","用户名或密码错误，请重新登录");
        return new ModelAndView("login.html");//"login";
        //return userService.login(user);
    }

    /**
     *
     * @param map
     * @return
     *
     *
     *     @RequestMapping("/mypage")
     *     public String text(){
     *         //接收用户上传的txt文件数据 ps此处有补充
     *         return "mypage";
     *     }
     */

    //@RequestMapping(value = "/success" ,method = RequestMethod.POST)
    @PostMapping(value = "/success")
    public String success(Map<String,String> map){
        map.put("hello","nihao");
        return "success";
    }
}

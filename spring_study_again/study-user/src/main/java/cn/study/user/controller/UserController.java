package cn.study.user.controller;

import cn.study.user.pojo.User;
import cn.study.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    /*
        @GetMapping("{id}")     使用@PathVariable("id")接收
        如果是链接后面加？的参数   使用@RequestParam接收
        如果是表单，直接使用属性名字作为形参接收
     */
    @GetMapping("{id}")
    @ResponseBody
    public User queryUserById(@PathVariable("id")Long id){
        return this.userService.queryUserById(id);
    }

    @GetMapping("/all")
    public String all(ModelMap model) {
        // 查询用户
        List<User> users = this.userService.queryAll();
        // 放入模型
        model.addAttribute("users", users);
        // 返回模板名称（就是classpath:/templates/目录下的html文件名）
        return "users";
    }


    @GetMapping("test")
    @ResponseBody
    public String test(){
        return "hello user";
    }
}

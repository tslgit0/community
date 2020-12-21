package life.majiang.community.controller;

import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }
    @PostMapping("/publish")
    public String dopublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
    HttpServletRequest request,
    Model model){
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if (title==null || title==""){
            model.addAttribute("error","标题不能为空");
            return "publish";

        }
        if (description=="" || description==null){
            model.addAttribute("error","补充不能为空");
            return "publish";
        }
        Cookie[] cookies =request.getCookies();

        User user=null;
        try {

            for (Cookie c:cookies){
                if (c.getName().equals("token"))
                {
                    String token = c.getValue();
                    user =userMapper.findBytoken(token);
                    if(user!=null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }


        if(user==null){
            System.out.println("用户未登录");
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        else
        {
            System.out.println("用户一登录");
        }
        Question question =new Question();
        question.setDescription(description);
        question.setTitle(title);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);
        return "redirect:/";


    }
}

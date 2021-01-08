package life.majiang.community.controller;

import life.majiang.community.mapper.NotificationMapper;
import life.majiang.community.model.Notification;
import life.majiang.community.model.NotificationExample;
import life.majiang.community.model.User;
import life.majiang.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;

@Controller
public class NotificationController {
    @Autowired
    NotificationService notificationService;
    @Autowired
    NotificationMapper notificationMapper;

    @GetMapping("/notification/{id}")
    public String notification(@PathVariable(name = "id") Long id,
                               HttpServletRequest request){
        //根据id，更改notification status
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }


        Long outerId=notificationService.read(user,id);
        return "redirect:/question/" + outerId;
    }
}

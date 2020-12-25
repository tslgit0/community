package life.majiang.community.service;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    public void createOrUpdate(User user) {
        //根据user 的acountid 查一下数据库中有没有
        User dbUser=userMapper.findByAccountId(user.getAccountId());
        if(dbUser!=null){
            //插入
        }
    }
}
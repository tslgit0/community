package life.majiang.community.service;

import life.majiang.community.dto.NotificationDTO;
import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.enums.NotificationStatusEnum;
import life.majiang.community.enums.NotificationTypeEnum;
import life.majiang.community.mapper.NotificationMapper;
import life.majiang.community.mapper.QuestionExtMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    QuestionExtMapper questionExtMapper;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    NotificationMapper notificationMapper;
    private Integer totalPage;

    //page 第几页 offset 当前问题起始

    public PaginationDTO list(Long id, Integer page, Integer size){
        PaginationDTO paginationDTO = new PaginationDTO();

        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id);
        notificationExample.setOrderByClause("gmt_create desc");

        Integer totalCount = (int)notificationMapper.countByExample(notificationExample);
        page= paginationDTO.setPagenation(totalCount,page,size);
        Integer offset=size*(page-1);
        List<Notification> notifications=notificationMapper.selectByExampleWithRowbounds(notificationExample,new RowBounds(offset,size));

        if (totalCount%size==0){
            totalPage=totalCount/size;

        }
        else {
            totalPage=totalCount/size+1;
        }
        for (Notification n:notifications
        ) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(n,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(n.getType()));
            notificationDTOS.add(notificationDTO);
        }

        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }


    public Long countUnread(Long id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus()).andReceiverEqualTo(id);

        Long totalCount = notificationMapper.countByExample(notificationExample);
        return totalCount;
    }

    public Long read(User user, Long id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andIdEqualTo(id);
        Notification notification = new Notification();
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByExampleSelective(notification,notificationExample);
        Long outerId=notificationMapper.selectByPrimaryKey(id).getOuterid();
        return outerId;
    }
}

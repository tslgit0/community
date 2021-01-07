package life.majiang.community.service;

import life.majiang.community.dto.CommentCreateDTO;
import life.majiang.community.dto.CommentDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.*;
import life.majiang.community.model.Comment;
import life.majiang.community.model.CommentExample;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    QuestionExtMapper questionExtMapper;
    @Autowired
    CommentExtMapper commentExtMapper;
    @Autowired
    UserMapper userMapper;
    //加入事务，不成功就回滚
    @Transactional
    public void insert(Comment comment) {
        if(comment.getParentId()==null || comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        //comment type异常 只可能是给问题 或者评论回复 代码分别是1 2
        if(comment.getType()==null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType()==CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment=commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
//            Comment ParentComment=commentMapper.selectByPrimaryKey(comment.getParentId());
//            commentExtMapper.incCommentCount(comment);
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());

            commentExtMapper.incCommentCount(parentComment);
        }
        else{
            //回复问题
            Question dbquestion=questionMapper.selectByPrimaryKey(comment.getParentId());
            if(dbquestion==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            questionExtMapper.incComment(dbquestion);
        }
    }



    public List<CommentDTO> listByTargetId(Long id,CommentTypeEnum type) {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> commentList=commentMapper.selectByExample(commentExample);
        if (commentList.size()==0) return new ArrayList<>();
        for (Comment c:commentList
             ) {
            CommentDTO commentDTO=new CommentDTO();
            BeanUtils.copyProperties(c,commentDTO);
            User user= userMapper.selectByPrimaryKey(c.getCommentator());
            commentDTO.setUser(user);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
}

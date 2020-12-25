package life.majiang.community.mapper;

import life.majiang.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {


    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    public void create(Question quesion);
    @Select("select * from question limit #{offset},#{size} ")
    //查询不需要参数 需要返回
    public List<Question> list(Integer offset, Integer size) ;
    @Select("select * from question where creator= #{userId} limit #{offset},#{size} ")
    //查询不需要参数 需要返回
    public List<Question> listByUserId(Integer userId,Integer offset, Integer size) ;
    @Select("select count(1) from question")
    Integer cout();


    @Select("select count(1) from question where creator=#{userId}")
    Integer countById(Integer userId);
    @Select("select * from question where id= #{id}")
    Question getById(Integer id);
}

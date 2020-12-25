package life.majiang.community.mapper;

import life.majiang.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into user(name,account_id,token,gmt_create,gmt_modified,avatar_url) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    public void insert(User user);
    @Select("select * from user where token = #{token}")
    User findBytoken(@Param("token") String token);
    @Select("select * from user where id=#{creator}")
    User findById(Integer creator);
    @Select("select * from user where account_id=#{accountId}")
    User findByAccountId(String accountId);
    @Update("update user set token=#{token},name=#{name},gmt_modified=#{gmtModified},avatar_url=#{avatarUrl} where account_id=#{accountId}")
    void updateUser(User user);
}

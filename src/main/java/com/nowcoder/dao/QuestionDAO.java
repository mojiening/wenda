package com.nowcoder.dao;

import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import jdk.nashorn.internal.runtime.QuotedStringTokenizer;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2017/5/5.
 */
@Mapper
public interface QuestionDAO {
   public static final String TABLE_NAME="question";
    public static final String INSERT_FIELDS="title,content,created_date,user_id,comment_count";
    public static final String SELECT_FIELDS="id,"+INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(",INSERT_FIELDS,
            ") values(#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    @Select({"select ",SELECT_FIELDS," from",TABLE_NAME," where id=#{id}"})
    Question selectById(int id);
    List<Question> selectLatestQuestion(@Param("userId") int userId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    @Update({"update ",TABLE_NAME," set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Delete({"delete from ",TABLE_NAME," where id=#{id}"})
    void deleteById(int id);
}

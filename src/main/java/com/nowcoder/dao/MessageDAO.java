package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2017/5/5.
 */
@Mapper
public interface MessageDAO {
   public static final String TABLE_NAME="message";
    public static final String INSERT_FIELDS="from_id,to_id,created_date,content,has_read,conversation_id";
    public static final String SELECT_FIELDS="id,"+INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(",INSERT_FIELDS,
            ") values(#{fromId},#{toId},#{createdDate},#{content},#{hasRead},#{conversationId})"})
    int addMessage(Message message);


    @Select({"select ", SELECT_FIELDS," from ",TABLE_NAME," where conversation_id=#{conversationId} order by created_date desc limit #{offset}, #{limit}"})
    List<Message> selectConversationDetail(@Param("conversationId") String conversationId,
    @Param("offset") int offset,
 @Param("limit") int limit);


 @Select({"select ", INSERT_FIELDS," , count(id) as id from ( select * from ",TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id order by created_date desc limit #{offset}, #{limit}"})
 List<Message> selectConversationList(@Param("userId") int userId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

 @Select({"select count(id) from ", TABLE_NAME," where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
 int selectCoversationUnreadCount(@Param("userId") int userId,@Param("conversationId") String conversationId);
}

package com.nowcoder.dao;

import com.nowcoder.model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Created by Administrator on 2017/5/11.
 */
@Mapper
public interface LoginTicketDAO {
    public static final String TABLE_NAME="login_ticket";
    public static final String INSERT_FIELDS="user_id,expired,status,ticket";
    public static final String SELECT_FIELDS="id,"+INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket ticket);

    @Select({"select ",SELECT_FIELDS," from ", TABLE_NAME," where ticket=#{ticket}"})
    LoginTicket selectTicket(String ticket);

    @Update({"update ",TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket,@Param("status") int status);
}

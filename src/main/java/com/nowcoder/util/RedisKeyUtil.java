package com.nowcoder.util;

/**
 * Created by Administrator on 2017/6/5.
 */
public class RedisKeyUtil {
    private static  String SPLIT=":";
    private static  String BIZ_LIKE="LIKE";
    private static  String BIZ_DISLIKE="DISLIKE";
    private static  String BIZ_EVENTQUEUE="EVENT_QUEUE";

    //粉丝
    private static String BIZ_FOLLOWER="FOLLOWER";
    //关注对象
    private static String BIZ_FOLLOWEE="FOLLOWEE";


   public static String getLikeKey(int entityType,int entityId){
       return BIZ_LIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
   }
   public static String getDislikeKey(int entityType,int entityId){
       return BIZ_DISLIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
   }

   public static String getEventQueueKey(){
       return BIZ_EVENTQUEUE;
   }

   public static String getFollowerKey(int entityType,int entityId)
   {
       return BIZ_FOLLOWER+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
   }

    public static String getFolloweeKey(int userId,int entityType)
    {
        return BIZ_FOLLOWEE+SPLIT+String.valueOf(userId)+SPLIT+String.valueOf(entityType);
    }
}
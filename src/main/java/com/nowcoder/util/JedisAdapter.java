package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.controller.LoginController;
import com.nowcoder.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/6/5.
 */
@Service
public class JedisAdapter implements InitializingBean{
    private static final Logger logger= LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool=new JedisPool("redis://localhost:6379/10");
    }

    public  long sadd(String key, String value)
    {
        Jedis jedis=null;
        try
        {
            jedis=pool.getResource();
          return   jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                 jedis.close();
            }
        }
      return 0;

    }

    public long srem(String key,String value){
        Jedis jedis=null;
        try
        {
            jedis=pool.getResource();
            return   jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key){
        Jedis jedis=null;
        try
        {
            jedis=pool.getResource();
            return   jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key,String value){
        Jedis jedis=null;
        try
        {
            jedis=pool.getResource();
            return   jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
        return false;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key,value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Jedis getJedis(){
        return pool.getResource();
    }

    public Transaction multi(Jedis jedis){
        try {
          return   jedis.multi();
        }catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
          return null;
    }

    public List<Object> exec(Transaction tx,Jedis jedis){
        try{
            return tx.exec();
        }catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(tx!=null){
                try{
                    tx.close();
                }catch (IOException ioe) {
                    logger.error("发生异常" + ioe.getMessage());
                }
            }
            if(jedis!=null){
                    jedis.close();
            }
        }
        return null;
    }

    public long zadd(String key,double score,String value){
        Jedis jedis=null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key,score,value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Set<String> zrevrange(String key, int start, int end){
        Jedis jedis=null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key,start,end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long zcard(String key){
        Jedis jedis=null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Double zscore(String key,String member){
        Jedis jedis=null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key,member);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

   /* public static void print(int index, Object obj)
    {
     System.out.println(String.format("%d,%s",index,obj.toString()));
    }

    public static void main(String[] args)
    {
        Jedis jedis=new Jedis("redis://localhost:6379/9");
        jedis.flushDB();

        jedis.set("hello","world");
        jedis.rename("hello","newhello");
        jedis.setex("hello2",15,"world");//15秒过期

        jedis.set("pv","100");
        jedis.incr("pv");
        jedis.incrBy("pv",5);
        jedis.decrBy("pv",3);
        jedis.keys("*");

        //list
        String listName="list";
        jedis.del(listName);
        for(int i=0;i<10;i++)
        {
            jedis.lpush("list","a"+String.valueOf(i));
        }
        print(4,jedis.lrange(listName,0,3));
        print(5,jedis.llen(listName));
        print(6,jedis.lpop(listName));
        print(7,jedis.llen(listName));
        print(8,jedis.lrange(listName,2,6));
        print(9,jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER,"a4","xx"));
        print(9,jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE,"a4","ss"));
        print(10,jedis.lrange(listName,0,12));


        //hash
        String userKey="userxx";
        jedis.hset(userKey,"name","jim");
        jedis.hset(userKey,"age","12");
        jedis.hset(userKey,"phone","1244484839");
        print(11,jedis.hget(userKey,"name"));
        print(12,jedis.hgetAll(userKey));
        jedis.hdel(userKey,"phone");
        print(13,jedis.hgetAll(userKey));
        print(14,jedis.hexists(userKey,"email"));
        print(15,jedis.hexists(userKey,"age"));
        print(16,jedis.hkeys(userKey));
        print(17,jedis.hvals(userKey));
        jedis.hsetnx(userKey,"school","HIT");
        jedis.hsetnx(userKey,"name","ning");
        print(18,jedis.hgetAll(userKey));

        //set
        String likeKey1="commentLike1";
        String likeKey2="commentLike2";
        for(int i=0;i<10;i++) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i * i));
        }
            print(19,jedis.smembers(likeKey1));
            print(20,jedis.smembers(likeKey2));
            print(21,jedis.sunion(likeKey1,likeKey2));
            print(22,jedis.sdiff(likeKey1,likeKey2));
            print(23,jedis.sinter(likeKey1,likeKey2));
            print(24,jedis.sismember(likeKey1,"12"));
            print(25,jedis.sismember(likeKey2,"16"));
            jedis.srem(likeKey1,"5");
            jedis.smove(likeKey2,likeKey1,"2");
            jedis.scard(likeKey1);


            //sorted set
            String rankKey="rankKey";
            jedis.zadd(rankKey,15,"jim");
            jedis.zadd(rankKey,60,"ben");
            jedis.zadd(rankKey,90,"lee");
            jedis.zadd(rankKey,75,"lecu");
            jedis.zadd(rankKey,34,"mei");
            print(26,jedis.zcard(rankKey));
            print(27,jedis.zcount(rankKey,60,100));
            print(28,jedis.zscore(rankKey,"jim"));
            print(29,jedis.zincrby(rankKey,2,"jim"));
            print(30,jedis.zrange(rankKey,0,10));
            print(31,jedis.zrevrange(rankKey,0,2));
            for(Tuple tuple:jedis.zrangeByScoreWithScores(rankKey,"60","100")){
                print(32,tuple.getElement()+":"+String.valueOf(tuple.getScore()));
            }
            print(33,jedis.zrank(rankKey,"jim"));
        print(45,jedis.get("pv"));

        JedisPool pool=new JedisPool();
        for(int i=0;i<100;i++)
        {
            Jedis j=pool.getResource();
            print(45,j.get("pv"));
            j.close();
        }

         User user=new User();
         user.setName("xx");
         user.setPassword("ppp");
         user.setHeadUrl("a.png");
         user.setSalt("salt");
         user.setId(1);
         //序列化对象，存到redis中
         jedis.set("user1", JSONObject.toJSONString(user));
        print(46,JSONObject.toJSONString(user));

        //反序列化取出对象
        String value=jedis.get("user1");
        User user2= JSON.parseObject(value,User.class);
        print(47,user2);


        }*/

}

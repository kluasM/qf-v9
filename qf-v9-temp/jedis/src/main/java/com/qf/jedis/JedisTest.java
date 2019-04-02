package com.qf.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/20
 */
public class JedisTest {

    @Test
    public void stringTest(){
        Jedis jedis = new Jedis("192.168.77.137",6379);
        jedis.auth("java1809");
        jedis.set("target","redis");
        System.out.println(jedis.get("target"));

        //api设计很
        jedis.mset("k1","v1","k2","v2");
        List<String> values = jedis.mget("k1", "k2");
        for (String value : values) {
            System.out.println(value);
        }
    }

    @Test
    public void hashTest(){
        Jedis jedis = new Jedis("192.168.77.137",6379);
        jedis.auth("java1809");

        Map<String, String> map = jedis.hgetAll("book:1");
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey()+"->"+entry.getValue());
        }
    }

    @Test
    public void ListTest(){
        Jedis jedis = new Jedis("192.168.77.137",6379);
        jedis.auth("java1809");
        //WRONGTYPE Operation against a key holding the wrong kind of value
        jedis.lpush("targets","money","house","wife");
        List<String> targets = jedis.lrange("targets", 0, -1);
        for (String s : targets) {
            System.out.println(s);
        }
    }

    @Test
    public void setTest(){
        Jedis jedis = new Jedis("192.168.77.137",6379);
        jedis.auth("java1809");

        jedis.sadd("luckymans","1001","1003");
        if(!jedis.sismember("luckymans","1003")){
            jedis.sadd("luckymans","1003");
        }else{
            System.out.println("1003用户，您已经得到了抢购资格，请勿重复抢购！");
        }
    }

    @Test
    public void zsetTest(){
        Jedis jedis = new Jedis("192.168.77.137",6379);
        jedis.auth("java1809");

        Map<String, Double> map = new HashMap<String, Double>();
        map.put("国家召开两会",100000.0);
        map.put("科比来深圳",10000.0);
        jedis.zadd("hot:search",map);

    }

    @Test
    public void hashSetTest(){
        //hashSet是按照hash算法去存储元素
        //出来只是无序，但不是随机，每次出来的顺序是一样的
        HashSet<String> set = new HashSet<String>();
        set.add("aaa");
        set.add("bbb");
        set.add("ccc");

        for (String s : set) {
            System.out.println(s);
        }
    }
}

import com.qf.entity.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void stringTest() throws IllegalAccessException, InstantiationException {
        //1,差异，每次先确定操作的类型
        //redisTemplate.opsForValue().set("mytarget","house");
        //System.out.println(redisTemplate.opsForValue().get("mytarget"));
        //2.默认会对我们key和value都做序列化操作
        //好的，保存对象，对象转换为json
        //单独配置
        //redisTemplate.setValueSerializer(JdkSerializationRedisSerializer.class.newInstance());
        /*Student student = new Student("zhangsan",3000000);
        redisTemplate.opsForValue().set("student:3",student);
        Object o = redisTemplate.opsForValue().get("student:3");
        System.out.println(o);//? ox 地址信息 Student{name='banzhang', money=1000000}*/
        //3.key-string
        //4.叠加的操作,value的序列化方式必须是字符串的方式
        redisTemplate.opsForValue().set("sifangqian2","10");
        redisTemplate.opsForValue().increment("sifangqian2",10);
        System.out.println(redisTemplate.opsForValue().get("sifangqian2"));
    }

    @Test
    public void hashTest(){
        Map<String,String> map = new HashMap();
        map.put("name","把时间浪费在美好的事物上");
        map.put("auth","java");
        redisTemplate.opsForHash().putAll("book:101",map);
    }

    @Test
    public void multiTest(){
        /*redisTemplate.opsForList().leftPush("user:101:follow","32");
        redisTemplate.opsForList().leftPushAll("user:101:follow","11","22");
*/
        /*List range = redisTemplate.opsForList().range("user:103:follow", 0, -1);
        for (Object o : range) {
            System.out.println(o);
        }*/
        redisTemplate.execute(new SessionCallback() {
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForList().leftPush("user:103:follow","6");
                operations.opsForList().leftPush("user:6:fans","1");
                //这个地方非常神奇，set没有问题，list拿不到东西
                /*List range = operations.opsForList().range("user:103:follow", 0, -1);
                for (Object o : range) {
                    System.out.println(o);
                }*/
                operations.exec();
                /*List range = operations.opsForList().range("user:103:follow", 0, -1);
                for (Object o : range) {
                    System.out.println(o);
                }*/
                return null;
            }
        });
    }

    @Test
    public void noPipelineTest(){
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            redisTemplate.opsForValue().set("k"+i,"v"+i);
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);//14890
    }

    @Test
    public void pipeLineTest(){
        long start = System.currentTimeMillis();
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            public Object execute(RedisOperations operations) throws DataAccessException {
                for (int i = 0; i < 10000; i++) {
                    operations.opsForValue().set("kk"+i,"vv"+i);
                }
                return null;
            }
        });
        long end = System.currentTimeMillis();
        System.out.println(end-start);//14890 1396
    }

    @Test
    public void sendMessageTest(){
        redisTemplate.convertAndSend("nba","game over!");
    }

    @Test
    public void ttlTest(){
        redisTemplate.opsForValue().set("dream","house");
        redisTemplate.expire("dream",10, TimeUnit.MINUTES);
    }
}

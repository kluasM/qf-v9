package com.qf.qfv9userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.api.search.IUserService;
import com.qf.v9.common.base.BaseServiceImpl;
import com.qf.v9.common.base.IBaseDao;
import com.qf.v9.common.pojo.ResultBean;
import com.qf.v9.entity.TUser;
import com.qf.v9.mapper.TUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/22
 */
@Component
@Service
public class UserServiceImpl extends BaseServiceImpl<TUser> implements IUserService{

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public TUser checkLogin(TUser user) {
        TUser currentUser = userMapper.selectByUsername(user.getUsername());
        if(currentUser != null){
            if(passwordEncoder.matches(user.getPasswd(),currentUser.getPasswd())){
                return currentUser;
            }
        }
        return null;
    }

    @Override
    public ResultBean checkIsLogin(String uuid) {
        //
        StringBuilder redisKey = new StringBuilder("user:token:").append(uuid);
        //
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        TUser currentUser = (TUser) redisTemplate.opsForValue().get(redisKey.toString());
        if(currentUser != null){
            //刷新凭证的有效期
            //30分钟 20->30
            redisTemplate.expire(redisKey.toString(),30, TimeUnit.MINUTES);
            //return ResultBean.success(currentUser.getUsername());
            return new ResultBean(200,currentUser);
        }
        return ResultBean.error("用户未登录");
    }

    @Override
    public IBaseDao<TUser> getBaseDao() {
        return userMapper;
    }
}

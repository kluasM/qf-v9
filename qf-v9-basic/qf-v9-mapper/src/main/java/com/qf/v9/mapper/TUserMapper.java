package com.qf.v9.mapper;

import com.qf.v9.common.base.IBaseDao;
import com.qf.v9.entity.TUser;

public interface TUserMapper extends IBaseDao<TUser>{
    TUser selectByUsername(String username);
}
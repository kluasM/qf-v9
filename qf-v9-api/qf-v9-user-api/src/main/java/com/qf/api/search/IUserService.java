package com.qf.api.search;

import com.qf.v9.common.base.IBaseService;
import com.qf.v9.common.pojo.ResultBean;
import com.qf.v9.entity.TUser; /**
 * @author HuangGuiZhao
 * @Date 2019/3/22
 */
public interface IUserService extends IBaseService<TUser>{
    TUser checkLogin(TUser user);

    public ResultBean checkIsLogin(String uuid);
}

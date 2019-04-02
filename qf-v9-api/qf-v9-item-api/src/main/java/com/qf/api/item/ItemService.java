package com.qf.api.item;

import com.qf.v9.common.pojo.ResultBean;

import java.util.List;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/15
 */
public interface ItemService {

    ResultBean createHtmlById(Long productId);

    ResultBean batchCreateHtml(List<Long> idList);
}

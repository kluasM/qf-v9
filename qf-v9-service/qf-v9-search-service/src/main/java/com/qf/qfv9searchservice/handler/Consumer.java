package com.qf.qfv9searchservice.handler;

import com.qf.api.search.ISearchService;
import com.qf.v9.common.constant.RabbitMQConstant;
import com.qf.v9.entity.TProduct;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/18
 */
@Component
public class Consumer {

    @Autowired
    private ISearchService searchService;

    @RabbitListener(queues = RabbitMQConstant.BACKGROUND_PRODUCT_SAVE_UPDATE_QUEUE)
    public void processAddOrUpdate(Long productId){
        searchService.updateById(productId);
    }
}

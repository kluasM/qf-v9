package com.qf.qfv9itemservice.handler;

import com.qf.api.item.ItemService;
import com.qf.v9.common.constant.RabbitMQConstant;
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
    private ItemService itemService;

    @RabbitListener(queues = RabbitMQConstant.BACKGROUND_PRODUCT_SAVE_UPDATE_ITEM_QUEUE)
    public void processAddOrUpdate(Long productId){
        itemService.createHtmlById(productId);
    }
}

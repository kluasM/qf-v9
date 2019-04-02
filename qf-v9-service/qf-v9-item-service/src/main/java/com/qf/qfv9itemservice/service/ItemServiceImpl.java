package com.qf.qfv9itemservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.api.item.ItemService;
import com.qf.v9.common.pojo.ResultBean;
import com.qf.v9.entity.TProduct;
import com.qf.v9.mapper.TProductMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/15
 */
@Component
@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    private TProductMapper productMapper;

    @Autowired
    private Configuration configuration;

    //JDK 提供了线程池
    //1.单例线程池，特点是只有线程，保证我们提交给他的任务按顺序执行
    //存在隐患：队列太长了Integer.MAX_VALUE
    //OOM内存溢出
    //private ExecutorService pool = Executors.newSingleThreadExecutor();
    //2.定长线程池
    //存在隐患：队列太长了Integer.MAX_VALUE
    //OOM内存溢出
    //private ExecutorService pool2 = Executors.newFixedThreadPool(10);
    //3.线程的数量只受限于内存
    //存在的隐患：创建的线程数最大为Integer.MAX_VALUE
    //OOM内存溢出
    //private ExecutorService pool3 = Executors.newCachedThreadPool();
    //线程池的关键参数
    //初始化线程池数(4)，最大线程数(8)，发呆时间(3000)，等待队列(100)
    //4个忙不过来，排队
    //队满了，创建新的线程（最大线程数）

    //结合真实服务器硬件条件来设置
    private int corePoolSize = Runtime.getRuntime().availableProcessors();
    //结论就是自己来创建线程池
    private ExecutorService pool = new ThreadPoolExecutor(
            corePoolSize,corePoolSize*2,
            0L, TimeUnit.SECONDS,new LinkedBlockingQueue(100));

    @Value("${html.locations}")
    private String htmlLocations;

    @Override
    public ResultBean createHtmlById(Long productId) {
        //1.根据id获取数据
        TProduct product = productMapper.selectByPrimaryKey(productId);
        try {
            //2.数据结合模板生成静态页
            Template template = configuration.getTemplate("item.ftl");
            Map<String,Object> data = new HashMap<>();
            data.put("product",product);
            FileWriter writer = new FileWriter(htmlLocations+productId+".html");
            template.process(data,writer);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            return ResultBean.error("生成静态页面失败！");
        }
        System.out.println("ok!!!!");
        return ResultBean.success("生成静态页面成功！");
    }

    @Override
    public ResultBean batchCreateHtml(List<Long> idList) {
        List<Future> list = new ArrayList<>(idList.size());
        for (Long id : idList) {
            //提交一个任务给线程池去调用线程并且执行
            list.add(pool.submit(new CreateHtmlTask(id)));
            //createHtmlById(id);
        }
        //看看执行的结果
        for (Future future : list) {
            try {
                System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return ResultBean.success("批量生成页面成功！");
    }

    private class CreateHtmlTask implements Callable<Boolean>{

        private Long productId;

        public CreateHtmlTask(Long productId){
            this.productId = productId;
        }

        @Override
        public Boolean call() throws Exception {
            //生成静态页面
            //1.根据id获取数据
            TProduct product = productMapper.selectByPrimaryKey(productId);
            try {
                //2.数据结合模板生成静态页
                Template template = configuration.getTemplate("item.ftl");
                Map<String,Object> data = new HashMap<>();
                data.put("product",product);
                FileWriter writer = new FileWriter(htmlLocations+productId+".html");
                template.process(data,writer);
            } catch (IOException | TemplateException e) {
                e.printStackTrace();
                return false;
            }
            System.out.println("ok!!!!");
            return true;
        }
    }


}

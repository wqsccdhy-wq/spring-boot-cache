package com.wq.cache.service;

import com.wq.cache.bean.Book;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author wangqing
 * @Desc 监听RabbitMQ，接收消息
 * @Date: 2020-02-24 15:30
 * @since 2020-2-24 15:30
 */
@Service
public class BookService {

    @RabbitListener(queues = "atguigu.news")
    public void recMsg(Book book){
        System.out.println("id:" + book.getId() + "|name:" + book.getName());
    }

}

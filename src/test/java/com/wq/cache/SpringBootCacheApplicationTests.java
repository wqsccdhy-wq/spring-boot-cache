package com.wq.cache;

import com.wq.cache.bean.Book;
import com.wq.cache.bean.Department;
import com.wq.cache.bean.Employee;
import com.wq.cache.config.RedisTemplateConfig;
import com.wq.cache.mapper.DepartmentMapper;
import com.wq.cache.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class SpringBootCacheApplicationTests {

    private final static Logger logger = LoggerFactory.getLogger(SpringBootCacheApplicationTests.class);

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    DepartmentMapper departmentMapper;

    /**
     * Redis操作对象
     */
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis操作字符串
     */
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * RabbitTemq操作对象
     */
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
        Employee employee = employeeMapper.getEmployeeById(3);
        System.out.println(employee);

//        Employee employee = new Employee();
//        employee.setLastName("李四");
//        employee.setEmail("lishi@126.com");
//        employee.setGender(1);
//        employee.setdId(1);
//        employeeMapper.insertEmployee(employee);
//        Employee employee = employeeMapper.getEmployeeById(4);
////        System.out.println(employee);
        Department department = new Department();
        department.setDepartmentName("开发部");
        departmentMapper.insertDepartment(department);

    }

    /**
     * opsForValue ：对应 String（字符串）
     * opsForZSet：对应 ZSet（有序集合）
     * opsForHash：对应 Hash（哈希）
     * opsForList：对应 List（列表）
     * opsForSet：对应 Set（集合）
     * opsForGeo：对应 GEO（地理位置
     */
    @Test
    public void testString(){
        //stringRedisTemplate.opsForValue() 操作字符串
        //stringRedisTemplate.opsForValue().append("msg","001");
        //stringRedisTemplate.opsForValue().set("k1","value");
        String k1 = stringRedisTemplate.opsForValue().get("k1");
        logger.info("k1：" + k1);

        //stringRedisTemplate.opsForList().leftPush("list1","1");
        //stringRedisTemplate.opsForList().leftPush("list1","2");
        //stringRedisTemplate.opsForList().leftPushAll("list1",new String[]{"dfdg","dgdaasdf","gdadfdf"});

    }

    @Test
    public void testObject(){
        Employee employee = employeeMapper.getEmployeeById(3);
//        redisTemplate.opsForValue().set("emp" + employee.getId(),employee);
        Employee emp = (Employee) redisTemplate.opsForValue().get("emp" + employee.getId());
        logger.info("emp:" + emp);

        Department department = departmentMapper.getEmployeeById(2);
//        redisTemplate.opsForValue().set("dept" + department.getId(),department);
        Department dept = (Department) redisTemplate.opsForValue().get("dept" + department.getId());

        logger.info("dept:" + dept);

    }

    /**
     * RabbitMq单播
     */
    @Test
    public void tesSendtDirect(){
       // rabbitTemplate.convertAndSend("exchange.direct","atguigu","单播测试");
        Book book = new Book(3331l,"Java开发3331" ,"Tom猫3331");
        //rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend("exchange.direct","atguigu.news",book);
    }

    @Test
    public void testRecDirect(){
        //Message message = rabbitTemplate.receive("atguigu");
        Book book = (Book) rabbitTemplate.receiveAndConvert("atguigu.news");
        System.out.println(book);
    }

}

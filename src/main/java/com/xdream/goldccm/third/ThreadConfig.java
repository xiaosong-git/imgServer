package com.xdream.goldccm.third;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * spring线程池配置
 */
@Configuration
//@PropertySource("classpath:thread.properties")
public class ThreadConfig {

//    //线程池维护线程的最少数量
//    @Value("${spring.corePoolSize}")
//    private Integer corePoolSize;
//    //允许的空闲时间
//    @Value("${spring.keepAliveSeconds}")
//    private Integer keepAliveSeconds;
//    //线程池维护线程的最大数量
//    @Value("${spring.maxPoolSize}")
//    private Integer maxPoolSize;
//    //缓存队列
//    @Value("${spring.queueCapacity}")
//    private Integer queueCapacity;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setKeepAliveSeconds(200);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(20);
        ThreadPoolExecutor.CallerRunsPolicy callerRunsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();
        //对拒绝task的处理策略
        //callerRunsPolicy用于被拒绝任务的处理程序，它将抛出RejectedExecutionException
        executor.setRejectedExecutionHandler(callerRunsPolicy);
        executor.initialize();
        return executor;
    }


}

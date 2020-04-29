package com.juan.shop.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 流量消息线程池配置
 *
 * @author liguanhuan
 */
@Configuration
public class ThreadPoolConfiguration {

    /**
     * 核心线程数：线程池创建时候初始化的线程数
     */
    @Value("${executor.corePoolSize}")
    private int corePoolSize;

    /**
     * 最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
     */
    @Value("${executor.maxPoolSize}")
    private int maxPoolSize;

    /**
     * 缓冲队列200：用来缓冲执行任务的队列
     */
    @Value("${executor.queueCapacity}")
    private int queueCapacity;

    /**
     * 允许线程的空闲时间(单位：秒)：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
     */
    @Value("${executor.keepaliveSeconds}")
    private int keepAliveSeconds;

    /**
     * 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
     */
    @Value("${executor.threadNamePrefix}")
    private String threadNamePrefix;

    @Bean(name = "myExecutor")
    public ThreadPoolTaskExecutor MessageExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(threadNamePrefix);

        // 线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
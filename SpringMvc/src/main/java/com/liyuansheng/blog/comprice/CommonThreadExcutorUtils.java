package com.liyuansheng.blog.comprice;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @Author: dainan
 * @Date: 2019/4/28 17:20
 * @Description:
 */
public class CommonThreadExcutorUtils {
    /**
     * 创建固定队列长度线程池
     */
    public static final ExecutorService FixedThreadPoolexecutorService = newFixedThreadPool(100);

    public static  ExecutorService  getDefaultExecutorService(){
        return FixedThreadPoolexecutorService;
    }

}

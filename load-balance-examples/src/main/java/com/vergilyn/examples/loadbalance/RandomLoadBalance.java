package com.vergilyn.examples.loadbalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机算法（Random Load Balance）：
 * <pre>
 *     随机把请求分配给后端服务器。请求分配的均匀程度依赖于随机算法了，因为实现简单，常常用于配合处理一些极端的情况，如出现热点请求，
 *     这个时候就可以random到任意一台后端，以分散热点。
 *     当然缺点也不言而喻。
 * </pre>
 *
 * 备注：代码参考SEATA源码
 * @author vergilyn
 * @date 2020-02-29
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected <T> T doSelect(List<T> invokers) {
        int length = invokers.size();

        // 参考dubbo中的写法
        return invokers.get(ThreadLocalRandom.current().nextInt(length));
    }
}

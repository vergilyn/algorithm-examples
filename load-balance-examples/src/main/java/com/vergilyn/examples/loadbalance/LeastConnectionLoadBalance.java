package com.vergilyn.examples.loadbalance;

import java.util.List;

/**
 * 最小连接数：
 * <pre>
 *   将任务分配给此时具有最小连接数的节点，因此它是动态负载均衡算法。
 *   一个节点收到一个任务后连接数就会加1，当节点故障时就将节点权值设置为0，不再给节点分配任务。
 * </pre>
 * @author vergilyn
 * @date 2020-02-29
 */
public class LeastConnectionLoadBalance extends AbstractLoadBalance {
    @Override
    protected <T> T doSelect(List<T> invokers) {
        return null;
    }
}

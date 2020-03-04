package com.vergilyn.examples.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询（Round Robin Load Balance）：
 * <pre>
 *     轮询是一种很简单的实现，依次将请求分配给后端服务器。
 *     优点：就是实现简单，请求均匀分配。
 *     缺点：也恰恰在于请求均匀分配，因为后端服务器通常性能会有差异，所以希望性能好的服务器能够多承担一部分。也不适合对长连接和命中率有要求的场景。
 * </pre>
 *
 * 备注：代码参考至SEATA源码
 * @author vergilyn
 * @date 2020-02-29
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance {
    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    @Override
    protected <T> T doSelect(List<T> invokers) {
        int length = invokers.size();
        int sequence = SEQUENCE.getAndIncrement();

        // 如果达到最大值`2147483647`，继续getAndIncr `-2147483648`、`-2147483648`...
        sequence = sequence < 0 ? -sequence : sequence;
        return invokers.get(sequence % length);
    }

    /**
     * SEATA 源码中的写法
     */
    private int getPositiveSequence() {
        do {
            int current = SEQUENCE.get();
            int next = current >= Integer.MAX_VALUE ? 0 : current + 1;
            if (SEQUENCE.compareAndSet(current, next)) {
                return current;
            }
        }while (true);
    }

}

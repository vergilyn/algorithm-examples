# load-balance-examples (负载均衡)

- [负载均衡](https://baike.baidu.com/item/%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1/932451)
- [讲解一下常用的负载均衡策略有哪些](https://www.2cto.com/kf/201805/744031.html)

负载均衡算法主要分为两类：静态、动态 。
静态负载均衡算法：固定的概率分配任务，不考虑服务器的状态信息，如轮询算法、加权轮询算法等；
动态负载均衡算法：服务器的实时负载状态信息来决定任务的分配，如最小连接法、加权最小连接法等。

代码参考：
- com.netflix.ribbon:ribbon-loadbalancer-2.3.0
- org.apache.dubbo:dubbo-2.7.3 (org.apache.dubbo.rpc.cluster.loadbalance)

dubbo 中的Random、Round-Robin都扩展了加权（weight）。
在后端服务器性能差异较大的情况下，高权值的服务可以承受更多的连接负载。
感觉大部份都可以引入weight（除开hash和consistent-hash）

## 代码实现参考
1. [github, seata]\: 实现超级简单，就几行代码。（未引入 weight权重）
- random
- round-robin

2. [github, dubbo]\: 引入了 weight权重
- random
- round-robin
- least-active
- consistent-hash

3. `com.netflix.ribbon`
TBD

[github, seata]: https://github.com/seata/seata
[github, dubbo]: https://github.com/apache/dubbo



## 1. Random (随机算法)
随机把请求分配给后端服务器。请求分配的均匀程度依赖于随机算法了，因为实现简单，常常用于配合处理一些极端的情况，如出现热点请求，
这个时候就可以random到任意一台后端，以分散热点。  
当然缺点也不言而喻。

## 2. Round Robin (轮询)
依次将请求分配的后端服务器。

**优点：**
就是实现简单，请求均匀分配。

**缺点：**  
恰恰在于请求均匀分配，因为后端服务器通常性能会有差异，所以希望性能好的服务器能够多承担一部分。  
也不适合对长连接和命中率有要求的场景。

### 2.1 Weighted Round Robin (加权轮询)
加权本质是一种带优先级的方式，加权轮询就是一种改进的轮询算法，轮询算法是权值相同的加权轮询。  
需要给后端每个服务器设置不同的权值，决定分配的请求数比例。  
**这个算法应用就相当广泛，对于无状态的负载场景，非常适合。**

**优点：**解决了服务器性能不一的情况；  
**缺点：**是权值需要静态配置，无法自动调节。

也不适合对长连接和命中率有要求的场景。

- [负载均衡算法--最小连接数法（Least Connections）](https://blog.csdn.net/claram/article/details/90290397)

## 3. Least Connection (最小连接法， weight)
将任务分配给此时具有最小连接数的节点，因此它是动态负载均衡算法。  
一个节点收到一个任务后连接数就会加1，当节点故障时就将节点权值设置为0，不再给节点分配任务。

最小连接法适用于各个节点处理的性能相似时。任务分发单元会将任务平滑分配给服务器。  
但当服务器性能差距较大时，就无法达到预期的效果。  
因为，此时连接数并不能准确表明处理能力，连接数小而自身性能很差的服务器可能不及连接数大而自身性能极好的服务器。  
所以，在这个时候就会导致任务无法准确的分配到剩余处理能力强的机器上。

**同样可以通过加权优化**


## 4. Least Active (最小活跃数，dubbo源码)
> [Dubbo负载均衡：最少活跃数(LeastActive)的实现分析](https://blog.csdn.net/revivedsun/article/details/71330126)
> 例如，每个服务维护一个活跃数计数器。  
> 当A机器开始处理请求，该计数器加1，此时A还未处理完成，若处理完毕则计数器减1。  
> 而B机器接受到请求后很快处理完毕。那么A,B的活跃数分别是1，0。  
> 当又产生了一个新的请求，则选择B机器去执行(B活跃数最小)，这样使慢的机器A收到少的请求。

感觉得跟least-connection类似，但是有区别。
例如，现在有4个节点，10个请求。
least-connection，最终每个节点处理的请求数趋近平衡（3 3 2 2）。
但least-active会根据节点的处理能力，那么可能A服务器处理极慢，B服务器处理极快，那么可能的处理分配（1 5 2 2）。

## 5. Least Response Time (最短响应时间)
把请求分配给平均响应时间最短的后端服务器。  
平均响应时间可以通过ping探测请求或者正常请求响应时间获取。

RT(Response Time)是衡量服务器负载的一个非常重要的指标。对于响应很慢的服务器，说明其负载一般很高了，应该降低它的QPS。

之前有人说使用CPU占用率作为负载均衡的指标，只能说没理解CPU占用率的实质。  
理论上CPU占用率是越高越好，说明服务充分利用了CPU资源。  
但对于设计不合理的程序导致的CPU占用过高这是程序的设计问题，并不违背这条理论。

## 6. Hash (哈希)

## 7. Consistent Hash (一致性哈希)
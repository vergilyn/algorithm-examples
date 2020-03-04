# algorithm-examples

## load-balance-examples （负载均衡策略）
代码实现参考：
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


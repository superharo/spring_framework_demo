> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [blog.csdn.net](https://blog.csdn.net/z2431435/article/details/124978166)

### 文章目录

*   [消息队列](#_1)
*   [为什么不使用 Redis 发布订阅 (pub/sub) 来实现消息队列](#Redis__pubsub__18)
*   [Stream](#Stream_26)
*   *   [消息队列相关命令：](#_35)
    *   [消费者组相关命令：](#_45)
*   [Stream 最简单的生产、消费模型](#Stream_60)
*   [Stream 优点 / 改进](#Stream__65)
*   *   [Stream 支持「阻塞式」拉取消息](#Stream__67)
    *   [支持发布 / 订阅模式](#___73)
    *   *   [XADD key ID field value [field value ...]](#XADD_key_ID_field_value_field_value__78)
    *   [Stream 能保证消息不丢失，重新消费](#Stream__136)
    *   [Stream 数据会写入到 RDB 和 AOF 做持久化](#Stream__RDB__AOF__147)
    *   [消息堆积时，Stream 的处理](#Stream_150)
*   [专业的消息队列](#_169)

[消息队列](https://so.csdn.net/so/search?q=%E6%B6%88%E6%81%AF%E9%98%9F%E5%88%97&spm=1001.2101.3001.7020)
====================================================================================================

“消息队列” 是在消息的传输过程中保存消息的容器。“消息” 是在两台计算机间传送的数据单位。消息队列管理器在将消息从它的源中继到它的目标时充当中间人。[队列](https://so.csdn.net/so/search?q=%E9%98%9F%E5%88%97&spm=1001.2101.3001.7020)的主要目的是提供路由并保证消息的传递；如果发送消息时接收者不可用，消息队列会保留消息，直到可以成功地传递它。  
当我们在使用一个消息队列时，希望它的功能如下：

```
支持阻塞等待拉取消息
支持发布 / 订阅模式
消费失败，可重新消费，消息不丢失
实例宕机，消息不丢失，数据可持久化
消息可堆积
```

消息队列 (Message Queue) 是一种应用间的通信方式，消息发送后可以立即返回，有消息系统来确保信息的可靠专递，消息生产者只管把消息发布到 MQ 中而不管谁来取，消息消费者只管从 MQ 中取消息而不管谁发布的，这样发布者和使用者都不用知道对方的存在。

首先，我们可以知道，消息队列是一种异步的工作机制，比如说日志收集系统，为了避免数据在传输过程中丢失，还有订单系统，下单后，会生成对应的单据，库存的扣减，消费信息的发送，一个下单，产生这么多的消息，都是通过一个操作的触发，然后将其他的消息放入消息队列中，依次产生。再就是很多网站的，秒杀活动之类的，前多少名用户会便宜，都是通过消息队列来实现的。

这些例子，都是通过消息队列，来实现，业务的解耦，最终数据的一致性，广播，错峰流控等等，从而完成业务的逻辑。

为什么不使用 Redis 发布订阅 (pub/sub) 来实现消息队列
===================================

Redis [Stream](https://so.csdn.net/so/search?q=Stream&spm=1001.2101.3001.7020) 主要用于消息队列（MQ，Message Queue），Redis 本身是有一个 Redis 发布订阅 (pub/sub) 来实现消息队列的功能，支持发布 / 订阅，支持多组生产者、消费者处理消息。但是存在很多问题：  
1. 消费者下线，数据会丢失。  
2. 不支持数据持久化，Redis 宕机，数据也会丢失，Pub/Sub 没有基于任何数据类型实现，不会写入到 RDB 和 AOF 中，当 Redis 宕机重启，Pub/Sub 的数据也会全部丢失。  
3. 消息堆积，缓冲区溢出，消费者会被强制踢下线，数据也会丢失。缓冲区的默认配置：client-output-buffer-limit pubsub 32mb 8mb 60。它的参数含义如下：  
32mb：缓冲区一旦超过 32MB，Redis 直接强制把消费者踢下线。  
8mb + 60：缓冲区超过 8MB，并且持续 60 秒，Redis 也会把消费者踢下线。

Stream
======

Redis Stream 提供了消息的持久化和主备复制功能，可以让任何客户端访问任何时刻的数据，并且能记住每一个客户端的访问位置，还能保证消息不丢失。

Redis Stream 有一个消息链表，将所有加入的消息都串起来，每个消息都有一个唯一的 ID 和对应的内容。每个 Stream 都有唯一的名称，它就是 Redis 的 key，在我们首次使用 xadd 指令追加消息时自动创建。  
![](https://img-blog.csdnimg.cn/0fe1905138df45ebbc0168cdef5e0056.png)  
Consumer Group ：消费组，使用 XGROUP CREATE 命令创建，一个消费组有多个消费者 (Consumer)。  
last_delivered_id ：游标，每个消费组会有个游标 last_delivered_id，任意一个消费者读取了消息都会使游标 last_delivered_id 往前移动。  
pending_ids ：消费者 (Consumer) 的状态变量，作用是维护消费者的未确认的 id。 pending_ids 记录了当前已经被客户端读取的消息，但是还没有 ack (Acknowledge character：确认字符）。

消息队列相关命令：
---------

XADD - 添加消息到末尾  
XTRIM - 对流进行修剪，限制长度  
XDEL - 删除消息  
XLEN - 获取流包含的元素数量，即消息长度  
XRANGE - 获取消息列表，会自动过滤已经删除的消息  
XREVRANGE - 反向获取消息列表，ID 从大到小  
XREAD - 以阻塞或非阻塞方式获取消息列表

消费者组相关命令：
---------

XGROUP CREATE - 创建消费者组  
XREADGROUP GROUP - 读取消费者组中的消息  
XACK - 将消息标记为 "已处理"  
XGROUP SETID - 为消费者组设置新的最后递送消息 ID  
XGROUP DELCONSUMER - 删除消费者  
XGROUP DESTROY - 删除消费者组  
XPENDING - 显示待处理消息的相关信息  
XCLAIM - 转移消息的归属权  
XINFO - 查看流和消费者组的相关信息；  
XINFO GROUPS - 打印消费者组的信息；  
XINFO STREAM - 打印流信息

Stream 最简单的生产、消费模型
==================

XADD：发布消息  
XREAD：读取消息  
![](https://img-blog.csdnimg.cn/9c6e4f9aa2df4e0ca336221dc9828c07.png)

Stream 优点 / 改进
==============

Stream 支持「阻塞式」拉取消息
------------------

读取消息时，只需要增加 BLOCK 参数即可支持「阻塞式」拉取消息。  
// BLOCK 0 表示阻塞等待，不设置超时时间  
127.0.0.1:6379> XREAD COUNT 5 BLOCK 0 STREAMS queue 1618469127777-0  
这时，消费者就会阻塞等待，直到生产者发布新的消息才会返回。

支持发布 / 订阅模式
-----------

XADD 向队列添加消息，如果指定的队列不存在，则创建一个队列  
XGROUP：创建消费者组  
XREADGROUP：在指定消费组下，开启消费者拉取消息  
首先，生产者依旧发布 2 条消息：

### XADD key ID field value [field value …]

向队列添加消息，如果指定的队列不存在，则创建一个队列  
key ：队列名称，如果不存在就创建  
ID ：消息 id，我们使用 * 表示由 redis 生成，可以自定义，但是要自己保证递增性，* 表示让 Redis 自动生成唯一的消息 ID。  
field value ： 记录。  
比如：`XADD mystream * field1 value1 field2 value2 field3 value3`

```
127.0.0.1:6379> XADD queue * name a 
"1618470740565-0"
127.0.0.1:6379> XADD queue * name b 
"1618470743793-0"
```

发布后要开启 2 组消费者处理同一批数据，就需要创建 2 个消费者组：  
// 创建消费者组 1，0-0 表示从头拉取消息

```
127.0.0.1:6379> XGROUP CREATE queue group1 0-0
```

// 创建消费者组 2，0-0 表示从头拉取消息

```
127.0.0.1:6379> XGROUP CREATE queue group2 0-0
```

消费者组创建好之后，我们可以给每个「消费者组」下面挂一个「消费者」，让它们分别处理同一批数据。  
XREADGROUP GROUP 读取消费组中的消息。  
第一个消费组开始消费：  
// group1 的 consumer 开始消费，> 表示拉取最新数据

```
127.0.0.1:6379> XREADGROUP GROUP group1 consumer COUNT 5 STREAMS queue 

1) 1) "queue" 2) 1) 1) "1618470740565-0" 2) 1) "name" 2) "a" 2) 1) "1618470743793-0" 2) 1) "name" 2) "b"
```

同样地，第二个消费组开始消费：

// group2 的 consumer 开始消费，> 表示拉取最新数据

```
127.0.0.1:6379> XREADGROUP GROUP group2 consumer COUNT 5 STREAMS queue

1) 1) "queue" 2) 1) 1) "1618470740565-0" 2) 1) "name" 2) "a" 2) 1) "1618470743793-0" 2) 1) "name" 2) "b"
```

我们可以看到，这 2 组消费者，都可以获取同一批数据进行处理了。  
![](https://img-blog.csdnimg.cn/4b3b0f8bdc32438da621431366c68701.png)  
// 消费者重新上线，0-0 表示重新拉取未 ACK 的消息

```
127.0.0.1:6379> XREADGROUP GROUP group1 consumer1 COUNT 5 STREAMS queue 0-0
// 之前没消费成功的数据，依旧可以重新消费
1) 1) "queue" 2) 1) 1) "1618472043089-0" 2) 1) "name" 2) "a" 2) 1) "1618472045158-0" 2) 1) "name" 2) "b"4)
```

Stream 能保证消息不丢失，重新消费
--------------------

当一组消费者处理完消息后，需要执行 XACK 命令告知 Redis，这时 Redis 就会把这条消息标记为「处理完成」。  
// group1 下的 1618472043089-0 消息已处理完成

```
127.0.0.1:6379> XACK queue group1 
1618472043089-0
```

![](https://img-blog.csdnimg.cn/8c6ded5f367a429c8455d8b1a9b3d94a.png)  
消费者异常宕机，肯定不会发送 XACK，那么 Redis 就会依旧保留这条消息。  
待这组消费者重新上线后，Redis 就会把之前没有处理成功的数据，重新发给这个消费者。这样一来，即使消费者异常，也不会丢失数据了。

Stream 数据会写入到 RDB 和 AOF 做持久化
----------------------------

Stream 是新增加的数据类型，它与其它数据类型一样，每个写操作，也都会写入到 RDB 和 AOF 中。  
我们只需要配置好持久化策略，这样的话，就算 Redis 宕机重启，Stream 中的数据也可以从 RDB 或 AOF 中恢复回来。

消息堆积时，Stream 的处理
----------------

当消息队列发生消息堆积时，一般只有 2 个解决方案：  
生产者限流：避免消费者处理不及时，导致持续积压  
丢弃消息：中间件丢弃旧消息，只保留固定长度的新消息  
而 Redis 在实现 Stream 时，采用了第 2 个方案。

在发布消息时，你可以指定队列的最大长度，防止队列积压导致内存爆炸。

```
// 队列长度最大10000
127.0.0.1:6379> XADD queue MAXLEN 10000 * name a
"1618473015018-0"
```

当队列长度超过上限后，旧消息会被删除，只保留固定长度的新消息。  
这么来看，Stream 在消息积压时，如果指定了最大长度，还是有可能丢失消息的。

专业的消息队列
=======

1. 消息不丢  
2. 消息可堆积

使用一个消息队列，其实就分为三大块：生产者、队列中间件、消费者。  
![](https://img-blog.csdnimg.cn/3c3f094d727845e385ee542241227efe.png)  
消息是否会发生丢失，其重点也就在于以下 3 个环节：  
**生产者会不会丢消息？  
消费者会不会丢消息？  
队列中间件会不会丢消息？**

**当生产者在发布消息时，可能发生以下异常情况：**  
**消息没发出去**：网络故障或其它问题导致发布失败，中间件直接返回失败。消息根本没发出去，那么重新发一次就好了。  
**不确定是否发布成功**：网络问题导致发布超时，可能数据已发送成功，但读取响应结果超时了。为了避免消息丢失，只能继续重试，直到发布成功为止。生产者一般会设定一个最大重试次数，超过上限依旧失败，需要记录日志报警处理。  
**在使用消息队列时，要保证消息不丢，宁可重发，也不能丢弃。**

无论是 Redis 还是专业的队列中间件，生产者都是可以保证消息不丢的。因为大不了重发。

**消费者会不会丢消息？**  
消费者拿到消息后，还没处理完成，就异常宕机了，那消费者还能否重新消费失败的消息？  
消费者在处理完消息后，必须「告知」队列中间件，队列中间件才会把标记已处理，否则仍旧把这些数据发给消费者。  
这种方案需要消费者和中间件互相配合，才能保证消费者这一侧的消息不丢。  
无论是 Redis 的 Stream，还是专业的队列中间件，例如 RabbitMQ、Kafka，其实都是这么做的。

**队列中间件会不会丢消息？**  
Redis 在以下 2 个场景下，都会导致数据丢失：  
AOF 持久化配置为每秒写盘，但这个**写盘过程是异步的**，**Redis 宕机时会存在数据丢失的可能**  
**主从复制也是异步的**，**主从切换时，也存在丢失数据的可能（从库还未同步完成主库发来的数据，就被提成主库）**  
基于以上原因我们可以看到，**Redis 本身的无法保证严格的数据完整性。**

像 RabbitMQ 或 Kafka 这类专业的队列中间件，在使用时，一般是部署一个集群，生产者在发布消息时，队列中间件通常会写「多个节点」，以此保证消息的完整性。这样一来，即便其中一个节点挂了，也能保证集群的数据不丢失。

因为 Redis 的数据都存储在内存中，这就意味着一旦发生消息积压，则会导致 Redis 的内存持续增长，如果超过机器内存上限，依旧可能被强行删除。但 Kafka、RabbitMQ 这类消息队列就不一样了，它们的数据都会存储在磁盘上，磁盘的成本要比内存小得多，当消息积压时，无非就是多占用一些磁盘空间，相比于内存，在面对积压时也会更加轻松。

**Redis 相比于 Kafka、RabbitMQ，部署和运维更加轻量。如果你的业务场景足够简单，对于数据丢失不敏感，而且消息积压概率比较小的情况下，把 Redis 当作队列是完全可以的。  
如果你的业务场景对于数据丢失非常敏感，而且写入量非常大，消息积压时会占用很多的机器资源，那么我建议你使用专业的消息队列中间件。**
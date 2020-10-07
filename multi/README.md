# 多线程总结
  
## 一、线程池 （第一大法宝）
#### 1.线程池介绍
我这里理解为计划调度，复用总量相对固定的资源，合理分配完成任务.
    
#### 2.创建、停止线程池

线程池构造函数的参数    
  
| 参数名 | 类型 | 含义 |
| :-----| ----: | :----: |
| corePoolSize | int | 核心线程数 |
| maxPoolSize | int | 最大线程数 |
| keepAliveTime | long | 保持存活时间 |
| workQueue | BlockingQueue | 任务存储队列 |
| threadFactory | ThreadFactory | 线程池需要新线程时，由threadFactory来生成 |
| Handler | RejectedExecutionHandler | 线程池无法接受你所提交的任务的拒绝策略 |


线程池手动创建还是自动创建？手动创建更好，能够明确线程池运行规则，避免资源耗尽。（详见代码示例）

线程池例的线程数量设置多少比较合适？线程数=CPU核心数*（1+平均等待时间/平均工作时间）常规算法 （如需准确设置需要进行压测计算）
  
停止线程池的正确方法 （详见代码示例）

#### 3.常见线程池特点及用法 （详见代码示例）

#### 4.水满则溢，如何拒绝过多的任务？ 

拒绝策略：

AbortPolicy : 抛出异常  
DiscardPolicy : 丢弃  
DisCardOldestPolicy : 丢弃最老的任务  
CallerRunPolicy : 谁提交的线程谁来完成  

#### 5.钩子方法，给线程池加点料 （详见代码示例）

#### 6.实现原理及源码分析  

Executor 顶层接口 

ExecutorService  继承 Executor 并增加shutdown等操作方法

Executors  工具类 帮助快速创建线程池

#### 7.使用线程池的注意点  

避免任务堆积  
避免线程数过度增加  
排查线程泄漏  

线程池状态： 
 
RUNNING:  接受新任务并处理排队任务  
SHUTDOWN:  不接受新任务，但处理排队任务  
STOP:  不接受新任务，也不处理排队任务，并中断正在执行的任务  
TIDYING:  中文解释问整洁。可以理解为所有任务以终止。workerCount为0时，线程转到TIDYING状态，并运行terminate()钩子方法  
TERMINATED:  terminate() 执行完成

## 二、ThreadLocal
####1.应用场景  

1）每个线程需要一个独享的对象（通常时工具类，典型需要使用的类有SimpleDateFormat和Random）  
2）每个线程内需要保存全局变量（例如在拦截器中获取用户信息），可以让同方法直接使用，避免参数传递的麻烦  

####2.使用ThreadLocal的好处

线程安全

无需加锁，提高执行效率

高效的利用内存、节省开销，避免每次都重新创建对象

免去层层传递参数

####3.主要使用方法介绍

分析源码，了解Thread、ThreadLocal、ThreadLocalMap三者之间关系  
每个Thread对象中都持有一个ThreadLocalMap成员变量，1对1关系
每个ThreadLocalMap中包含N个ThreadLocal对象，1对多关系  

initialValue方法：返回当前线程对应的初始值，这是个延迟加载的方法，只有调用get时才会触发。如果先调用set方法设置值，则不会为线程调用initialValue方法
set方法：为这个线程设置一个新值
get方法：获取当前线程值
remove方法：清空当前线程值

ThreadLocal内存泄漏分析：ThreadLocalMap的每个Entry都是对key的弱引用。同时，每个Entry都包含一个对value的强引用。如果线程长时间不终止，导致强引用value无法被回收，就可能会发生OOM。

如何避免：在ThreadLocal使用完毕后调用remove方法，清除对象。

注意事项：

1）本身就是全局变量对象就不要使用ThreadLocal,拿到的还是全局变量本身，全局共享还是存在并发访问问题。

2）如果可以不使用ThreadLocal解决问题，就不要强行使用。

3）优先使用框架，没必要自己造 例如spring 成熟框架考虑较为周全

## 三、锁
####1.Lock接口 （详见以下代码示例）

####2.锁的分类 （详见以下代码示例）

从不同角度分类，一把锁可能存在多种类型，一种类型也可能包含多把锁，例如：ReentrantLock既是互斥锁，又是可重入锁

####3.乐观锁、悲观锁

悲观锁=互斥同步锁，阻塞和唤醒带来性能劣势，可能引起永久阻塞 （例如：synchronized、lock）

乐观锁=非互斥同步锁，原子类、并发容器，保证原子性

####4.可重入锁、非可重入锁

以ReentrantLock为例

好处：可以重复获取同一把锁、可以避免死锁、提升封装性

####5.公平锁、非公平锁

公平：按照线程请求顺序来分配锁。

非公平：不完全按照请求顺序，在一定情况下，可以插队。

####6.共享锁、排他锁

以ReentrantReadWriteLock为例

排他锁=独占锁、独享锁

共享锁=读锁

####7.自旋锁、阻塞锁

自旋锁：一般用于多核服务器，并发不是特别高的情况下比阻塞锁效率高。临界区比较短（线程一旦拿到锁，很久才释放。这种临界区较长的也不适合）

####8.可中断锁

synchronized：不可中断

Lock：可中断

####9.锁优化

自旋锁和自适应

锁消除、锁粗化（合并中间细节加锁解锁）

缩小同步代码块

尽量不要锁住方法

减少请求锁次数

避免人为制造"热点"

锁中尽量不再包含锁，避免死锁

选择合适锁类型或合适的工具类

## 四、Atomic 原子类
#### 1.作用和分类

比Lock更细粒度，能够保证基本类型或者将普通变量升级为具有原子功能 （详见代码示例）

Adder： Java8中引入，效率比atomicLong高，本质是空间换时间。竞争激烈时，是使用多段锁的理念，提高并发性。（详见代码示例）

Accumulator: 相当于通用加强的Adder （适用于大量且需并行计算）（详见代码示例）

## 五、CAS
#### 1.什么是CAS？

一定是在并发的场景下存在。（详见代码示例）

CPU的特殊指令

#### 2.应用场景

乐观锁

并发容器

原子类

#### 3.缺点

ABA问题 通过版本号，类似乐观锁的思路来解决

自旋时间过长

## 六、不变性
#### 1.什么是"不变性"？

如果对象在被创建后，状态就不能被修改，那么它就是不可变的。

#### 2.final关键字

类防止被继承、方法防止被重写、变量防止被修改。

天生是线程安全的，不需要额外的同步开销。

用法：

1.修饰变量，意味着被修饰的变量值不能被修改。

赋值时机：

1）在神明变量的等号右边直接赋值

2）构造函数中赋值

3）初始代码块中赋值（不常用）

2.修饰方法，意味着被修饰对象的引用不能变。

构造方法不允许final修饰

不可被重写，也就是不能被@Override,并且子类不能有同名方法

3.修饰类

不可被继承

#### 3.不变性和final的关系

不变性并不意味着，简单地用final修饰就是不可变

基本数据类型，被final修饰后确实具有不变性，但是对象类型，需要该对象保证自身及自身属性在创建后，状态永远不会变才可以

满足不可变的必要条件：

1）对象创建后，起状态就不能修改

2）所有属性都是final修饰

3）对象创建过程中没有发生溢出

#### 4.栈封闭技术

在方法里新建的局部变量，实际上是存储在每个线程私有的栈空间，而每个栈的栈空间是不能被其他线程访问到的，所以不会
有线程安全问题。这就是"栈封闭"技术，是"线程封闭"技术的一种情况。

## 七、并发容器

### 并发容器概览

ConcurrentHashMap：线程安全的HashMap

CopyOnWriteArrayList：线程安全的List

BlockingQueue：这是一个接口，表示阻塞队列，适用于数据共享通道

ConcurrentLinkedQueue：高效的非阻塞并发队列，使用链表实现。可以看作是一个线程安全的LinkedList

ConcurrentSkipListMap：是一个Map，使用跳表的数据结构进行快速查找（不常用）

缺点：数据一致性问题（只具备最终一致性，不具备迭代过程中实时一致。）、占用内存（写操作是通过复制机制）

1）为什么使用队列？

用队列可以在线程间传递数据：生产者消费者模式、银行转帐
考虑锁等线程安全问题转移到了"队列"上

2）并发队列简介

3）并发队列关系

4）阻塞队列

BlockingQueue take、put 阻塞方法  是否有届

ArrayBlockingQueue 有届、需要指定容量

LinkedBlockingQueue 无界、容量Integer.MAX_VALUE,内部结构：Node、两把锁。

PriorityBlockingQueue 支持优先级、自然顺序，无界。可以理解为PriorityQueue的线程安全版本

SynchronousQueue 容量为0。不做存储，不需要持有元素。它所做的就是直接传递，效率很高

DelayQueue  延迟队列，根据延迟排序，无界

阻塞队列和线程池等关系

5）非阻塞队列

ConcurrentLinkedQueue 通过CAS原理实现线程安全

6）选择合适的队列

考虑边界、空间、吞吐量

## 八、控制并发流程

1.什么是控制并发流程？

让线程之间互相配合，满足业务逻辑

2.CountDownLatch倒计时门闩 （详见代码示例）

3.Semaphore信号量 （详见代码示例）

4.Condition接口（又称条件对象）(详见代码示例)

5.CyclicBarrier循环栅栏 （详见代码示例）

## 九、AQS

#### 1.学习思路

理解原理、提升技术

#### 2.为什么需要AQS

并发控制、线程协作核心

#### 3.作用

用于构建锁、同步器、协作工具类的框架。

#### 4.重要性、地位

CountDownLatch、ReentrantLock、ReentrantReadWriteLock、ThreadPool等都用到了AQS

#### 5.内部原理

三大要素：

1.state状态 （协作工具类去定义，含义各不相同。例：ReentrantLock中state表示锁的占有情况，包括可重入计数。state为0时，表示Lock不被任何线程占有）

2.控制线程抢锁和配合的FIFO队列（这个队列用来存在等待的线程，AQS可以理解为排队管理器）

3.获取/释放等重要方法 （协作工具类去实现，含义各不相同）

#### 6.用法示例

第一步，写一个类，想好协作逻辑，实现获取/释放方法。

第二歩，内部写一个Sync类继承AbstractQueuedSynchronizer

第三步，根据是否独占来重写tryAcquire/tryRelease或tryAcquireShared（int acquires）和tryReleaseShared（int releases）等方法，在之前写等获取/释放方法中调用AQS的acquire/release或者Shared方法

#### 7.利用AQS实现自己的Latch门闩 （详见代码示例）

## 十、Future和Callable （第二大法宝）

1.Runnable的缺陷

1）没有返回值

2）不能抛出checked Exception （详见代码示例）

2.Callable接口

1）类似于Runnable，被其他线程执行的任务

2）实现call方法

3）有返回值

3.Future类

重要方法：

1）get()方法：用于获取结果 （任务已完成会立即返回结果，尚未完成将阻塞并直到任务完成。）

2）get(long timeout,TimeUnit unit)方法：带参数的get方法

3）cancel()方法：取消任务

4）isDone()方法：判断线程是否执行完毕

5）isCancelled()方法：判断是否被取消

4.用法1：线程池submit方法返回Future对象（详见代码示例）

5.用法2：用FutureTask来创建Future (详见代码示例)

6.Future注意点

## 十一、手写缓存 （详见代码）
# 第10章 高级知识点

- [10-1 高级知识点](#10-1-高级知识点)
  - [并行计算的方法](#并行计算的方法)
- [10-2 外部排序分析](#10-2-外部排序分析)
- [10-3 死锁分析](#10-3-死锁分析)
- [10-4 线程池介绍](#10-4-线程池介绍)
- [10-5 线程池-Java Excutor Framework演示（上）](#10-5-线程池-java-excutor-framework演示上)
- [10-6 线程池-Java Excutor Framework演示（下）](#10-6-线程池-java-excutor-framework演示下)
- [10-7 服务器Socket编程](#10-7-服务器socket编程)
- [10-8 线程池实现服务器](#10-8-线程池实现服务器)
- [10-9 NIO服务器](#10-9-nio服务器)
- [10-10 select模型的缺点](#10-10-select模型的缺点)
- [10-11 go语言实现异步服务器](#10-11-go语言实现异步服务器)
- [10-12 资源管理](#10-12-资源管理)
- [10-13 Java进阶知识点介绍](#10-13-java进阶知识点介绍)
- [10-14 Java垃圾回收(上)](#10-14-java垃圾回收上)
- [10-15 Java垃圾回收(下)](#10-15-java垃圾回收下)
- [10-16 Java内存模型1](#10-16-java内存模型1)
- [10-17 Java内存模型2](#10-17-java内存模型2)
- [10-18 异常处理](#10-18-异常处理)
- [10-19 架构演进](#10-19-架构演进)

### 10-1 高级知识点

并行计算、多线程、资源管理

#### 并行计算的方法

- 将数据拆分到每个节点上。如何拆分数据
- 每个节点并行的计算出结果。计算出什么样的结果
- 将结果汇总。如何汇总结果

### 10-2 外部排序分析

> 如何排序10个G的元素？

快速排序、归并排序、堆排序时间复杂度都是nlogn，但是内存占用较多

外部排序基于归并排序。一部分数据在内存中，一部分数据在磁盘或网络资源中

归并排序回顾：

- 将数据分为左右两半，分别归并排序，再把两个有序数据归并

- 如何归并

  ```
  [1, 3, 6, 7][1, 2, 3, 5] ==> 1
  [3, 6, 7][1, 2, 3, 5] ==> 1
  [3, 6, 7][2, 3, 5] ==> 2
  [3, 6, 7][3, 5] ==> 3
  [6, 7][3, 5] ==> 3
  [6, 7][5] ==> 5
  [6, 7][] ==> 6
  [7][] ==> 7
  [][]
  ```

  归并操作：分别选择左右两边最小的数，然后进行比较，如果小于另一边则选出，等于则选出左边的数（稳定排序）

  <img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gjwdlwrp8xj30yg0szwhb.jpg" alt="归并排序拆分数据.jpg" width="35%;" />
  
  将数据切分为内存中能够容纳数据段，然后将每一段放入每个节点进行排序计算，最后进行归并操作

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gjwcdc23qij312c0c8tbt.jpg" alt="K路归并.jpg" width=" 45%;" />

1. K路归并数据堆：完全二叉树，根是最小的元素，层序遍历为有序序列
2. PriorityQueue：q.push(2);q.push(1);q.push(3);q.push(4);    q.pop()==1

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gjwcrbytt8j315u0ietd9.jpg" alt="缓冲区数据归并操作.jpg" width="45%;" />

> 归并节点只需要将K路中每路最小的数据放在内存即可

归并：使用`Iterable<T>`接口

- 可以不断获取下一个元素的能力
- 元素存储/获取方式被抽象，与归并节点无关
- `Iterable<T> merge(List<Iterable<T>> sortedData);`

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gjwco2ejbcj30kf0kdaby.jpg" alt="Iterable接口获取元素.jpg" width="25%;" />

归并数据源来自`Iterable<T>.next()`

- 如果缓冲区空，读取下一批元素放入缓冲区
- 给出缓冲区第一个元素
- 可配置项：缓冲区大小，如何读取下一批元素

```java
public class Range implements Iterable<Long> {
  private final long start;
  private final long end;
  private final long step;
  public Range(long start, long end, long step) {
    this.start = start;
    this.end = end;
    this.step = step;
  }

  @Override public Iterator<Long> iterator() {
    return new RangeIterator();
  }

  private class RangeIterator implements Iterator<Long> {
    private long current;
    public RangeIterator() {
      this.current = start;
    }

    @Override public boolean hasNext() {
      return current < end;
    }
    @Override public Long next() {
      if (current >= end) { throw new NoSuchElementException(); }
      long value = current;
      current += step;
      return value;
    }
  }
}
```

```java
public class ExternalSort {
  private static class ResultEntry<T extends Comparable<T>> implements Comparable<ResultEntry<T>> {
    private final T value;
    private final Iterator<T> source;
    public ResultEntry(T value, Iterator<T> source) {
      this.value = value;
      this.source = source;
    }

    @Override public int compareTo(ResultEntry<T> o) {
      // compare only value with o.value
      return value.compareTo(o.value);
    }
  }

  private static class MergeResultIterator<T extends Comparable<T>> implements Iterator<T> {
    private final PriorityQueue<ResultEntry<T>> queue;
    public MergeResultIterator(List<Iterable<T>> sortedData) {
      // Collect minimum data in each sortedData.
      this.queue = new PriorityQueue<>();
      for (Iterable<T> data : sortedData) {
        Iterator<T> iterator = data.iterator();
        if (iterator.hasNext()) {
          this.queue.add(new ResultEntry<>(iterator.next(), iterator));
        }
      }
    }

    @Override public boolean hasNext() {
      return !queue.isEmpty();
    }
    // Finds minimum data in our collection.
    @Override public T next() {
      if (queue.isEmpty()) { throw new NoSuchElementException(); }
      ResultEntry<T> entry = queue.poll();
      // Replace extracted data with next minimum in its source.
      if (entry.source.hasNext()) {
        queue.add(new ResultEntry<>(entry.source.next(), entry.source));
      }
      return entry.value;
    }
  }

  public Iterable<Long> merge(List<Iterable<Long>> sortedData) {
    return () -> new MergeResultIterator<>(sortedData);
  }
}

```

```java
private static void printInitialResults(Iterable<Long> resultSmall, int resultsToPrint) {
  int count = 0;
  for (Long value : resultSmall) {
    System.out.print(value + " ");
    count++;
    if (count >= resultsToPrint) {
      break;
    }
  }
  System.out.println();
}
```

```java
Iterable<Long> data1 = new Range(1L, 1000000000000L, 1L);
Iterable<Long> data2 = new Range(1L, 1000000000000L, 2L);
Iterable<Long> data3 = new Range(1L, 1000000000000L, 3L);
Iterable<Long> data4 = new Range(1L, 1000000000000L, 5L);
Iterable<Long> data5 = new Range(1L, 1000000000000L, 7L);

Iterable<Long> smallData1 = new Range(1L, 10L, 1L); // 1, 2, 3, 4, 5, ... 9
Iterable<Long> smallData2 = new Range(1L, 10L, 2L); // 1, 3, 5, 7, 9

ExternalSort sort = new ExternalSort();

System.out.println("Testing small data set.");
Iterable<Long> resultSmall = sort.merge(Arrays.asList(smallData1, smallData2));
printInitialResults(resultSmall, 100);

System.out.println("Testing normal data set again.");
Iterable<Long> anotherResult = sort.merge(
  Arrays.asList(sort.merge(Arrays.asList(data1, data2)), sort.merge(Arrays.asList(data3, data4)), data5)
);
printInitialResults(anotherResult, 100);
```

### 10-3 死锁分析

多线程程序首先要注意线程安全性问题，同一个数据在不同的线程对他进行读写操作时可能会出现一些数据安全的问题，想要保证线程安全就需要对程序进行加锁，但锁可能会影响程序效率，所以尽可能降低锁的粒度，降低锁的粒度又可能引起死锁的问题

死锁分析

```java
void transfer(Account from, Account to, int amount) {
  synchronized (from) {
    synchronized (to) {
      from.setAmount(from.getAmount() - amount);
      to.setAmount(to.getAmount + amount);
    }
  }
}
```

- 在任何地方都可以线程切换，甚至在一句语句中间

- 要尽力设想对自己**最不利**的情况

- synchronized(from) ===> 别的线程在等待from

  synchronized(to) ===> 别的线程已经锁住了to

  可能死锁：transfer(a, b, 100)和transfer(b, a, 100)同时进行

死锁条件必须同时满足：互斥等待、保持与等待、循环等待、无法剥夺的等待

死锁防止：

- 破除互斥等待 ===> 一般无法破除
- 破除保持与等待 ===> 一次性获取所有资源
- 破除循环等待 ===> 按顺序获取资源
- 破除无法剥夺的等待 ===> 加入超时

### 10-4 线程池介绍

创建线程开销大，所以使用线程池预先建立好线程，等待任务派发

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gjxdr91217j313v0jbwih.jpg" alt="线程池工作示意图.jpg" width="45%;" />

线程池的参数

- corePoolSize：线程池中初始线程数量，可能处于等待状态
- maximumPoolSize：线程池中最大允许线程数量
- keepAliveTime：超出corePoolSize部分的线程如果等待这些时间将被回收

### 10-5 线程池-Java Excutor Framework演示（上）

```java
ExecutorService executorService = Executors.newFixedThreadPool(3);

List<Future<?>> taskResults = new ArrayList<>();
IntStream.rangeClosed(1, 10).forEach(i -> {
  taskResults.add(executorService.submit(new CodingTask(i)));
});
log.info("10 tasks dispatched successfully.");

executorService.shutdown();
log.info("All tasks finished.");
```

```java
@Slf4j
public class CodingTask implements Runnable {
  private final int employeeId;
  public CodingTask(int employeeId) {
    this.employeeId = employeeId;
  }

  @SneakyThrows @Override public void run() {
    log.info("Employee {} started writing code.", employeeId);
    TimeUnit.SECONDS.sleep(5);
    log.info("Employee {} finished writing code.", employeeId);
  }
}
```

### 10-6 线程池-Java Excutor Framework演示（下）

- 线程池的创建
- 任务派发
- 利用Future检查任务结果

### 10-7 服务器Socket编程



### 10-8 线程池实现服务器



### 10-9 NIO服务器



### 10-10 select模型的缺点



### 10-11 go语言实现异步服务器



### 10-12 资源管理

Java垃圾回收

- 不被引用的对象会被回收
- 垃圾回收包括Minor GC和Full GC
- 垃圾回收时所有运行暂停

Java资源管理（IO）

- 内存会被回收，资源不会被释放

- databaseConnection需要databaseConnection.close()来释放

  ```java
  try {
    Database databaseConnection = connect(...);
    databaseConnection.beginTransaction();
    // do work
  } catch (Exception e) {
    databseConnection.rollBack();
  } finally {
    databaseConnection.close(); // 不要抛异常，最好使用try-catch
  }
  ```

  ```java
  try (Database databaseConnection = connect(...)) {
    databaseConnection.beginTransaction();
    // do work
  } catch (Exception e) {
    databaseConnection.rollBack();
  }
  ```

C++资源管理

- C++没有finally、try-with-resource，但是有析构函数
- C++在析构函数执行delete操作释放内存

```c++
void doWork() {
  Database databaseConnection(...);
  try {
    databaseConnection.beginTransaction();
    // do work
  } catch (Exception& e) {
    databaseConnection.rollBack();
  }
}

Database::~Database() {
  // close database connection
}
```

### 10-13 Java进阶知识点介绍



### 10-14 Java垃圾回收(上)



### 10-15 Java垃圾回收(下)



### 10-16 Java内存模型1



### 10-17 Java内存模型2



### 10-18 异常处理



### 10-19 架构演进



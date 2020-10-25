# 第9章 设计模式

- [9-1 设计模式简介](#9-1-设计模式简介)
- [9-2 State模式](#9-2-state模式)
- [9-3 Decorator模式](#9-3-decorator模式)
  - [不太好的实现方式](#不太好的实现方式)
  - [Decorator Pattern](#decorator-pattern)
- [9-4 创建对象](#9-4-创建对象)
  - [总结](#总结)

### 9-1 设计模式简介

课程内容：设计模式简介、再谈Singleton、变继承关系为组合关系、如何创建对象

Singleton优缺点

- 优点：确保全局至多只有一个对象；用于构造缓慢的对象，需要统一管理的资源
- 缺点：很多全局状态，线程安全性

Singleton的创建

- 双重锁模式Double checked locking
- 作为Java类的静态变量
- 使用框架提供的能力，如依赖输入DI

### 9-2 State模式

变继承关系为组合关系

继承关系：描述is-a关系、不要用继承关系来实现复用，应该使用设计模式实现复用

如果Employee升级成了Manager怎们表述？

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gjsucvyerwj314g0i60y4.jpg" alt="Role模式表示Employee对象.jpg" width="45%;" />

```java
public class Employee {
  private final String name;
  private final int salary;
  private Role role;

  public Employee(String name, int salary, Role role) {
    this.name = name;
    this.salary = salary;
    this.role = role;
  }

  public void doWork() { role.doWork(); }

  public void setRole(Role role) { this.role = role; }
}
```

```java
public interface Role {
  void doWork();
}
```

```java
public class Engineer implements Role {
  @Override public void doWork() {
    System.out.println("Doing engineer work.");
  }

  @Override public String toString() { return "Engineer"; }
}
```

```java
public class Manager implements Role {
  private final List<Employee> reporters;
  public Manager(List<Employee> reporters) {
    this.reporters = Collections.unmodifiableList(new ArrayList<>(reporters));
  }

  @Override public void doWork() {
    System.out.println("Dispatching work");
    Employee worker = selectReporters();
    worker.doWork();
  }

  @Override public String toString() { return "Manager"; }

  private Employee selectReporters() {
    Assert.notEmpty(reporters, "Manager without reporters");
    return reporters.get(0);
  }
}
```

```java
employee2.setRole(new Manager(Collections.singletonList(employee1)));
```

### 9-3 Decorator模式

```java
@FunctionalInterface
public interface Runnable {
  public abstract void run();
}
```

如何实现LoggingRunable，TransactionalRunnable

#### 不太好的实现方式

```java
public abstract class LoggingRunable implements Runnable {
  protected abstract void doRun();

  @Override public void run() {
    long startTime = System.currentTimeMillis();
    System.out.println("Task started at " + startTime);
    doRun(); // working...
    long endTime = System.currentTimeMillis();
    System.out.println("Task finished at " + endTime);
    System.out.println("Elapsed time: " + (endTime - startTime));
  }
}
```

```java
public class CodingTask extends LoggingRunable {
  @SneakyThrows @Override
  protected void doRun() {
    System.out.println("Writing code.");
    TimeUnit.SECONDS.sleep(5);
  }
}
```

```java
new CodingTask().run();
```

如果现在还要添加TransactionalRunnable的功能，则不能同时与LoggingRunable实现

```java
public abstract class TransactionalRunnable implements Runnable {
  protected abstract void doRun();

  @Override public void run() {
    boolean shouldRollback = false;
    try {
      beginTransaction();
      doRun(); // working...
    } catch (Exception e) {
      shouldRollback = true;
      throw e;
    } finally {
      if (shouldRollback) { rollback(); } 
      else { commit(); }
    }
  }

  private void commit() { System.out.println("commit"); }
  private void rollback() { System.out.println("rollback"); }
  private void beginTransaction() { System.out.println("beginTransaction"); }
}
```

#### Decorator Pattern

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gjv46x6sp1j31540l742w.jpg" alt="Decorator Pattern实现.jpg" width="50%;" />

```java
public class CodingTask implements Runnable {
  @SneakyThrows @Override
  public void run() {
    System.out.println("Writing code.");
    TimeUnit.SECONDS.sleep(5);
  }
}
```

```java
public class LoggingRunable implements Runnable {
  private final Runnable innerRunnable;
  public LoggingRunable(Runnable innerRunnable) {
    this.innerRunnable = innerRunnable;
  }

  @Override
  public void run() {
    long startTime = System.currentTimeMillis();
    System.out.println("Task started at " + startTime);
    innerRunnable.run(); // working...
    long endTime = System.currentTimeMillis();
    System.out.println("Task finished at " + endTime);
    System.out.println("Elapsed time: " + (endTime - startTime));
  }
}
```

```java
public class TransactionalRunnable implements Runnable {
  private final Runnable innerRunnable;
  public TransactionalRunnable(Runnable innerRunnable) {
    this.innerRunnable = innerRunnable;
  }

  @Override
  public void run() {
    boolean shouldRollback = false;
    try {
      beginTransaction();
      innerRunnable.run(); // working...
    } catch (Exception e) {
      shouldRollback = true;
      throw e;
    } finally {
      if (shouldRollback) { rollback(); }
      else { commit(); }
    }
  }

  private void commit() { System.out.println("commit"); }
  private void rollback() { System.out.println("rollback"); }
  private void beginTransaction() { System.out.println("beginTransaction"); }
}
```

```java
new LoggingRunable(new CodingTask()).run();
new TransactionalRunnable(new LoggingRunable(new CodingTask())).run();
new LoggingRunable(new TransactionalRunnable(new CodingTask())).run();
```

### 9-4 创建对象

使用new创建对象的缺点：

1. 编译时必须决定创建哪个类的对象
2. 参数意义不明确

解决一：Abstract Factory Pattern

```java
task = new LoggingTask(new CodingTask());
task = taskFactory.createCodingTask();
```

解决二：Builder Pattern

> 不可变对象往往配合Builder使用

```java
employee = new Employee(oldEmployee.getName(), 15000);
employee = Employee.fromExisting(oldEmployee).withSalary(15000).build();
```

#### 总结

设计模式简介、再谈Singleton、变继承关系为组合关系、如何创建对象

# 第8章 面向对象

- [8-1 面向对象-概述](#8-1-面向对象-概述)
- [8-2 面向对象-类与对象](#8-2-面向对象-类与对象)
  - [逻辑结构](#逻辑结构)
  - [物理结构](#物理结构)
- [8-3 对象的特殊函数（上）](#8-3-对象的特殊函数上)
- [8-4 对象的特殊函数（下）](#8-4-对象的特殊函数下)
- [8-5 接口与抽象类](#8-5-接口与抽象类)
  - [接口](#接口)
  - [抽象类](#抽象类)
- [8-6 实现Iterable接口](#8-6-实现iterable接口)
- [8-7 继承](#8-7-继承)
- [8-8 封装](#8-8-封装)
- [8-9 面向对象-例题](#8-9-面向对象-例题)
- [8-10 面向对象-不可变性](#8-10-面向对象-不可变性)
- [8-11 泛型（上）](#8-11-泛型上)
- [8-12 泛型（下）](#8-12-泛型下)
- [8-13 虚函数表](#8-13-虚函数表)
- [8-14 面向对象-小结](#8-14-面向对象-小结)
- [8-15 面向对象问题](#8-15-面向对象问题)

### 8-1 面向对象-概述

面向对象思想：封装、继承、多态

知识点：类与对象、接口与实现、继承与封装、不可变对象、泛型

思维方式：

- 从用户（终端用户、使用代码的用户）的角度思考问题
- 摒弃完全基于逻辑的思维

### 8-2 面向对象-类与对象

> 类的成员变量 ===> 对象状态
>
> 类的成员函数 ===> 对象行为
>
> 类的静态变量
>
> 类的静态函数

#### 逻辑结构

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gjecvdx1goj315o0h3gtg.jpg" alt="类的逻辑结构.jpg" width="50%;" />

#### 物理结构

```java
employeeToPay.getPaaid(bank);

public class Employee {
  public static List<Employee> allEmployees;
  private String name;
  private int salary;

  public void doWork() {
    loadAllEmployees();
  }

  public void getPaid(BankEndPoint bank) {
    bank.payment(name, salary);
  }

  public static void loadAllEmployees() {
    // loads all employees from database.
  }
}
```

```java
public interface BankEndPoint {
  void payment(String name, int salary);
}
```

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gjed4lqq82j315j0kfdo6.jpg" alt="物理结构.jpg" width="50%;" />

```java
public class Accounting {
  private BankEndPoint bank;

  public void payAll() {
    Employee.loadAllEmployees();
    for (Employee employee : Employee.allEmployees) {
      employee.getPaid(bank);
    }
  }
}
```

类的静态对象，静态函数？

- 没有this引用，静态变量全局唯一一份
- 普通函数引用静态变量、函数？OK
- 对象上引用静态变量、函数？编译器警告
- 静态函数引用普通成员变量、函数？编译错误

### 8-3 对象的特殊函数（上）

类的特殊函数：构造函数、equals、hashCode、toString

```java
public Employee(String name, int salary) {
  this.name = name;
  this.salary = salary;
}

public Employee(String name) {
  this(name, 0);
}
```

```java
@Override
public boolean equals(Object o) {
  if (this == o) {
    return true;
  }
  if (o == null || getClass() != o.getClass()) {
    return false;
  }
  Employee employee = (Employee) o;
  return salary == employee.salary &&
    Objects.equals(name, employee.name);
}
```

```java
@Override
public int hashCode() {
  return Objects.hash(name, salary);
}
```

```java
Employee employee1 = new Employee("John", 10000);
Employee employee2 = new Employee("Mary", 20000);
Employee employee3 = new Employee("John");
employee3.salary = 10000;
System.out.println("(employee1==employee3) = " + (employee1 == employee3)); // false
System.out.println("employee1.equals(employee3) = " + employee1.equals(employee3)); // true
System.out.println("employee2.equals(employee3) = " + employee2.equals(employee3)); // false
Employee employee4 = null;
System.out.println("employee4.equals(employee3) = " + employee4.equals(employee3)); // NPE
```

### 8-4 对象的特殊函数（下）

A：a.hashCode() == b.hashCode()

B：a.equals(b)

> equals相等可以推导出hashCode相等
>
> equals相等是hashCode相等的充分条件，hashCode相等是equals相等的必要条件
>
> equals相等则hashCode一定相等，hashCode相等但equals不一定相等

```java
@Override
public String toString() {
  return "Employee{name='" + name + '\'' + ", salary=" + salary + '}';
}
```

### 8-5 接口与抽象类

#### 接口

```java
public interface BankEndPoint {
  void payment(String name, int salary);
}
```

为什么要有接口的概念？

1. 从用户（使用实现的代码）角度看问题
2. bankEndPoint.payment(name, salary);

与类相比：

- 由编译器强制的一个模块间协作的合约（Contract）
- 无成员变量
- 成员函数只有申明不能有实现

#### 抽象类

```java
public abstract class BankEndPoint {
  abstract void payment(String name, int salary);
}
```

接口与抽象类的区别

1. 抽象类可以有成员变量

2. 抽象类可以有部分实现

3. 抽象类不可以有多重继承，接口可以

   ```java
   public class ArrayList<E> extends AbstractList<E>
     implements List<E>, RandomAccess, Cloneable, java.io.Serializable
   { }
   ```

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gjeeqj0aksj31e00gywmm.jpg" alt="类与接口.jpg" width="55%;" />

抽象类提供公有的实现，接口描述类拥有的能力

### 8-6 实现Iterable接口

> 包装链表类，实现Iterable接口

```java
public class LinkedList<T> implements Iterable<T> {
  private Node<T> head;
  private Node<T> tail;
  private LinkedList() {
    head = null; tail = null;
  }

  public static <T> LinkedList<T> newEmptyList() {
    return new LinkedList<T>();
  }

  public void add(T value) {
    Node<T> node = new Node<>(value);
    if (tail == null) {
      head = node;
    } else {
      tail.setNext(node);
    }
    tail = node;
  }

  private static class ListIterator<T> implements Iterator<T> {
    private Node<T> currentNode;
    private ListIterator(Node<T> head) {
      this.currentNode = head;
    }

    @Override public boolean hasNext() {
      return currentNode != null;
    }

    @Override public T next() {
      if (currentNode == null) { throw new NoSuchElementException(); }
      T value = currentNode.getValue();
      currentNode = currentNode.getNext();
      return value;
    }
  }

  @Override public Iterator<T> iterator() {
    return new ListIterator<>(head);
  }
}
```

```java
LinkedList<Integer> list = LinkedList.newEmptyList();
for (int i = 0; i < 100; i++) { list.add(i); }
for (Integer value : list) {
  System.out.println(value);
}
```

- 阅读接口注解，严格按照合约来实现
- 循环不变式用于假想的循环中：add(),next()

练习：包装树类，实现Iterable接口

### 8-7 继承

接口：一个类实现一些功能来负责模块与别人的模块互相协作

继承与封装：自己在实现功能的时候去复用自己的代码

继承

- is-a关系

- 子类**增加或修改**基类（增加成员变量,函数）不能减少
- Java中所有函数都相当于C++虚函数

```java
public class Manager extends Employee {
  private List<Employee> reporters;
  public Manager(String name, int salary) {
    super(name, salary);
  }

  @Override public void getPaid(BankEndPoint bank) {
    getStocks();
    super.getPaid(bank);
  }

  @Override public void doWork() {
    Employee worker = selectReporters();
    worker.doWork();
  }

  @Override public String toString() {
    return "Manager{name='" + name + '\'' + ", salary=" + salary + '}';
  }

  private void getStocks() {
  }
  private Employee selectReporters() {
    return null;
  }
}
```

```java
LinkedList<Employee> employees = new LinkedList<>();
employees.add(new Employee("John", 10000));
employees.add(new Employee("Mary", 20000));
employees.add(new Employee("John"));

Employee manager = new Manager("Tony", 100000);
employees.add(manager);

for (Employee employee : employees) {
  System.out.println(employee);
}
```

### 8-8 封装

|           | 类内部 | 包内部 | 派生类 | 外部 |
| --------- | ------ | ------ | ------ | ---- |
| private   | Y      | N      | N      | N    |
| （默认）  | Y      | Y      | N      | N    |
| protected | Y      | Y      | Y      | N    |
| public    | Y      | Y      | Y      | Y    |

不建议在派生类中修改封装可见性

### 8-9 面向对象-例题

【单选】下列程序执行后结果为（    ）

```java
class A {
  public int func(int a, int b) {
    return a - b;
  }
}

class B extends A {
  public int func(int a, int b) {
    return a + b;
  }
}
```

```java
A a = new B();
B b = new B();
System.out.println(a.func(100, 50));
System.out.println(b.func(100, 50));
```

答：150，150

### 8-10 面向对象-不可变性

> 不可变对象（Immutable Objects）：值传递，可以缓存、线程安全

final关键字

- 类声明：类不可以被继承
- 函数声明：函数不可以在派生类中重写
- 变量声明：变量不可以指向其他对象，Object类型的变量内容还是可以修改
- static final变量：用于定义常量，名称一般大写

实现不可变性

- final关键字无法保证不可变性
- 从接口定义，类的实现上保证不可变性
- Collections.unmodifiableXXX()

```java
public class Employee {
  private final String name;
  private final int salary;

  public Employee(String name, int salary) {
    this.name = name;
    this.salary = salary;
  }
}
```

```java
public class Manager extends Employee {
  private final List<Employee> reporters; // = new ArrayList<>();

  public Manager(String name, int salary, List<Employee> reporters) {
    super(name, salary);
    this.reporters = Collections.unmodifiableList(new ArrayList<>(reporters));
  }

  private void loadReporters() {
    reporters.clear(); // java.lang.UnsupportedOperationException
  }
}
```

### 8-11 泛型（上）

从 `List` 到 `List<T>`

- 规定List中的元素只能是类型T
- `ArrayList<Integer>`、`ArrayList<String>`
- `LinkedList<Integer>`、`LinkedList<String>`

语法

```java
List<Integer> list1 = new ArrayList<>(); // Java1.7
List<Integer> list2 = Stream.of(1, 2, 3).collect(Collectors.toList());
List<Integer> list3 = Collections.emptyList();

Objects.equals(emptyIntList, LinkedList.<Integer> newEmptyList());
class ArrayList<T> { }
public static  <T> void printList(List<T> list) { }
```

### 8-12 泛型（下）

Java类型擦除（Type Erasure）

- 早期Java没有泛型，为了兼容性，在运行时将所有的泛型内容进行类型擦除
- 运行时：`List`、`List<String>`、`List<Integer>`没有区别

运行时如何知道泛型类型

```java
// 只能输出[1, 2, 3]
void <T> printList(List<T> list);
// 可以输出Integer list of：[1, 2, 3]
void <T> printList(List<T> list, Class<T> elementType);
```

Covariance

- `ArrayList<Integer>`是`List<Integer>`么？

  是

- `List<Integer>`是`List<Object>`么？

  否。如果`List<Integer>`是`List<Object>`则

  ```java
  List<Object> objList = intList; // 编译器ERROR
  objList.set(0, "hello");
  Integer firstElement = intList.get(0); // 理论上ERROR
  ```

把`List<Integer>`转换成`List<Object>`

- `new ArrayList<Object>(intList)`：占用内存
- `(List<Object>)(List)intList`：类型危险

### 8-13 虚函数表

理解C++的虚函数表可以帮助Java开发者理解面向对象的具体实现、理解语言设计的不同

C++的实例

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gjshoe2ph4j315r0h4wmm.jpg" alt="C++的实例.jpg" width="50%;" />

实例的物理结构

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gjshpsid01j315a0bpjvn.jpg" alt="实例的物理结构.jpg" width="50%;" />

内存中虚函数表的引用结构

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gjshs3x3dmj31dm0h67b5.jpg" alt="内存中虚函数表的引用结构.jpg" width="50%;" />



```c++
void dispatch_work(Employee* p) {
  p->doWork();
}

dispatch_work(&manager);
```

实例的虚函数表指针指向

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gjshuh145ej31dq0h2gr1.jpg" alt="实例的虚函数表指针指向.jpg" width="50%;" />

### 8-14 面向对象-小结

类与对象、接口与实现、继承与封装、不可变对象、泛型、虚函数表

### 8-15 面向对象问题



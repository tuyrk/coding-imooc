# 第6章 程序设计语言基础

- [6-1 程序设计语言基础-归类](#6-1-程序设计语言基础-归类)
- [6-2 程序语言问题](#6-2-程序语言问题)
- [6-3 数据类型、整数和补码](#6-3-数据类型整数和补码)
  - [数据类型](#数据类型)
  - [整数类型](#整数类型)
  - [补码](#补码)
- [6-4 浮点数和定点数简述](#6-4-浮点数和定点数简述)
  - [浮点数](#浮点数)
- [6-5 Java数据类型、拆箱和装箱](#6-5-java数据类型拆箱和装箱)
- [6-6 数据类型问题](#6-6-数据类型问题)

### 6-1 程序设计语言基础-归类

类型检查

- 编译时：C、C++、Java、Go
- 运行时：Python、Perl、JavaScript、Ruby

运行/编译

- 编译为机器代码：C、C++
- 编译为中间代码，在虚拟机运行：Java，C#
- 解释执行：Python、Perl、JavaScript

编程范式(Programming Paradigm)

- 面向过程：C、Visual Basic
- 面向对象：Java、C#、C++、Scala
- 函数式：Haskell、Erlang

### 6-2 程序语言问题



### 6-3 数据类型、整数和补码

#### 数据类型

- boolean、byte、char(2字节)
- short、int、long、float、double
- String、Enum、Array
- Object

#### 整数类型

32位int的取值范围：-2^31 ~ 2^32-1

直观方法：第一位表示正负，取值范围为-(2^31-1) ~ 2^31-1，此时有+0和-0两种方式表示0

#### 补码

补码表示为取反加1。例如-1，0000...1--->取反--->1111...0--->+1--->1111...1

> 1000...0 = -2^31
>
> 1111...1 = -1
>
> 0000...0 = 0
>
> 0111...1 = 2^31-1

补码表示负数的优点：

> 数学：-1 + 1 = 0
>
> 二进制：1111...1 + 0000...1 = 0000...0
>
> 唯一表示0，没有+0、-0
>
> 总共表示2^32个数

### 6-4 浮点数和定点数简述

#### 浮点数

> (+/-) 1.xxx * 2^y^

- 符号位|指数部分|基数部分
- 64位double范围：+/- 10^308^
- 64为double精度：10^15^

浮点数比较

- a == b ？

- Math.abs(a-b) < eps ？

- 使用BigDecimal算钱

  还可以使用分，将金额乘以100得到一个整数的分

### 6-5 Java数据类型、拆箱和装箱

primitive type VS Object

- primitive type：boolean、byte、char、short、int、long、float、double

  值类型

  使用 a == b 判断相等

- Object：Long、Float、String

  引用类型

  使用 a == b 判断是否为同一个Object

  使用 a.equals(b) 或 Objects.equals(a, b) 判断是否相等

Boxing and Unboxing

- Integer a = 2; // Boxing 隐式装箱
- Integer b = new Integer(2); // Boxing 显式装箱
- int v = a.intValue(); // Unboxing

常见问题：

```java
new Integer(2) == 2; // true
new Integer(2) == new Integer(2); // false
Integer.valueOf(100) == Integer.valueOf(100); // true。高速缓冲区概念
Integer.valueOf(1000) == Integer.valueOf(1000); // false
Integer.valueOf(2).intValue() == 2; // true
new Integer(2).equals(new Integer(2)); // true
```

Integer类重写equals()方法。源码如下：

```java
public boolean equals(Object obj) {
  if (obj instanceof Integer) {
    return value == ((Integer)obj).intValue();
  }
  return false;
}
```

### 6-6 数据类型问题



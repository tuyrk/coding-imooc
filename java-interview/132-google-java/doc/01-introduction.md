# 第1章 课程引言

- [1-1 导学](#1-1-导学)
  - [课程安排之基础知识](#课程安排之基础知识)
  - [课程安排之编程能力](#课程安排之编程能力)
- [1-2 校招录取率和在线笔试](#1-2-校招录取率和在线笔试)
- [1-3 从一道谷歌在线笔试开始](#1-3-从一道谷歌在线笔试开始)
  - [Problem B. Beautiful Numbers](#problem-b-beautiful-numbers)
    - [Problem](#problem)
    - [Input](#input)
    - [Output](#output)
    - [Limits](#limits)
    - [Small dataset](#small-dataset)
    - [Large dataset](#large-dataset)
    - [Sample](#sample)

### 1-1 导学

- 基础知识

  需要掌握流行的编程语言和工具，对数据结构、算法、数据库、操作系统原理、计算机体系结构、计算机网络、离散数学等基础学科要点掌握扎实，或者在某一领域特别突出

- 编程能力

  了解目前广泛应用的技术，有好奇心，愿意学习新知识、新技术，并且有很好的方法快速掌握，能够找到合适的场景实际动手操作，并取得一定的成果

#### 课程安排之基础知识

- 计算机基础：操作系统、网络、数据库
- 编程语言基础：数据类型、装箱与拆箱

#### 课程安排之编程能力

- 编码技巧：递归控制、循环控制、边界控制、数据结构、**树的遍历**
- 面向对象思想：类与对象、接口与实现、继承与封装、不可变类型、泛型
- 设计模式：再谈Singleton、变继承关系为组合关系、对象如何创建
- 高级知识点：并行计算、多线程、资源管理
- 面试流程和技巧分享
- Google在线笔试系统演示

### 1-2 校招录取率和在线笔试

赛码：https://acmcoder.com

牛客：https://www.nowcoder.com

Google Code Jam系统：https://codingcompetitions.withgoogle.com/codejam

### 1-3 从一道谷歌在线笔试开始

Round E APAC Test 2017

#### Problem B. Beautiful Numbers

##### Problem
We consider a number to be beautiful if it consists only of the digit 1 repeated one or more times. Not all numbers are beautiful, but we can make any base 10 positive integer beautiful by writing it in another base.

Given an integer N, can you find a base B (with B > 1) to write it in such that all of its digits become 1? If there are multiple bases that satisfy this property, choose the one that maximizes the number of 1 digits.

##### Input
The first line of the input gives the number of test cases, T. T test cases follow. Each test case consists of one line with an integer N.

##### Output
For each test case, output one line containing Case #x: y, where x is the test case number (starting from 1) and y is the base described in the problem statement.

##### Limits
1 ≤ T ≤ 100.

##### Small dataset
3 ≤ N ≤ 1000.

##### Large dataset
3 ≤ N ≤ 10^18.

##### Sample

```
Input   Output
2       Case #1: 2
3       Case #2: 3
13
```

In case #1, the optimal solution is to write 3 as 11 in base 2.

In case #2, the optimal solution is to write 13 as 111 in base 3. Note that we could also write 13 as 11 in base 12, but neither of those representations has as many 1s.


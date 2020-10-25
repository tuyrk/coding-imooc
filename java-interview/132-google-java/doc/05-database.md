# 第5章 数据库

- [5-1 数据库-概述](#5-1-数据库-概述)
- [5-2 JOIN和GROUP BY](#5-2-join和group-by)
  - [JOIN](#join)
  - [GROUP BY](#group-by)
- [5-3 事务和乐观锁](#5-3-事务和乐观锁)
  - [事务](#事务)
  - [乐观锁](#乐观锁)
- [5-4 数据库问题](#5-4-数据库问题)
- [5-5 索引的创建和验证](#5-5-索引的创建和验证)
- [5-6 索引的实现](#5-6-索引的实现)
- [5-7 数据库连接池](#5-7-数据库连接池)
- [5-8 数据库例题](#5-8-数据库例题)

### 5-1 数据库-概述

- 基于关系代数理论
- 缺点：表结构不直观，实现复杂，速度慢
- 优点：健壮性高，社区庞大

### 5-2 JOIN和GROUP BY

```mysql
drop table if exists product;
create table product (
  product_id   int auto_increment primary key comment '商品ID',
  product_name varchar(20) not null comment '商品名称',
  category_id  int         null comment '商品类型',
  price        decimal     null comment '商品价格'
) comment '商品表';
insert into product (product_id, product_name, category_id, price) values (1, 'nike', null, 600);
insert into product (product_id, product_name, category_id, price) values (2, 'addidas', 1, 500);
insert into product (product_id, product_name, category_id, price) values (3, 'porsche', 2, 1000000);
insert into product (product_id, product_name, category_id, price) values (4, 'toyota', 2, 100000);
insert into product (product_id, product_name, category_id, price) values (5, 'apple', 3, 8000);

drop table if exists category;
create table category (
  category_id   int auto_increment primary key comment '商品类型ID',
  category_name varchar(20) not null comment '商品类型名称'
) comment '商品类型表';
insert into category (category_id, category_name) values (1, 'shoe');
insert into category (category_id, category_name) values (2, 'automobile');
insert into category (category_id, category_name) values (3, 'phone');
```

#### JOIN

1. 笛卡尔积

```mysql
select * from product join category;
```

```
+------------+--------------+-------------+---------+-------------+---------------+
| product_id | product_name | category_id | price   | category_id | category_name |
+------------+--------------+-------------+---------+-------------+---------------+
|          1 | nike         |        NULL |     600 |           2 | automobile    |
|          1 | nike         |        NULL |     600 |           1 | shoe          |
|          2 | addidas      |           1 |     500 |           2 | automobile    |
|          2 | addidas      |           1 |     500 |           1 | shoe          |
|          3 | porsche      |           2 | 1000000 |           2 | automobile    |
|          3 | porsche      |           2 | 1000000 |           1 | shoe          |
|          4 | toyota       |           2 |  100000 |           2 | automobile    |
|          4 | toyota       |           2 |  100000 |           1 | shoe          |
+------------+--------------+-------------+---------+-------------+---------------+
```

2. 内连接

```mysql
select * from product p
         join category c on p.category_id = c.category_id;
```

```
+------------+--------------+-------------+---------+-------------+---------------+
| product_id | product_name | category_id | price   | category_id | category_name |
+------------+--------------+-------------+---------+-------------+---------------+
|          2 | addidas      |           1 |     500 |           1 | shoe          |
|          3 | porsche      |           2 | 1000000 |           2 | automobile    |
|          4 | toyota       |           2 |  100000 |           2 | automobile    |
+------------+--------------+-------------+---------+-------------+---------------+
```

3. 左外连接

```mysql
select * from product p
         left join category c on p.category_id = c.category_id;
```

```
+------------+--------------+-------------+---------+-------------+---------------+
| product_id | product_name | category_id | price   | category_id | category_name |
+------------+--------------+-------------+---------+-------------+---------------+
|          1 | nike         |        NULL |     600 |        NULL | NULL          |
|          2 | addidas      |           1 |     500 |           1 | shoe          |
|          3 | porsche      |           2 | 1000000 |           2 | automobile    |
|          4 | toyota       |           2 |  100000 |           2 | automobile    |
+------------+--------------+-------------+---------+-------------+---------------+
```

#### GROUP BY

```mysql
update product set category_id = 1 where product_id = 1;
```

1. 分组计数

```mysql
select p.category_id, category_name, count(*)
from product p
         left join category c on p.category_id = c.category_id
group by p.category_id, category_name;
```

```
+-------------+---------------+----------+
| category_id | category_name | count(*) |
+-------------+---------------+----------+
|           1 | shoe          |        2 |
|           2 | automobile    |        2 |
+-------------+---------------+----------+
```

2. 分组最小值

```mysql
select p.category_id, category_name, min(p.price)
from product p
         left join category c on p.category_id = c.category_id
group by p.category_id, category_name;
```

```
+-------------+---------------+--------------+
| category_id | category_name | min(p.price) |
+-------------+---------------+--------------+
|           1 | shoe          |          500 |
|           2 | automobile    |       100000 |
+-------------+---------------+--------------+
```

3. 分组最小值并返回名称

```mysql
select p.*, cat_min.category_name from product p right join (
  select p.category_id, category_name, min(p.price) min_price
  from product p
  left join category c on p.category_id = c.category_id
  group by p.category_id, category_name ) as cat_min
on p.category_id = cat_min.category_id
where p.price = cat_min.min_price;
```

```
+------------+--------------+-------------+--------+---------------+
| product_id | product_name | category_id | price  | category_name |
+------------+--------------+-------------+--------+---------------+
|          2 | addidas      |           1 |    500 | shoe          |
|          4 | toyota       |           2 | 100000 | automobile    |
+------------+--------------+-------------+--------+---------------+
```

### 5-3 事务和乐观锁

#### 事务

事务的四个特性：ACID

- 原子性(Atomicity)：要么全部执行，要么全部不执行
- 一致性(Consistency)：事务的执行使得数据库从一种正确状态转化为另一种正确状态
- 隔离性(Isolation)：在事务正确提交之前，不允许把该事务对数据的任何改变提供给其他事务
- 持久性(Durability)：事务提交后，其结果永久保存在数据库中

事务的隔离级别：

- read uncommitted：未提交读，读到未提交数据

- read committed：读已提交，也叫不可重复读，两次读取到的数据不一致

- repeatable read：可重复读

- serializable：串行化，读写数据都会锁住整张表，数据操作不会出错，但并发性能极低，开发中很少用到

隔离级别的示例：

  ```mysql
alter table product
  add count int null comment '库存';
update product set count = 50 where product_id = 2;
  ```

```mysql
# client1
begin;
set autocommit = 0;
select count from product where product_id = 2; # 读
# ...此处其他客户端操作修改数据... #
update product set count = 49 where product_id = 2; # 写
commit; # rollback;
```

```mysql
select @@transaction_isolation; # 查询事务隔离级别
set session transaction isolation level repeatable read; # 设置当前会话的事务隔离级别
set session transaction isolation level read committed;
```

  #### 乐观锁

```mysql
select count from product where product_id = 2;
update product set count = 46
where product_id = 2 and count = 47; # 版本保护，条件不满足则会返回影响0行
```

- 读取数据，记录timestamp
- 修改数据
- 检查和提交数据

### 5-4 数据库问题



### 5-5 索引的创建和验证



### 5-6 索引的实现



### 5-7 数据库连接池



### 5-8 数据库例题

下列方法中，________不可以用来程序调优

> A：改善数据访问方式以提升缓存命中率
>
> B：使用多线程的方式提高I/O密集型操作的效率
>
> C：利用数据库连接池代替直接的数据库访问
>
> D：使用迭代代替递归
>
> E：合并多个远程调用批量发送
>
> F：共享冗余数据提高访问效率

答：B

题目解析：B的瓶颈是磁盘的IO效率，并不是CPU
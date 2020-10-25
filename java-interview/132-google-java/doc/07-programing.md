# 第7章 编码技巧

- [7-1 编码技巧-概述](#7-1-编码技巧-概述)
- [7-2 在白板上写程序](#7-2-在白板上写程序)
- [7-3 数学归纳法是编码的依据](#7-3-数学归纳法是编码的依据)
- [7-4 编码技巧-递归书写方法](#7-4-编码技巧-递归书写方法)
- [7-5 递归控制-例题链表创建](#7-5-递归控制-例题链表创建)
- [7-6 递归控制-例题链表反转](#7-6-递归控制-例题链表反转)
- [7-7 递归控制-例题列出所有组合](#7-7-递归控制-例题列出所有组合)
- [7-8 递归开销](#7-8-递归开销)
- [7-9 编码技巧-循环书写方法](#7-9-编码技巧-循环书写方法)
- [7-10 循环控制-例题链表反转非递归](#7-10-循环控制-例题链表反转非递归)
- [7-11 循环控制-例题链表删除结点](#7-11-循环控制-例题链表删除结点)
- [7-12 边界控制-二分查找](#7-12-边界控制-二分查找)
- [7-13 二分查找-设计测试用例和隐藏10年的bug](#7-13-二分查找-设计测试用例和隐藏10年的bug)
- [7-14 数据结构回顾](#7-14-数据结构回顾)
- [7-15 Java集合类型常见问题](#7-15-java集合类型常见问题)
- [7-16 树的遍历](#7-16-树的遍历)
- [7-17 树的遍历-构造后序](#7-17-树的遍历-构造后序)
- [7-18 中序遍历下一个结点-分析](#7-18-中序遍历下一个结点-分析)
- [7-19 中序遍历下一个结点-代码](#7-19-中序遍历下一个结点-代码)
- [7-20 树的遍历-例题](#7-20-树的遍历-例题)
- [7-21 算法复杂度](#7-21-算法复杂度)
  - [O(N^2)](#on2)
  - [O(NlogN)](#onlogn)
  - [O(logN)](#ologn)
  - [算法的组合](#算法的组合)
  - [递归](#递归)
- [7-22 编码技巧-总结](#7-22-编码技巧-总结)

### 7-1 编码技巧-概述

递归控制、循环控制、边界控制、数据结构（树）

### 7-2 在白板上写程序

心理不抵触，先思考后写，不要惧怕修改/重写

### 7-3 数学归纳法是编码的依据

用于证明断言对所有自然数成立

- 证明对于N=1成立
- 证明N>1时：如果对于N-1成立，那么对于N成立

求证：1+2+3+...+n = n(n+1)/2

- 1 = 1*2/2

- 如果1+2+3+...+(n-1) = (n-1)n/2

  那么1+2+3+...+n = 1+2+3+...+(n-1)+n

  => (n-1)n/2+n = (n(n-1)+2n)/2 = n(n+1)/2

```java
int sum(int n) {
  if (n == 1)
    return 1;
  return sum(n-1) + n;
}
```

### 7-4 编码技巧-递归书写方法

如何证明递归函数正确执行？

- 数学归纳法中的数学/自然语言 <==> 程序语言

递归书写方法

- 严格定义递归函数作用，包括参数、返回值、side-effect
- 先一般，后特殊
- 每次调用必须缩小问题规模
- 每次问题规模缩小程度必须为1

### 7-5 递归控制-例题链表创建

链表容易理解、代码难写

```java
@Data
public class Node {
  private final int value;
  private Node next;

  public static void printLinkedList(Node head) {
    while (head != null) {
      System.out.print(head.getValue() + " ");
      head = head.getNext();
    }
    System.out.println();
  }
}
```

```java
public class LinkedListCreator {
  /**
   * Creates a linked list.
   * @param data data the data to create the list
   * @return head of the linked list. The returned linked list ends with last node with getNext() == null.
   */
  public static Node createLinkedList(List<Integer> data) {
    if (data.isEmpty()) {
      return null;
    }

    Node firstNode = new Node(data.get(0));
    firstNode.setNext(createLinkedList(data.subList(1, data.size())));
    return firstNode;
  }
}
```

```java
Node.printLinkedList(createLinkedList(new ArrayList<>()));
Node.printLinkedList(createLinkedList(Arrays.asList(1)));
Node.printLinkedList(createLinkedList(Arrays.asList(1, 2, 3, 4, 5)));
```

### 7-6 递归控制-例题链表反转

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gizpssgn5nj319a0i2n1z.jpg" alt="链表反转-递归.jpg" width="45%;" />

```java
public class LinkedListReverser {
  /**
   * Reverses a linked list.
   * @param head head the linked list to reverse
   * @return head of the reversed linked list
   */
  public static Node reverseLinkedList(Node head) {
    // size == 0 or size == 1
    if (head == null || head.getNext() == null) {
      return head;
    }

    Node newHead = reverseLinkedList(head.getNext());
    head.getNext().setNext(head);
    head.setNext(null);
    return newHead;
  }
}
```

```java
Node.printLinkedList(reverseLinkedList(LinkedListCreator.createLinkedList(new ArrayList<>())));
Node.printLinkedList(reverseLinkedList(LinkedListCreator.createLinkedList(Arrays.asList(1))));
Node.printLinkedList(reverseLinkedList(LinkedListCreator.createLinkedList(Arrays.asList(1, 2, 3, 4, 5))));
```

### 7-7 递归控制-例题列出所有组合

要点：

1. 多个参数的初始值
2. side-effect的维护

```java
public class Combinations {
  /**
   * Generates all combinations and output them, selecting n elements from data.
   * @param selected 已选择的数组
   * @param data     数组
   * @param n        选择个数
   */
  public static void combinations(List<Integer> selected, List<Integer> data, int n) {
    // initial value for recursion
    // how to select elements
    // how to output
    
    if (n < 0) { return; }
    if (n == 0) {
      // output all selected elements + empty list
      selected.forEach(e -> System.out.print(e + " ")); System.out.println();
      return;
    }
    if (data.isEmpty()) { return; }
    if (data.size() < n) { return; }

    selected.add(data.get(0)); // select element 0
    combinations(selected, data.subList(1, data.size()), n - 1);
    
    selected.remove(selected.size() - 1); // un-select element 0
    combinations(selected, data.subList(1, data.size()), n);
  }
}
```

```java
combinations(new ArrayList<>(), new ArrayList<>(), 2);
combinations(new ArrayList<>(), new ArrayList<>(), 0);
combinations(new ArrayList<>(), Arrays.asList(1, 2, 3, 4, 5), 0);
combinations(new ArrayList<>(), Arrays.asList(1, 2, 3, 4, 5), 1);
combinations(new ArrayList<>(), Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 4);
```

### 7-8 递归开销

递归函数调用栈的开销，如果数据规模过大会有Stack Overflow错误

不要尝试将递归改为非递归，因为一般化的方法仍然需要使用栈、代码复杂、不能根本解决问题

### 7-9 编码技巧-循环书写方法

循环不变式（loop invariant）

> 是一句断言定义各变量所满足的条件

``` java
var a, b;
while () {
  // a, b必须满足某种条件
}
```

循环书写方法

- 定义循环不变式，并在循环体每次结束后保持循环不变式
- 先一般，后特殊
- 每次必须向前推进循环不变式中涉及的变量值
- 每次推进的规模必须为1

### 7-10 循环控制-例题链表反转非递归

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gizpor55pcj31e00fkn1m.jpg" alt="链表反转-非递归1.jpg" width="45%;" />

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gizpq777fyj31dv0ku7ag.jpg" alt="链表反转-非递归2.jpg" width="45%;" />

- newHead指向反转成功的链表
- currentHead指向还没有反转的链表

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gizpqwse1ej31ea0af427.jpg" alt="链表反转-非递归3.jpg" width="50%;" />

```java
public class LinkedListReverser {
  public static <T> Node<T> reverseLinkedList(Node<T> head) {
    Node<T> newHead = null;
    Node<T> curHead = head;
    // Loop invariant:
    // newHead points to the linked list already reversed.
    // curHead points to the linked list not yet reversed.

    // Loop invariant holds.
    while (curHead != null) {
      // Loop invariant holds.
      Node<T> next = curHead.getNext();
      curHead.setNext(newHead);
      newHead = curHead;
      curHead = next;
      // Loop invariant holds.
    }
    // Loop invariant holds.
    return newHead;
  }
}
```

```java
// #LinkedListCreator.java#
public static Node createLargeLinkedList(int size) {
  Node prev = null;
  Node head = null;

  for (int i = 1; i <= size; i++) {
    Node node = new Node<>(i);
    if (prev != null) {
      prev.setNext(node);
    } else {
      head = node;
    }
    prev = node;
  }
  return head;
}
```

```java
Node.printLinkedList(reverseLinkedList(LinkedListCreator.createLinkedList(new ArrayList<>())));
Node.printLinkedList(reverseLinkedList(LinkedListCreator.createLinkedList(Arrays.asList(1))));
Node.printLinkedList(reverseLinkedList(LinkedListCreator.createLinkedList(Arrays.asList(1, 2, 3, 4, 5))));
reverseLinkedList(LinkedListCreator.createLargeLinkedList(1000000));
System.out.println("done");
```

### 7-11 循环控制-例题链表删除结点

> 删除链表中给定值的节点

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gizsfn1qsrj31670fp77h.jpg" alt="链表删除结点1.jpg" width="45%;" />

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gizshm5mzhj31680ly79b.jpg" alt="链表删除结点2.jpg" width="45%;" />

头节点没有previous怎们办？

1. 特殊处理，单独写在循环外
2. 增加虚拟头节点

```java
public static <T> Node<T> deleteIfEquals(Node<T> head, int value) {
  // 注意此处while的作用：如果开头有多个与value相同的值则循环删除
  while (head != null && Objects.equals(head.getValue(), value)) {
    head = head.getNext();
  }
  if (head == null) {
    return null;
  }

  Node<T> prev = head;
  // Loop invariant: list nodes from head up to prev has bean processed. 
  // (Nodes with values equal to value are deleted.)
  while (prev.getNext() != null) {
    if (Objects.equals(prev.getNext().getValue(), value)) {
      // delete it.
      prev.setNext(prev.getNext().getNext());
    } else {
      prev = prev.getNext();
    }
  }

  return head;
}
```

```java
Node.printLinkedList(deleteIfEquals(LinkedListCreator.createLinkedList(Arrays.asList(1, 2, 3, 2, 5)), 2));
Node.printLinkedList(deleteIfEquals(LinkedListCreator.createLinkedList(Arrays.asList(1, 2, 3, 2, 2)), 2));
Node.printLinkedList(deleteIfEquals(LinkedListCreator.createLinkedList(Arrays.asList(1, 2, 3, 2, 2)), 1));
Node.printLinkedList(deleteIfEquals(LinkedListCreator.createLinkedList(Arrays.asList(2, 2, 3, 2, 2)), 2));
Node.printLinkedList(deleteIfEquals(LinkedListCreator.createLinkedList(Arrays.asList(2, 2, 2, 2, 2)), 2));
Node.printLinkedList(deleteIfEquals(LinkedListCreator.createLinkedList(Arrays.asList(2)), 2));
Node.printLinkedList(deleteIfEquals(LinkedListCreator.createLinkedList(Arrays.asList(2)), 1));
Node.printLinkedList(deleteIfEquals(LinkedListCreator.createLinkedList(new ArrayList<Integer>()), 2));
```

### 7-12 边界控制-二分查找

二分查找：在有序数组中查找元素k，返回k所在下标

例：binarySearch([1, 2, 10, 15, 100], 15) == 3

二分查找思路：

- 规定要查找的值k可能在的数组arr内下标区间a, b
- 计算区间a, b的中间点m
- 若k < arr[m]，将区间缩小为a, m，继续二分查找
- 若k > arr[m]，将区间缩小为m, b，继续二分查找
- 若k == arr[m]，则找到元素位于位置m

```java
public class BinarySearch {
  /**
   * Searches element k in a sorted array
   * @param arr a sorted array
   * @param k the element to search
   * @return index in arr where k is. -1 if not found.
   */
  public static int binarySearch(int[] arr, int k) {
    int a = 0;
    int b = arr.length; // 此处b为数组最后一个元素的位置+1，形成区间[a, b)
    // Loop invariant: [a, b) is a valid range. (a <= b)
    // k may only be within range [a, b)
    while (a < b) {
      int m = (a + b) / 2;
      if (k < arr[m]) {
        b = m; // 此处的b也保持是数组最后一个元素的位置+1
      } else if (k > arr[m]) {
        a = m + 1;
      } else {
        return m;
      }
    }
    return -1;
  }
}
```

区间定为左开右闭[a, b)的好处：

- [a, b) + [b, c) == [a, c)
- b - a == len([a, b))
- [a, a) ==> empty range

int m = (a + b) / 2;

- b = a ==> m = a and m = b

- b = a+1 ==> m = a

- b = a+2 ==> m = a+1

### 7-13 二分查找-设计测试用例和隐藏10年的bug

```java
System.out.println(binarySearch(new int[]{1, 2, 10, 15, 100}, 15)); // 3
System.out.println(binarySearch(new int[]{1, 2, 10, 15, 100}, -2)); // -1
System.out.println(binarySearch(new int[]{1, 2, 10, 15, 100}, 101)); // -1
System.out.println(binarySearch(new int[]{1, 2, 10, 15, 100}, 13)); // -1
System.out.println("====================");
System.out.println(binarySearch(new int[]{}, 13)); // -1
System.out.println(binarySearch(new int[]{12}, 13)); // -1
System.out.println(binarySearch(new int[]{13}, 13)); // 0
System.out.println("====================");
System.out.println(binarySearch(new int[]{12, 13}, 12)); // 0
System.out.println(binarySearch(new int[]{12, 13}, 13)); // 1
```

注意：`int m = (a + b) / 2;`可能会溢出，使用`int m = a + (b - a) / 2;`解决。

另`int m = a / 2 + b / 2;`如果a和b都是奇数则计算结果将会比正确结果少1

### 7-14 数据结构回顾

- 树的遍历
- 算法复杂性分析

**列表**：数组、链表、队列，栈

**树**：二叉树、搜索树、堆/优先队列

栈/队列/优先队列

```java
push(1);push(3);push(2);pop();pop();pop();
```

- 栈：231
- 队列：132
- 优先队列：123

`Map<K, V>` / `Set<K>`

- HashMap / HashSet ==> K.hashCode()
- TreeMap / TreeSet ==> K implements Comparable

【单选】HashSet子类依靠(  C  )方法区分重复元素？

> A：toString()，equals()
>
> B：clone()，equals()
>
> C：hashCode()，equals()
>
> D：getClass()，clone()

**图**：无向图、有向图、有向无环图

**图的算法**：深度优先算法DFS、广度优先算法BFS、拓扑排序、最短路径/最小生成树

### 7-15 Java集合类型常见问题



### 7-16 树的遍历

二叉树的遍历：前序遍历、中序遍历、后序遍历、层次遍历

前序遍历：先遍历树根，然后前序遍历左子树，再前序遍历右子树

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gj7oqs5sroj30px0lywh1.jpg" alt="二叉树的遍历.jpg" width="25%;" />

- 前序遍历：ABDEGCF
- 中序遍历：DBGEACF
- 后序遍历：DGEBFCA

树节点

```java
@Data
public class TreeNode {
  private final char value;
  private TreeNode left;
  private TreeNode right;
}
```

树的创建

```java
public class TreeCreator {
  public static TreeNode createSampleTree() {
    TreeNode root = new TreeNode('A');
    root.setLeft(new TreeNode('B'));
    root.getLeft().setLeft(new TreeNode('D'));
    root.getLeft().setRight(new TreeNode('E'));
    root.getLeft().getRight().setLeft(new TreeNode('G'));
    root.setRight(new TreeNode('C'));
    root.getRight().setRight(new TreeNode('F'));
    return root;
  }
}
```

树的遍历

```java
public class TreeTraversal {
  // 前序遍历
  public static void preOrder(TreeNode root) {
    if (root == null) { return; }
    System.out.print(root.getValue());
    preOrder(root.getLeft());
    preOrder(root.getRight());
  }

  // 中序遍历
  public static void inOrder(TreeNode root) {
    if (root == null) { return; }
    inOrder(root.getLeft());
    System.out.print(root.getValue());
    inOrder(root.getRight());
  }

  // 后序遍历
  public static void postOrder(TreeNode root) {
    if (root == null) { return; }
    postOrder(root.getLeft());
    postOrder(root.getRight());
    System.out.print(root.getValue());
  }
}
```

```java
TreeNode sampleTree = TreeCreator.createSampleTree();
preOrder(sampleTree); System.out.println();
inOrder(sampleTree); System.out.println();
postOrder(sampleTree);
```

### 7-17 树的遍历-构造后序

例一：根据前序中序遍历构造二叉树

> 前序遍历：ABDEGCF
>
> 中序遍历：DBGEACF
>
> 后序遍历：？

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gj7pjymyauj314o0h0jv0.jpg" alt="根据前序中序遍历构造二叉树.jpg" width="45%;" />

根据前序遍历和中序遍历创建二叉树

```java
public static TreeNode createTree(String preOrder, String inOrder) {
  if (preOrder.isEmpty()) { return null; }

  char rootValue = preOrder.charAt(0);
  int rootIndex = inOrder.indexOf(rootValue);

  TreeNode root = new TreeNode(rootValue);
  root.setLeft(createTree(preOrder.substring(1, 1 + rootIndex), inOrder.substring(0, rootIndex)));
  root.setRight(createTree(preOrder.substring(1 + rootIndex), inOrder.substring(1 + rootIndex)));
  return root;
}
```

```java
postOrder(TreeCreator.createTree("ABDEGCF", "DBGEACF")); System.out.println();
postOrder(TreeCreator.createTree("", "")); System.out.println();
postOrder(TreeCreator.createTree("A", "A")); System.out.println();
postOrder(TreeCreator.createTree("AB", "BA"));
```

通过前序遍历和中序遍历得到后序遍历的结果

```java
public static String postOrder(String preOrder, String inOrder) {
  if (preOrder.isEmpty()) { return ""; }

  char rootValue = preOrder.charAt(0);
  int rootIndex = inOrder.indexOf(rootValue);

  return postOrder(preOrder.substring(1, 1 + rootIndex), inOrder.substring(0, rootIndex))
    + postOrder(preOrder.substring(1 + rootIndex), inOrder.substring(1 + rootIndex))
    + rootValue;
}
```

```java
System.out.println(postOrder("ABDEGCF", "DBGEACF"));
System.out.println(postOrder("", ""));
System.out.println(postOrder("A", "A"));
System.out.println(postOrder("AB", "BA"));
```

**总结**：

- 思考算法时：只要减小问题的规模即可
- 初始值的确定：根据递归函数定义

**例题**：

1. 已知一个二叉树的前序遍历的结果是（ACDEFHGB），中序遍历结果是（DECAHFBG），请问后序遍历结果是（EDCHBGFA）
2. 已知一个二叉树的前序遍历的结果是（ABDGCEFH），中序遍历结果是（DGBAECFH），请问后序遍历结果是（GDBEHFCA）

**练习**：根据前序遍历和后序遍历构造二叉树

> 前序遍历：ABDEGCF
>
> 后序遍历：DGEBFCA
>
> BDEGCF
>
> DGEBFC

问：构造的二叉树唯一么？不唯一的话有多少个？能够输出全部可能的中序遍历么？

### 7-18 中序遍历下一个结点-分析

例题二：寻找中序遍历时的下一个节点

> 例中序遍历DBGEACF，则Next(B) ==> G、Next(A) ==> C、……

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gj7yllvlf8j30ri0loadx.jpg" alt="中序遍历轨迹.jpg" width="25%;" />

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gj7yy8woscj30pp0lotb6.jpg" alt="搜索树.jpg" width="25%;" />

**搜索树的中序遍历结果是一个有序的序列**

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gj7ynynmczj30ci0hsjsi.jpg" alt="中序遍历下一个节点的可能性.jpg" width="15%;" />

```
L：无关
R != NULL
  ==>返回右子树第一个节点
否则 P != NULL
  N是P的左子树
    ==>返回P
  否则
    ==>一直往父亲节点走，直到是某个节点左子树
```

### 7-19 中序遍历下一个结点-代码

树节点

```java
@Data
public class TreeNode {
  private final char value;
  private TreeNode left;
  private TreeNode right;
  private TreeNode parent;

  public void setLeft(TreeNode left) {
    this.left = left;
    if (this.left != null) {
      this.left.setParent(this);
    }
  }

  public void setRight(TreeNode right) {
    this.right = right;
    if (this.right != null) {
      this.right.setParent(this);
    }
  }

  private void setParent(TreeNode parent) {
    this.parent = parent;
  }
}
```

```java
public class InOrder {
  // 中序遍历下一个结点
  public static TreeNode next(TreeNode node) {
    if (node == null) { return null; }

    if (node.getRight() != null) {
      return first(node.getRight());
    } else {
      while (node.getParent() != null
             && node.getParent().getLeft() != node) {
        node = node.getParent();
      }
    }
    // node.getParent() == null || node is left child of its parent
    return node.getParent();
  }

  // 获取中序遍历的第一个节点
  public static TreeNode first(TreeNode node) {
    if (node == null) { return null; }
    TreeNode curNode = node;
    while (curNode.getLeft() != null) {
      curNode = curNode.getLeft();
    }
    return curNode;
  }

  // 中序遍历树结构
  public static void traverse(TreeNode root) {
    for (TreeNode node = InOrder.first(root);
         node != null;
         node = InOrder.next(node)) {
      System.out.print(node.getValue());
    }
    System.out.println();
  }
}
```

```java
traverse(TreeCreator.createSampleTree());
traverse(TreeCreator.createTree("", ""));
traverse(TreeCreator.createTree("A", "A"));
traverse(TreeCreator.createTree("AB", "BA"));
traverse(TreeCreator.createTree("ABCD", "DCBA"));
traverse(TreeCreator.createTree("ABCD", "ABCD"));
```

总结

- 分情况讨论：左子树、右子树、父亲节点
- 注意null指针，当前节点是否为null
- 使用private函数来维护复杂数据结构

### 7-20 树的遍历-例题

【单选】以下序列中不可能是一颗二叉查找树的后序遍历结构的是：

> A：1,2,3,4,5
>
> B：3,5,1,4,2
>
> C：1,2,5,4,3
>
> D：5,4,3,2,1

答：B

题目解析：A树全为左子节点，二叉查找树特点：左子节点<父节点<右子节点，后序遍历顺序：左子节点-右子节点-父节点，D树全为右子节点

### 7-21 算法复杂度

算法复杂度：最坏情况下程序运行用时

> O(N!), O(2^N), O(N^2), O(NlogN), O(N), O(logN)……

假设要求某程序的算法复杂度在10^8秒级，则N最大能够取值：

| O(N!) | O(2^N) | O(N^2) | O(NlogN) | O(N) | O(logN) |
| ----- | ------ | ------ | -------- | ---- | ------- |
| 10    | 30     | 10000  | 10^7     | 10^8 | ∞       |

#### O(N^2)

> 插入排序，选择排序

```java
for (int i = 0; i < n; i++) {
  for (int j = i; j < n; j++) {
    // ......
  }
}
```

#### O(NlogN)

> 归并排序，快速排序(平均)

快速排序是选择某个数作为基准，将比其小的数都放入左边，比其大的数都放入右边，然后缩小问题规模递归。在选择基准数字的时候可能每次都选择最小或者最大的数，就会出现最坏情况时间复杂度为O(N^2)

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gja9e2rsm8j30sw0fjdif.jpg" alt="O(NlogN).jpg" width="30%;" />

#### O(logN)

> 二分查找（需为有序序列）

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gja9g4o52gj30nm0d075c.jpg" alt="O(logN).jpg" width="30%;" />

#### 算法的组合

例：区间合并，给定一系列区间，合并他们

> 输入[1, 3]、[4, 7]、[2, 6]、[9, 10]、[8, 9]
>
> 输出[1, 7]、[8, 10]

- 线段树

- 先根据区间左端点排序，再扫描端点记录最大右端点

  排序O(NlogN)，扫描已排序的列表O(N)，总复杂度O(NlogN)

#### 递归

递归算法的时间复杂度判断较为困难，可根据以下条件进行确定

1. 每个节点都访问一次吗？
2. 输出多少东西？
3. 每个节点访问的时间是常数吗？

### 7-22 编码技巧-总结

- 数学归纳法
- 递归控制
- 循环控制：循环不变式
- 边界控制：初始值，特殊值，NULL，空串。例二分查找
- 数据结构：树、树的遍历及算法
- 算法复杂度


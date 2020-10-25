# 第11章 谷歌在线笔试题解

- [11-1 解小数据集](#11-1-解小数据集)
- [11-2 估算算法时间](#11-2-估算算法时间)
- [11-3 解大数据集（上）](#11-3-解大数据集上)
- [11-4 解大数据集（下）](#11-4-解大数据集下)
- [11-5 运气和异常](#11-5-运气和异常)

### 11-1 解小数据集

[Beautiful Numbers](./01-introduction.md#problem-b-beautiful-numbers)

1，11，111……是beautiful的

```
3 ==> 2进制 ==> 11
13 ==> 3进制 ==> 111 ✔️
13 ==> 12进制 ==> 11 ❌
```

13 ==> 3进制 ==> 111

```
1*3*3 + 1*3 + 1 = 13
13%3 = 1, 13/3 == 4
4%3 = 1, 4/3 = 1
1%3 = 1, 1/3 = 0
```

暴力计算：

```java
public class BeautifulNumber {
  public static long beautiful(long num) {
    for (int radix = 2; radix < num; radix++) {
      if (isBeautiful(num, radix)) {
        return radix;
      }
    }
    throw new IllegalStateException("Should not reach here.");
  }

  private static boolean isBeautiful(long num, int radix) {
    while (num > 0) {
      if (num % radix != 1) {
        return false;
      }
      num /= radix;
    }
    return true;
  }
}
```

```java
Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
int cases = in.nextInt();
for (int i = 1; i <= cases; i++) {
  long num = in.nextLong();
  System.out.printf("Case #%d: %d\n", i, BeautifulNumber.beautiful(num));
}
```

### 11-2 估算算法时间

时间复杂度：O(nlogn)

10^8次计算约为1秒，即秒级

### 11-3 解大数据集（上）

N ==> r进制 ==> 111……1(k个)

> 假设N能转换成k个1组成的Beautiful Number
>
> 那么这个Beautiful Number是几进制？r = ?

```
N = r^(k-1) + r^(k-2) + …… + r + 1
N = (1 - r^k) / (1 - r)
k = 64, 63, ……, 2
```

```java
public class BeautifulNumberLarge {
  public static long beautiful(long n) {
    for (int bits = 64; bits >= 2; bits--) {
      long radix = getRadix(n, bits);
      if (radix != -1) {
        return radix;
      }
    }
    throw new IllegalStateException("Should not reach here.");
  }

  /**
   * Gets radix so that n is 111...1 (bits 1 in total) in that radix.
   * @return the radix. -1 if there's no such radix.
   */
  private static long getRadix(long n, int bits) {
    long minRadix = 2;
    long maxRadix = n;
    while (minRadix < maxRadix) {
      long m = minRadix + (maxRadix - minRadix) / 2;
      long t = convert(m, bits);
      if (t == n) {
        return m;
      } else if (t < n) {
        minRadix = m + 1;
      } else {
        maxRadix = m;
      }
    }
    return -1;
  }

  /**
   * Returns the value of 111...1 (bits 1 in total) in radix.
   */
  private static long convert(long radix, int bits) {
    long component = 1;
    long sum = 0;
    for (int i = 0; i < bits; i++) {
      if (Long.MAX_VALUE - sum < component) {
        sum = Long.MAX_VALUE;
      } else {
        sum += component;
      }

      if (Long.MAX_VALUE / component < radix) {
        component = Long.MAX_VALUE;
      } else {
        component *= radix;
      }
    }
    return sum;
  }
}
```

### 11-4 解大数据集（下）

```java
Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
int cases = in.nextInt();
for (int i = 1; i <= cases; i++) {
  long num = in.nextLong();
  System.out.printf("Case #%d: %d %d\n", i, BeautifulNumber.beautiful(num), BeautifulNumberLarge.beautiful(num));
}
```

时间复杂度：`O(logN*logN*logN)`，`64*64*64`

### 11-5 运气和异常

有意料之外的情况尽量使用异常将其抛出

```java
throw new IllegalStateException("Should not reach here.");
```


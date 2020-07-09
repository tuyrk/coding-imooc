# 第7章 系统可用性测试【完善系统少不了测试】

## 7-1 可用性测试前的准备工作

```yaml
zuul:
  prefix: /tuyrk
  routes:
    course:
      path: /homepage-course/**
      serviceId: eureka-client-homepage-course
      strip-prefix: false
    user:
      path: /homepage-user/**
      serviceId: eureka-client-homepage-user
      strip-prefix: false
```

## 7-2 测试对外服务接口的可用性

- 课程服务

  ```
  GET http://127.0.0.1:7001/homepage-course/get/course?id=1
  ```

  ```
  GET http://127.0.0.1:9000/tuyrk/homepage-course/get/course?id=1
  ```

  ```
  POST http://192.168.0.109:9000/tuyrk/homepage-course/get/courses
  {
    "ids": [1, 2]
  }
  ```

- 用户服务

  ```
  POST http://192.168.0.109:9000/tuyrk/homepage-user/create/user
  {
    "username": "wm",
    "email": "wm@qq.com"
  }
  ```

  ```
  GET http://192.168.0.109:9000/tuyrk/homepage-user/get/user?id=1
  ```

  ```
  GET http://192.168.0.109:9000/tuyrk/homepage-user/get/user/course?id=1
  ```

  






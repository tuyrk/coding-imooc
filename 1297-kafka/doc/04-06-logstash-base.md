# 4-6 附：logstash基础语法与使用

## logstash基础语法与使用

### 1. logstash安装：

1. Mac安装

   ```shell
   brew install logstash
   ```

2. conf下配置文件说明

   > logstash配置：config/logstash.yml
   >
   > JVM虚拟机配置：config/jvm.options
   >
   > 日志配置：log4j2.properties
   >
   > 启动配置：config/startup.options

   - logstash配置说明

     ```shell
     # config/logstash.yml
     --path.config #  或 –f。logstash启动时使用的配置文件
     --configtest # 或 –t。测试 Logstash 读取到的配置文件语法是否能正常解析
     --log或-l：# 日志输出存储位置
     --pipeline.workers # 或 –w。运行 filter 和 output 的 pipeline 线程数量。默认是 CPU 核数。增加workers工作线程数可以有效提升logstash性能
     --pipeline.batch.size # 或 –b。每个 Logstash pipeline 线程，在执行具体的 filter 和 output 函数之前，最多能累积的日志条数
     --pipeline.batch.delay # 或 –u。每个 Logstash pipeline 线程，在打包批量日志的时候，最多等待几毫秒。
     --verbose：# 输出调试日志
     --debug：# 输出更多的调试日志
     ```

   - 启动配置，比如启动时的java位置、LS的home等

     ```shell
     # config/startup.options
     ```

3. 启动logstash

   ```shell
   logstash -f script/logstash-script.conf
   ```

   

```shell
## 数据收集目录：/usr/local/logstash-6.6.0/data
## 插件目录：/usr/local/logstash-6.6.0/vendor/bundle/jruby/1.9/gems
## 查看插件命令：
/usr/local/logstash-6.6.0/bin/logstash-plugin list
## 更新插件命令：
/usr/local/logstash-6.6.0/bin/logstash-plugin update logstash-xxxx-xxxxx
## 安装插件命令：
/usr/local/logstash-6.6.0/bin/logstash-plugin install logstash-xxxx-xxxxx
## 插件地址： https://github.com/logstash-plugins
```

### 2. logstash语法与基本使用

1. Logstash设计了自己的DSL包括有区域，注释，数据类型(布尔值，字符串，数值，数组，哈希)，条件判断字段引用等。

2. Logstash用{}来定义区域。区域内可以包括插件区域定义，你可以在一个区域内定义多个插件。插件区域内则可以定义键值对设置。

3. 格式、语法、使用方式：

   ```shell
   # 注释.
   input {
     ...
   }
    
   filter {
     ...
   }
    
   output {
     ...
   }
   ```

   ```shell
   ## 两个input设置：
   input {
     file {
       path => "/var/log/messages"
       type => "syslog"
     }
     file {
       path => "/var/log/apache/access.log"
       type => "apache"
     }
   }
   ```

   ```shell
   ## 数据类型：
   ## bool类型
   debug => true
   ## string类型
   host => "hostname"
   ## number类型
   port => 6789
   ## array or list类型
   path => ["/var/log/message","/var/log/*.log"]
   ## hash类型
   match => {
       "field1" => "value1"
       "field2" => "value2"
   }
   ## codec类型
   codec => "json"
   
   ##字段引用方式：
   {
       "agent":  "Mozilla/5.0  (compatible;  MSIE  9.0)",
       "ip":  "192.168.24.44",
       "request":  "/index.html"
       "response":  {
           "status":  200,
           "bytes":  52353
       },
       "ua":  {
           "os":  "Windows  7"
       }
   }
   ##获取字段值：
   [response][status]
   [ua][os]
   ```

   ```shell
   ## 条件判断condition：
   if EXPRESSION {
     ...
   } else if EXPRESSION {
     ...
   } else {
     ...
   }
   
   ==(等于), !=(不等于), <(小于), >(大于), <=(小于等于), >=(大于等于), =~(匹配正则), !~（不匹配正则）
   in(包含), not in(不包含), and(与), or(或), nand(非与), xor(非或)
   ()(复合表达式), !()(对复合表达式结果取反)
   ```

   ```shell
   ## 使用环境变量（缺失报错）:
   input { 
   	tcp { 
   		port => "${TCP_PORT}" 
   	} 
   }
   ## 使用环境变量（缺失使用默认值）：
   input { 
   	tcp { 
   		port => "${TCP_PORT:54321}" 
   	} 
   }
   ```

4. logstash例子：

   ```shell
   ## input 从标准输入流：
   input { stdin { } }
   
   ## 输入数据之后 如何进行处理：
   filter {
     ## grok：解析元数据插件,这里从input输入进来的所有数据默认都会存放到 "message" 字段中
     ## grok提供很多正则表达式，地址为：http://grokdebug.herokuapp.com/patterns
     ## 比如：%{COMBINEDAPACHELOG} 表示其中一种正则表达式 Apache的表达式
     grok {
       match => { "message" => "%{COMBINEDAPACHELOG}" }
     }
     ## date：日期格式化
     date {
       match => [ "timestamp" , "dd/MMM/yyyy:HH:mm:ss Z" ]
     }
   }
   
   ## output 从标准输出流：
   output {
     elasticsearch { hosts => ["192.168.11.35:9200"] }
     stdout { codec => rubydebug }
   }
   ```
   
5. file插件使用：

   ```shell
   ## file插件
   input {
       file {
           path => ["/var/log/*.log", "/var/log/message"]
           type => "system"
           start_position => "beginning"
       }
   }
   ## 其他参数：
   discover_interval ## 表示每隔多久检测一下文件，默认15秒
   exclude ## 表示排除那些文件
   close_older ## 文件超过多长时间没有更新，就关闭监听 默认3600s
   ignore_older ## 每次检查文件列表 如果有一个文件 最后修改时间超过这个值 那么就忽略文件 86400s
   sincedb_path ## sincedb保存文件的位置，默认存在home下（/dev/null）
   sincedb_write_interval ## 每隔多久去记录一次 默认15秒
   stat_interval ## 每隔多久查询一次文件状态 默认1秒
   start_position ## 从头开始读取或者从结尾开始读取
   ```

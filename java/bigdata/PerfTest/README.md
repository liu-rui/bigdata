# 概述
1. 对比各个数据分析的性能
2. 对比的数据源（参照项目DataGenerator）
3. 对比的方案
    1. 使用hive + hdfs存储数据
    2. 使用hive + hbase存储数据
    3. 使用impala + hive + hdfs存储数据
    4. 使用impala + hive + hbase存储数据
    5. 使用phoenix + hbase存储数据
    6. 使用kylin
4. 测试的语句
    1. 统计订单表个数
    ```sql
        select count(0) from orderlog;
    ```
    2. 查询特定用户某天购买的商品列表
    ```sql
       
    ```
5. 部署
   1. 使用hive + hdfs存储数据
   * 表定义
```text
create table if not exists customer(
customer_id int,
name string,
sex int
)ROW FORMAT DELIMITED FIELDS TERMINATED BY ','  LINES TERMINATED BY '\n'
STORED as TEXTFILE;

create table if not exists item(
item_id int,
name string
)ROW FORMAT DELIMITED FIELDS TERMINATED BY ','  LINES TERMINATED BY '\n'
STORED as TEXTFILE;

create table if not exists orderLog(
order_id int,
customer_id int,
item_id int,
price int,
order_date TIMESTAMP
)ROW FORMAT DELIMITED FIELDS TERMINATED BY ','  LINES TERMINATED BY '\n'
STORED as TEXTFILE;

```
    * 导入脚本
    
```bash
load data local inpath '/media/liurui/data/code/bigdata/java/bigdata/customer.csv' into table customer;
load data local inpath '/media/liurui/data/code/bigdata/java/bigdata/item.csv' into table item;
load data local inpath '/media/liurui/data/code/bigdata/java/bigdata/order1.csv' into table orderLog;
```
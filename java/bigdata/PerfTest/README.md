# 概述
1. 对比各个数据分析的性能
2. 对比的数据源（参照项目DataGenerator）
3. 对比的方案
    1. 使用hive + hdfs存储数据           (不可取)
    2. 使用hive + hbase存储数据　        (不可取)  
    3. 使用impala + hive + hdfs存储数据  (30s)
    4. 使用impala + hive + hbase存储数据　(不可取)
    5. 使用phoenix + hbase存储数据
    6. 使用kylin
4. 测试的语句
    1. 统计订单表个数
    ```sql
     select count(0) from hbase_orderlog;
    ```
    2. 查询特定的订单
    ```sql
    select * from  orderlog where order_id=156456;
    ```
    
    3. 查询特定用户某天购买的商品和消费列表
    ```sql
     select c.name,a.price from orderlog a inner join customer b on a.customer_id=b.customer_id inner join item c on a.item_id = c.item_id where a.order_date >= '2017-01-01' and a.order_date < '2017-01-02' and a.customer_id=45612;
    ```   
    4. 查询某天女性用户消费总金额数
    ```sql
    select sum(a.price) from  orderlog a inner join customer b on a.customer_id=b.customer_id where a.order_date >= '2017-01-20' and a.order_date < '2017-01-21' and b.sex=1;    
    ```
    5. 查询某天商品的消费额
    ```sql
   select a.name,b.num,b.price from item a inner join(  
     select item_id,count(item_id) num,sum(price) price from orderlog where order_date >= '2017-01-20' and order_date < '2017-01-21' group by item_id) b
   on a.item_id=b.item_id limit 10;  



 
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
            load data local inpath '/media/liurui/data/data/customer.csv' into table customer;
            load data local inpath '/media/liurui/data/data/item.csv' into table item;
            load data local inpath '/media/liurui/data/data//order.csv' into table orderLog;
  
  
            load data  inpath 'data/customer.csv' into table customer;
            load data local inpath 'data/item.csv' into table item;
            load data local inpath 'data/order.csv' into table orderLog;
            ```

    2. 使用hive + hbase存储数据
        * hbase表定义
    
            ```
            create 'customer','info'
            create 'item','info'
            create 'orderlog','info'
            ```
        * hive表定义
         ```text
           CREATE EXTERNAL TABLE hbase_customer (
            customer_id string,
            name string,
            sex int)
           STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
           WITH SERDEPROPERTIES (
           "hbase.columns.mapping" =":key,info:name_col,info:sex_col"
           )TBLPROPERTIES("hbase.table.name" = "customer");  
    
    
           create EXTERNAL table if not exists hbase_item(
                item_id string,
                name string)
           STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
           WITH SERDEPROPERTIES (
           "hbase.columns.mapping" =":key,info:name_col"
           )TBLPROPERTIES("hbase.table.name" = "item");  
    
    
         create EXTERNAL table if not exists hbase_orderLog(
                order_id string,
                customer_id int,
                item_id int,
                price int,
                order_date TIMESTAMP) 
         STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
         WITH SERDEPROPERTIES (
         "hbase.columns.mapping" =":key,info:customer_id_col,info:item_id_col,info:price_col,info:order_date_col"
         )TBLPROPERTIES("hbase.table.name" = "orderlog");  
         ```        
        ＊　导入数据
          ```text
          from customer insert into table hbase_customer select *;
          from item insert into table hbase_item select *;
          from orderlog insert into table hbase_orderlog select *;
          ```
          
          nohup beeline -u jdbc:hive2://localhost:10000/default -n root -p root -e 'from orderlog insert into table hbase_orderlog select *;' & 
   

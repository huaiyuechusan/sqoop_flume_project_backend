# Springboot实现前后端大数据交互

## 技术栈
- Springboot 3.0.6（java17+）
- ajax
- mysql
- mybatis
- sqoop
- hadoop
- hive
- flume


## 项目事务
- [x] upload上传文件，并上传给hdfs
- [x] 用户登录注册接口
- [x] hive分析最终数据
- [x] sqoop将hive表中数据转换为mysql,并显示出来
- [ ] flume实时采集数据


## 项目结构
```
├─.idea
│  └─inspectionProfiles
├─.mvn
│  └─wrapper
└─src
   └─main
      ├─java
      │  └─com
      │      └─example
      │          └─demo
      │              ├─config
      │              ├─controller
      │              ├─pojo
      │              ├─mapper
      │              └─service
      └─resources
          └─static
              ├─css
              ├─images
              └─js
```



## HQL

- 各个时期游客数量

  ```
  INSERT OVERWRITE DIRECTORY '/project/anaylse'
  row format delimited fields terminated by '\t' STORED AS textfile
  SELECT `Year_Month`, COUNT(*) AS tourist_count
  FROM admin1685091186119
  GROUP BY `Year_Month`;
  ```

- 通过date来分析各个时期的rating的平均值

  ```
  SELECT date, AVG(CAST(rating AS FLOAT)) AS avg_rating
  FROM disneydata
  GROUP BY Year_Month;
  ```

- Reviewer_Location都来自哪个地区

  ```
  INSERT OVERWRITE DIRECTORY '/project/output/disney'
  SELECT Reviewer_Location, COUNT(*) AS reviewer_count
  FROM disneydata
  GROUP BY Reviewer_Location;
  ```

- 获取评分最高的三个公园

  ```
  SELECT Branch, AVG(CAST(rating AS FLOAT)) AS avg_rating
  FROM disneydata
  GROUP BY Branch
  ORDER BY avg_rating DESC
  LIMIT 3;
  ```

  

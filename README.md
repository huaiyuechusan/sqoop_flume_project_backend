# Springbootʵ��ǰ��˴����ݽ���

## ����ջ
- Springboot 3.0.6��java17+��
- ajax
- mysql
- mybatis
- sqoop
- hadoop
- hive
- flume


## ��Ŀ����
- [x] upload�ϴ��ļ������ϴ���hdfs
- [x] �û���¼ע��ӿ�
- [x] hive������������
- [x] sqoop��hive��������ת��Ϊmysql,����ʾ����
- [ ] flumeʵʱ�ɼ�����


## ��Ŀ�ṹ
```
����.idea
��  ����inspectionProfiles
����.mvn
��  ����wrapper
����src
   ����main
      ����java
      ��  ����com
      ��      ����example
      ��          ����demo
      ��              ����config
      ��              ����controller
      ��              ����pojo
      ��              ����mapper
      ��              ����service
      ����resources
          ����static
              ����css
              ����images
              ����js
```



## HQL

- ����ʱ���ο�����

  ```
  INSERT OVERWRITE DIRECTORY '/project/anaylse'
  row format delimited fields terminated by '\t' STORED AS textfile
  SELECT `Year_Month`, COUNT(*) AS tourist_count
  FROM admin1685091186119
  GROUP BY `Year_Month`;
  ```

- ͨ��date����������ʱ�ڵ�rating��ƽ��ֵ

  ```
  SELECT date, AVG(CAST(rating AS FLOAT)) AS avg_rating
  FROM disneydata
  GROUP BY Year_Month;
  ```

- Reviewer_Location�������ĸ�����

  ```
  INSERT OVERWRITE DIRECTORY '/project/output/disney'
  SELECT Reviewer_Location, COUNT(*) AS reviewer_count
  FROM disneydata
  GROUP BY Reviewer_Location;
  ```

- ��ȡ������ߵ�������԰

  ```
  SELECT Branch, AVG(CAST(rating AS FLOAT)) AS avg_rating
  FROM disneydata
  GROUP BY Branch
  ORDER BY avg_rating DESC
  LIMIT 3;
  ```

  

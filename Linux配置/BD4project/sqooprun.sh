#!/bin/bash
export HADOOP_HOME=/training/hadoop-2.9.2
export SQOOP_HOME=/training/sqoop-1.4.6.bin__hadoop-2.0.4-alpha/
export FLUME_HOME=/training/flume/

echo "-->> clean hadoop old directory.";
hadoop fs -rm -r /$4
hadoop fs -mkdir /$4/      # 在hdfs上创建一个和数据库同名的目录,用来保存导入的数据和密匙文件

echo "-->> create mysql database password file for easy login with sqoop job.";
echo -n $2 > db.password   # -n 表示不换行输出
hadoop fs -copyFromLocal db.password /$4     # 数据库密匙文件在hdfs的/$4目录中
# hadoop fs -chmod 400 /$4/db.password

echo "-->> delete sqoop job if exist and create new sqoop job.";
sqoop job --delete importjob
# 创建job:增量导入mysql中movie表到hdfs的/$4目录
sqoop job --create importjob -- import --connect jdbc:mysql://$3:3306/$4 --username $1 --password-file /$4/db.password --table $5 --target-dir /$4/$5 -m 1


echo "-->> Start sqoop script.";

sqoop job -exec importjob; # 执行导入
if (hadoop fs -test -e /$4/$5/part-m-00000); then     # 如果导入成功
	row=$(hadoop fs -cat /$4/$5/part-m-00000 | wc -l) # 被导入数据集的行数
	echo "-->> file exist on hdfs. part-m-00000 ===> Sqoop import 【MySQL->HDFS】 successfully ; rows：$row .";# 导入成功，打印行数
else   # 导入失败
	echo "-->> file not exist on hdfs. part-m-00000 ===> Sqoop import 【MySQL->HDFS】 failed, try again";
fi

echo "-->> End sqoop script.";



# 导入：mysql->hdfs
# 连接linux上的mysql数据库就使用Linux的静态ip，连接Windows上的mysql数据库就使用IPV4地址
# 执行脚本格式：
# sh sqooprun.sh <mysql_username> <mysql_password> <linux_ip> <database_name> <table_name>
# sh sqooprun.sh root Niit1234! 192.168.186.186.110 moviedata movie
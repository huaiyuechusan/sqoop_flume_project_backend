#!/bin/bash

mysql_username=$1
mysql_password=$2
mysql_dbname=$3
mysql_tbname=$4
server_ip=$5
mysql_port=3306
export_dir=$6

create_schema () {
export MYSQL_PWD=${mysql_password};
mysql -u${mysql_username} << EOF
CREATE DATABASE IF NOT EXISTS ${mysql_dbname};
USE ${mysql_dbname};
drop table if exists ${mysql_tbname};
create table if not exists ${mysql_tbname}(
Date varchar(255) primary key,
Avg_Rating FLOAT default null);
EOF
}

# echo "-->> clean hadoop old db.password.";
# hadoop fs -rm /${mysql_dbname}/db.password
# hadoop fs -mkdir /${mysql_dbname}/      # 在hdfs上创建一个和数据库同名的目录,用来保存导入的数据和密匙文件

# echo "-->> create mysql database password file for easy login with sqoop job.";
# mkdir ~/${mysql_dbname} 
# echo -n ${mysql_password} ~/${mysql_dbname}/db.password   # -n 表示不换行输出,> 表示覆盖
# hadoop fs -copyFromLocal ~/${mysql_dbname}/db.password /${mysql_dbname}     # 数据库密匙文件存在hdfs的/${mysql_dbname}目录中

echo "-->> Start, create schema in mysql.";
create_schema 
echo "-->> End, create schema in mysql.";

echo "-->> delete sqoop job if exist and create new sqoop job.";
sqoop job --delete exportjob
# 密匙文件在sqoop导入的脚本中已创建,连接字符串?后面是为了防止中文乱码
sqoop job --create exportjob -- export \
--connect "jdbc:mysql://${server_ip}:${mysql_port}/${mysql_dbname}?useUnicode=true&characterEncoding=utf-8" \
--username ${mysql_username} --password-file /${mysql_dbname}/db.password \
--table ${mysql_tbname} \
--columns Date,Avg_Rating \
--export-dir ${export_dir} \
--input-fields-terminated-by '\t' \
--input-lines-terminated-by '\n' \
--input-null-string '\\N' \
--input-null-non-string '\\N' \
-m 1 \
--validate \
# --direct
# --update-key Reviewer_Location --update-mode  allowinsert 

echo "-->> Start sqoop export job.";
sqoop job --exec exportjob; 
rows=`mysql -u${mysql_username} -e "use ${mysql_dbname};select count(*) from ${mysql_dbname}.${mysql_tbname}"  | sed -n '2p' ` # 只取第二行
echo "-->> End, sqoop export job.";


# sqoop eval \
# --connect "jdbc:mysql://${server_ip}:${mysql_port}/${mysql_dbname}?useUnicode=true&characterEncoding=utf-8" \
# --username ${mysql_username} --password-file /${mysql_dbname}/db.password \
# --query "select count(1) from ${mysql_tbname}"  # -e和--query是一样的
echo "-->> table rows：${rows}";



# hive命令：insert overwrite directory '/project/disney/output' row format delimited fields terminated by '\t' STORED AS textfile 
# select INSERT OVERWRITE DIRECTORY '/project/output/disney'SELECT Reviewer_Location,COUNT(*)AS Reviewer_Count FROM disneydata GROUP BY Reviewer_Location;

#  sh sqoopExport.sh <mysql_username> <mysql_password> <mysql_dbname> <mysql_tbname> <server_ip> <export_dir_in_hdfs>
#  bash /root/BD4project/sqoopExport.sh root niit1234 bd4project anaylse1 192.168.178.34 /project/anaylse/admin/anaylse1/*
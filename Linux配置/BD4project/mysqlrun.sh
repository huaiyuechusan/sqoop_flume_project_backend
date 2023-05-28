drop_schema () {
export MYSQL_PWD=$2;
mysql --user=$1 << EOF     # EOF包裹的部分为sql语句
SET GLOBAL local_infile=ON; -- 设置全局参数local_infile，开启从本地加载文件导入数据的开关
DROP DATABASE IF EXISTS $3;   -- 删除数据库  
EOF
}

create_schema () {
export MYSQL_PWD=$2;
mysql --user=$1 << EOF
SET GLOBAL local_infile=ON;
CREATE DATABASE IF NOT EXISTS $3;
USE $3;
drop table if exists $4;
create table $4( 
	Review_ID int(50) NOT NULL,
  Rating int(11) DEFAULT NULL,
  Date date DEFAULT NULL,
  Reviewer_Location varchar(255) DEFAULT NULL,
  Review_Text text,
  Branch varchar(255) DEFAULT NULL,
  PRIMARY KEY (Review_ID)
  )ENGINE=InnoDB DEFAULT CHARSET=latin1;
EOF
}

insert_data () {  # 将linux本地的csv数据集导入mysql的movie表中
export MYSQL_PWD=$2;
mysql --user=$1  --local-infile << EOF
SET GLOBAL local_infile=ON;  
USE $3;
LOAD DATA LOCAL INFILE '~/$3/data$4.csv' INTO TABLE $5 FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\n' IGNORE 1 ROWS;
EOF
# 只写data.csv时该文件要放在Linux的用户家目录下（~，即登录Linux后进入家目录,实际上这里的路径是是执行mysql -u root 时的工作目录），可以修改为其它路径,如/tools/data.csv
}

# echo "-->> Start, drop schema in mysql.";
# drop_schema $1 $2 $3
# echo "-->> End, drop schema in mysql.";
echo "-->> Start, create schema and table in mysql.";
create_schema $1 $2 $3 $4
echo "-->> End, create schema in mysql.";

# rm -r data?.csv   # 删除linux本地的csv数据集文件

echo "-->> Start, Loading .csv file into mysql.";
# run script in loop
for i in $(seq 1 $5)   # $5 : csv文件的数量
do
	
	sleep 1; # wait loop here for 1 second
	# echo "-->> Download data file - data$i.csv";
    # wget -P ~/$3  http://$5:$6/download/data$i.csv     # 从服务器下载数据集，-P指定文件下载后的保存目录(目录不存在会自动创建)，默认下载到当前工作路径 (项目要部署到Linux服务器上)
  	insert_data $1 $2 $3 $i $4;
    # echo "-->> Insert $i file into mysql successfully! ";  # 输出当前已经将多少个csv文件导入表中
done
echo "-->> $5 file has been all loaded into mysql successfully! ";

echo "-->> End, client inserting data into mysql.";


# 脚本执行使用的是Linux中Mysql数据库和表

# 执行脚本前，最好进入mysql命令行执行SET GLOBAL local_infile=ON; ，然后重启mysql，否则可能无法将csv文件导入mysql表中
# 准备步骤：
# 1. csv文件名应该改为data1.csv、data2.csv、data3.csv这种格式
# 2. 在linux的用户家目录下创建一个与数据库同名的目录（必须同名），并将数据集文件全部放入其中
# 3. 修改脚本中的建表语句（只修改小括号中的内容即可）——已修改

# sh mysqlrun.sh <mysql_username> <mysql_password> <database_name> <table_name> <csvfile_number>
# sh mysqlrun.sh root Niit1234! moviedata movie 1

# 若使用网络下方式，不需要上述的步骤2（将for循环中wget一行和上一行的注释去掉）
# sh mysqlrun.sh <mysql_username> <mysql_password> <database_name> <table_name> <csvfile_number> <server_ip> <port>
# sh mysqlrun.sh root Niit1234! moviedata movie 1 192.168.186.100 8080    (项目部署到linux上时)

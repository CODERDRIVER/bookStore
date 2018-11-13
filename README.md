# Install
## 一、Basic environment installation
1. jdk 1.8.0_151
2. git 2.14.3
3. maven 3.3.9
4. mysql 5.6.39

Warning: Add the above command to the global environment path!!!
## 二、Download source code
git clone https://github.com/CODERDRIVER/bookStore.git
## 三、Configuration database environment
1. Import the bookstore.sql file under src/resources into the local mysql server。
2. Modify the value of jdbc.url in local.db.properties under src/resources
jdbc:mysql://localhost:3306/bookstore?&useUnicode=true&characterEncoding=UTF8&useSSL=false
## 四、Server deployment & run (require networking)
0. Open the console and go to the project root directory，which is /bookStore.
1. input mvn install,Installation dependence.
2. input mvn package,package this project.
3. Go into the target directory，input java -jar {the name of project jar}.jar,Do not close the console.
## 五、Visiting
Open a browser, Visit localhost:8080.
if you want go to the admin page,you should visit localhost:8080/admin/login


# Chinese and English conversion plan
https://blog.csdn.net/csdn_lqr/article/details/78026254
http://www.cnblogs.com/linyusong/p/9678850.html

<script type="text/javascript"src="https://down.tenglongw.com/js/language.js"></script>
# Date tool
https://blog.csdn.net/qq_24084925/article/details/78833533

# cron Detailed expression
https://blog.csdn.net/wqh8522/article/details/79224290
一个cron The expression has at least 6 (and possibly 7) time elements separated by spaces, in order：
- second
- minute
- hour
- day
- month
- week（1-7 1=sun）
- year

# Barcode generation solution
https://yq.aliyun.com/articles/43462

# springboot File Upload
https://www.jianshu.com/p/6572888b2e23





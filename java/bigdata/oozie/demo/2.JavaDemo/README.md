1. 通过hue将workflow.xml添加到/user/root/ooziedemo/java/workflow目录下，注意权限为root
2. 打包oozie项目，包为oozie-1.0-SNAPSHOT.jar
3. 通过hue将oozie-1.0-SNAPSHOT.jar添加到/user/root/ooziedemo/java/lib目录下，注意权限为root
4. 在oozie客户端服务器上执行命令
```shell
# 上传job.properties配置信息
rz  job.properties 
# 运行任务
oozie job -oozie http://host17218115135:11000/oozie/ -config job.properties -run
```
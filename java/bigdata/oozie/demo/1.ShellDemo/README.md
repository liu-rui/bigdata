1. 通过hue将workflow.xml添加到/user/root/ooziedemo/shell目录下，注意权限为root
2. 在oozie客户端服务器上执行命令
```shell
# 上传job.properties配置信息
rz  job.properties 
# 运行任务
oozie job -oozie http://host17218115135:11000/oozie/ -config job.properties -run
```
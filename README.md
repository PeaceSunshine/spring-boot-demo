# springboot分布式系统

## 2020/06/07 商品系统

1. 商品列表（**thymeleaf**）
2. 商品添加 （**boostrap**）
3. 商品删除  
4. 商品分页 (**pagehelper**)
5. 富文本框（**wangEditor**）

## 2020/06/10 分布式文件系统（FastDFS）

1. 文件上传（**FastDFS**,**nginx**,**uploadifive**）****
2. 图片回显
3. 富文本框html内容存入数据库



## 2020/06/11 主页系统

1. 系统服务嵌套
2. 商品分类展示



## 2020/06/12 搜索系统(solr)
1. 初始化数据
2. 将数据库中数据查询出来,进行solrClient的add;commit操作
3. 进行增量操作（数据更新的时候同时更新索引库）
4. 搜索功能，分词高亮显示
5. 分页显示（创建pageBean填充list）

```
一台服务器多个solr-core?
1.复制一份solrhome中的collection
2.修改core.properties
3.修改conf文件夹下schema.xml
多台服务器一个solr-core?
1.zookeeper（存活过半数机制）作为集群入口
2.zookeeper统一管理配置文件
```



## 2020/06/15 详情页系统(FreeMarker)

1. 根据商品id生成静态详情页
2. 批量生成商品静态详情页



## 2020/06/17  消息系统（RabbitMQ）

1. 同步服务转异步服务（提高响应速度）
2. 不一定要使用，但是代码实现要完成



## 2020/06/18 短信与邮件系统

## 2020/0729 用户系统（sso）


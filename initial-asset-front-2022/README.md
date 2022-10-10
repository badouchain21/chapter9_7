## 系统运行步骤

```bash

# 进入项目目录
cd initial-asset-front-2022

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装以来，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# 启动服务
npm run dev
```

浏览器访问 [http://localhost:8000](http://localhost:8000)

## 发布

```bash
# 构建测试环境(配置.env.test文件请求地址)
npm run build

# 构建生产环境(配置.env.prd文件请求地址)
npm run build:prd

# 构建uat环境(配置.env.uat文件请求地址)
npm run build:uat
```

## 项目启动(idea启动)

##### 1）安装好node.js并配置环境

##### 2）idea安装vue插件

```
# 安装依赖
npm install

# 启动项目
npm run dev
```

## 相关项目

配套教程文章，指导如何使用:
- [开发框架(4.1)前端框架背景](http://ksp.badousoft.com/knowledge/knowledgelist/listKnowledgeContent.do?id=ff8080816e4f1da5016f2635119f0445)

- [开发框架(4.1)前端框架目录结构](http://ksp.badousoft.com/knowledge/knowledgelist/listKnowledgeContent.do?id=ff8080816e4f1da5016f6a71568905da)

其他更多请登录ksp,搜索关键词【4.1】会有更多相关文章
- [八斗分享平台](http://ksp.badousoft.com)

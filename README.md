# 心理健康评估系统开发文档

## 1. 项目概述
一个基于Spring Boot的心理健康评估系统，支持用户进行心理测评、查看结果，管理员进行数据统计和管理。

## 2. 技术栈
- 后端：Spring Boot 3.x
- 数据库：MySQL 8.x
- ORM：MyBatis-Plus
- 安全框架：Spring Security + JWT
- 其他：Lombok、Jackson

## 3. 核心功能模块

### 3.1 用户认证模块
- JWT token认证
- 角色权限控制（USER/ADMIN）
- 密码加密存储（BCrypt）

### 3.2 问卷管理模块
- 问卷CRUD
- 动态评分规则配置
- JSON格式存储问卷结构
```json
{
    "questions": [
        {
            "id": 1,
            "text": "问题内容",
            "options": [
                {
                    "id": 1,
                    "text": "选项内容",
                    "score": 1
                }
            ]
        }
    ]
}
```

### 3.3 测评记录模块
- 答案提交与自动评分
- 结果分类与解释
- 历史记录查询

### 3.4 数据统计模块
- 多维度数据统计（性别分布、压力水平等）
- 图表数据格式化（支持饼图、柱状图、折线图）

## 4. 关键技术点

### 4.1 分页查询封装
- 基础分页请求DTO：`PageRequestDTO`
- MyBatis-Plus分页插件配置
- 统一的分页查询参数处理

### 4.2 全局异常处理
- 统一异常处理：`GlobalExceptionHandler`
- 业务异常定义：`BusinessException`
- 标准化API响应格式

### 4.3 数据验证
- 使用Jakarta Validation进行参数校验
- 自定义验证注解
- 分组验证支持

### 4.4 安全配置
- JWT Token生成与验证
- 自定义认证过滤器
- 路由安全配置
- 跨域处理

### 4.5 数据库设计
核心表结构：
- users：用户信息
- assessments：问卷信息
- user_assessments：用户答题记录
- psychological_records：心理档案
- result_explanations：结果解释

## 5. 项目特点
1. 模块化设计，清晰的代码结构
2. 统一的错误处理和响应格式
3. 完善的数据校验机制
4. 灵活的问卷配置和评分规则
5. 多维度的数据统计分析

## 6. 注意事项
1. 敏感数据（如密码）不返回给前端
2. 所有管理员接口需要ADMIN角色权限
3. 问卷删除采用逻辑删除
4. 统计数据缓存优化（待实现）
5. 答案提交需要事务处理

## 7. API文档
核心接口：
```
POST /api/users/login            # 用户登录
POST /api/assessments/submit     # 提交答案
GET  /api/admin/statistics       # 统计数据
GET  /api/admin/user-assessments # 用户测评记录
```

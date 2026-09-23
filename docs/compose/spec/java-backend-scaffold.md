---
feature: java-backend-scaffold
status: designed
updated: 2026-02-19
branch: feat/initial-scaffold
commits: <base-sha>..<head-sha>
---

# Java Backend Scaffold

## Report

## [S1] Problem
需要一个可直接开业务的 Java 后端脚手架：本地可构建可测，结构清晰，并能推送到 GitHub 作为公共模板仓库。当前 `D:\project` 无现成通用脚手架，本机有 JDK 17 但无系统级 Maven/Gradle，也无 GitHub 凭据。

## [S2] Design

### 技术栈
- Java 17、Spring Boot 3.x、Maven（提交 Wrapper，`mvnw`/`mvnw.cmd` + `.mvn/wrapper/`）
- Spring Web、Validation、Data JPA、Actuator
- springdoc-openapi（Swagger UI / OpenAPI JSON）
- 默认 H2（内存库）可跑通；生产/外部库配置由使用方按需自备，不随仓库提交
- 测试：JUnit 5 + Spring Boot Test + MockMvc

### 模块结构（单模块按层分包）
```
com.example.scaffold
├── ScaffoldApplication
├── common
│   ├── api        ApiResponse / ResultCode / PageResponse
│   ├── exception  BizException / ResourceNotFoundException / GlobalExceptionHandler
│   └── config     OpenApiConfig / WebConfig(CORS)
└── module.user    UserController / UserService / UserRepository / User / dto/*
```

### 对外契约
- 统一响应：`ApiResponse<T>`，字段 `code`（0 成功）、`message`、`data`、`timestamp`
- 分页：`PageResponse<T>`（`items`、`page`、`size`、`total`）
- 业务错误：`BizException(ResultCode, detail)`；全局异常处理器映射为对应 HTTP 状态与 `ApiResponse`
- User REST（前缀 `/api/users`）：
  - `POST /` 创建（body: name/email，email 唯一）
  - `GET /{id}` 查询
  - `GET /` 分页列表（page/size）
  - `PUT /{id}` 更新
  - `DELETE /{id}` 删除
- OpenAPI：`/v3/api-docs`、`/swagger-ui.html`（或 `/swagger-ui/index.html`）
- Actuator：`/actuator/health`

### 错误行为
- 校验失败 → 400 + 字段级 message
- 资源不存在 → 404
- 邮箱重复创建 → 409
- 未捕获异常 → 500（生产可不暴露 detail）

### 测试边界
- Web 层：MockMvc 覆盖 User CRUD 成功与主要错误路径
- 配置：`application.yml` 可在 H2 下启动；文档说明 MySQL profile

## [S3] Out of Scope
- Spring Security / JWT / RBAC
- Redis、消息队列、微服务注册中心
- Docker/K8s 部署编排
- 真实远程 GitHub 创建（等待用户提供 PAT 或远程 URL 后在 Finish 推送）

## Tasks
- [ ] T1: 初始化 Maven 工程、Wrapper 与基础配置 — acceptance: `./mvnw -q -DskipTests package` 可离线 Wrapper 引导并编译（covers: S2）
- [ ] T2: 实现 common 层（ApiResponse/ResultCode/PageResponse/异常/全局处理器/OpenAPI/CORS） — acceptance: 模块可编译，异常路径返回统一 JSON（covers: S2; depends: T1）
- [ ] T3: 实现 User 示例 CRUD（entity/repo/service/controller/dto） — acceptance: `/api/users` 五类操作可用，邮箱唯一冲突返回 409（covers: S2; depends: T2）
- [ ] T4: 配置 application*.yml 与 README — acceptance: 默认 H2 可启动；README 说明构建、运行、Swagger、MySQL 切换（covers: S2; depends: T1）
- [ ] T5: 编写并跑通单元/集成测试 — acceptance: `./mvnw test` 全绿，覆盖 CRUD 与校验/404/409（covers: S2; depends: T3）

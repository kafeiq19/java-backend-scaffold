---
feature: java-backend-scaffold
status: delivered
updated: 2026-09-23
branch: feat/initial-scaffold
commits: 785ca4c95f402f0848c6aea18f19c43afa11d0c8..37c527a4b40fcafd4a8e6e05b006e655895d1970
---

# Java Backend Scaffold

## Report

**What was built** — Spring Boot 3.5 + Java 17 单模块 Maven 脚手架，含 Maven Wrapper、分层结构（`common/{api,config,exception}` + `module/user`）、统一响应 `ApiResponse`/`PageResponse`、全局异常映射（400/404/405/409/500）、springdoc OpenAPI、Actuator health、H2 默认可跑的 User 示例 CRUD，以及 CI workflow 与 README。环境/外部库配置按约定不入库。

**Verification** — `mvn clean test`：Tests run 12, Failures 0, Errors 0（PASS）。`mvn -DskipTests package`：BUILD SUCCESS，boot jar 产出（PASS）。初审 8 项验收均满足；复审确认异常映射与邮箱唯一约束兜底修复到位。

**Journey log** —
1. 无系统 Maven/gh，改用便携 Maven + only-script Wrapper；Wrapper jar 非必需。
2. 中央仓库拉依赖超时，改阿里云镜像后 `clean test` 约 48s 完成。
3. 用户要求环境配置不随仓库提交：移除 mysql/prod profile 与 README 中的切换说明，并同步修正规格 T4。
4. 初审 CRITICAL：catch-all 吞掉 400/404——补 `HttpMessageNotReadableException`/`NoResourceFoundException` 等显式映射。
5. TOCTOU 邮箱冲突：`saveAndFlush` + `DataIntegrityViolationException` → 409 兜底；补更新冲突与坏 JSON 测试。

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
- 校验失败 / 非法 JSON body → 400 + message
- 资源不存在 / 未知路由 → 404
- 邮箱重复（创建/更新，含并发唯一约束兜底）→ 409
- 方法不允许 → 405
- 未捕获异常 → 500（生产可不暴露 detail）

### 测试边界
- Web 层：MockMvc 覆盖 User CRUD 成功与主要错误路径（含更新邮箱冲突）
- 配置：`application.yml` 可在 H2 下启动

## [S3] Out of Scope
- Spring Security / JWT / RBAC
- Redis、消息队列、微服务注册中心
- Docker/K8s 部署编排
- 真实远程 GitHub 创建（等待用户提供 PAT 或远程 URL 后在 Finish 推送）

## Tasks
- [x] T1: 初始化 Maven 工程、Wrapper 与基础配置 — acceptance: `./mvnw -q -DskipTests package` 可离线 Wrapper 引导并编译（covers: S2）
- [x] T2: 实现 common 层（ApiResponse/ResultCode/PageResponse/异常/全局处理器/OpenAPI/CORS） — acceptance: 模块可编译，异常路径返回统一 JSON（covers: S2; depends: T1）
- [x] T3: 实现 User 示例 CRUD（entity/repo/service/controller/dto） — acceptance: `/api/users` 五类操作可用，邮箱唯一冲突返回 409（covers: S2; depends: T2）
- [x] T4: 配置 application.yml / application-dev.yml 与 README — acceptance: 默认 H2 可启动；README 说明构建、运行、Swagger（covers: S2; depends: T1）
- [x] T5: 编写并跑通单元/集成测试 — acceptance: `./mvnw test` 全绿，覆盖 CRUD 与校验/404/409（covers: S2; depends: T3）

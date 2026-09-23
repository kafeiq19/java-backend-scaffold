# Java Backend Scaffold

Spring Boot 3 + Java 17 后端脚手架：统一分层、统一响应/异常、OpenAPI、示例 User CRUD。

## 技术栈

| 项 | 选型 |
|---|---|
| 语言 | Java 17 |
| 框架 | Spring Boot 3.5.x |
| 构建 | Maven Wrapper（`./mvnw` / `mvnw.cmd`） |
| Web | Spring MVC |
| 数据 | Spring Data JPA（默认 H2） |
| 文档 | springdoc-openapi（Swagger UI） |
| 监控 | Spring Boot Actuator |
| 测试 | JUnit 5 + MockMvc |

## 快速开始

```bash
# 构建
./mvnw clean package

# 运行（默认 dev + H2 内存库）
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

启动后：

- 健康检查: <http://localhost:8080/actuator/health>
- Swagger UI: <http://localhost:8080/swagger-ui.html>
- OpenAPI JSON: <http://localhost:8080/v3/api-docs>
- H2 控制台（仅 dev）: <http://localhost:8080/h2-console>

## 本地配置（不要提交真实配置）

仓库只带可运行的默认配置。真实连接、密钥等写在本地，已用 `.gitignore` 排除。

1. 环境变量：复制 `.env.example` 为 `.env` 后填写  
2. 覆盖配置：复制 `config/application-local.yml.example` 为 `src/main/resources/application-local.yml`（或本机任意 `application-*.yml`）后修改

示例（写入本地 `application-local.yml`，勿提交）：

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:scaffold;MODE=MYSQL;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver
    username: sa
    password:
```

外部库时把 `url`/`username`/`password` 换成你的值，密码建议用环境变量占位（如 `${DB_PASSWORD}`）。

## 示例 API（User CRUD）

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/api/users` | 创建用户 |
| GET | `/api/users/{id}` | 按 ID 查询 |
| GET | `/api/users?page=0&size=10` | 分页列表 |
| PUT | `/api/users/{id}` | 更新 |
| DELETE | `/api/users/{id}` | 删除 |

请求示例：

```bash
curl -X POST http://localhost:8080/api/users \
  -H 'Content-Type: application/json' \
  -d '{"name":"Ada","email":"ada@example.com"}'
```

统一响应：

```json
{
  "code": 0,
  "message": "success",
  "data": { "id": 1, "name": "Ada", "email": "ada@example.com" },
  "timestamp": "2026-02-19T12:00:00Z"
}
```

- 成功：`code = 0`
- 校验失败：HTTP 400，`code = 400`
- 不存在：HTTP 404
- 邮箱冲突：HTTP 409
- 未处理异常：HTTP 500

## 工程结构

```
src/main/java/com/example/scaffold
├── ScaffoldApplication.java
├── common
│   ├── api/          # ApiResponse / ResultCode / PageResponse
│   ├── config/       # OpenAPI / CORS
│   └── exception/    # BizException / GlobalExceptionHandler
└── module/user/      # 示例业务：Controller / Service / Repository / DTO
```

新增业务时建议在 `module/<feature>/` 下按同样分层扩展。

## 测试

```bash
./mvnw test
```

## 推送到 GitHub

```bash
# 在 GitHub 创建空仓库 java-backend-scaffold 后
git remote add origin git@github.com:<your-name>/java-backend-scaffold.git
git push -u origin main
```

## 许可证

MIT

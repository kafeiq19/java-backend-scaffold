# Java Backend Scaffold

基于 Spring Boot 3 与 Java 17 的后端起步工程，包含分层结构、统一响应与异常处理、OpenAPI 文档，以及 User 示例接口。

## 技术栈

- Java 17
- Spring Boot 3.5.x
- Maven Wrapper（`./mvnw` / `mvnw.cmd`）
- Spring MVC / Validation / Data JPA / Actuator
- springdoc-openapi
- 测试：JUnit 5、Spring Boot Test、MockMvc
- 默认使用 H2 内存库，本地可直接运行

## 快速开始

```bash
./mvnw clean package
./mvnw spring-boot:run
# Windows
mvnw.cmd spring-boot:run
```

服务默认监听 `8080`：

| 用途 | 地址 |
|------|------|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| 健康检查 | http://localhost:8080/actuator/health |
| H2 控制台（dev） | http://localhost:8080/h2-console |

## 配置

仓库内仅包含可直接运行的默认配置（H2）。本地开发如需连接其他数据源，请使用被 `.gitignore` 排除的本地文件，避免将真实配置提交到仓库。

| 用途 | 模板 | 本地目标路径 |
|------|------|----------------|
| 环境变量 | `.env.example` | `.env` |
| Spring 配置覆盖 | `config/application-local.yml.example` | `src/main/resources/application-local.yml` |

`application-local.yml` 示例：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/scaffold
    username: root
    password: ${DB_PASSWORD}
```

密码等敏感项请通过环境变量注入，不要写入仓库。

## API

示例资源为 User，前缀 `/api/users`：

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/users` | 创建用户 |
| GET | `/api/users/{id}` | 按 ID 查询 |
| GET | `/api/users?page=0&size=10` | 分页列表 |
| PUT | `/api/users/{id}` | 更新用户 |
| DELETE | `/api/users/{id}` | 删除用户 |

请求示例：

```bash
curl -X POST http://localhost:8080/api/users \
  -H 'Content-Type: application/json' \
  -d '{"name":"Ada","email":"ada@example.com"}'
```

响应统一格式：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1,
    "name": "Ada",
    "email": "ada@example.com"
  },
  "timestamp": "2026-02-19T12:00:00Z"
}
```

`code = 0` 表示成功。常见错误：参数校验失败 400，资源不存在 404，邮箱冲突 409，服务端异常 500。

## 工程结构

```
src/main/java/com/example/scaffold
├── ScaffoldApplication.java
├── common
│   ├── api/          # ApiResponse / ResultCode / PageResponse
│   ├── config/       # OpenAPI / CORS
│   └── exception/    # 业务异常与全局异常处理
└── module/user/      # 示例业务（Controller / Service / Repository / DTO）
```

新增业务模块时，建议在 `module/<feature>/` 下按相同分层扩展。

## 测试

```bash
./mvnw test
```

## License

MIT

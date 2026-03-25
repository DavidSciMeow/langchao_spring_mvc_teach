# Spring MVC 快速入门项目

## 项目概述

这是一个基于 Spring MVC 的 Web 应用示例项目，包含完整的 Web 界面和数据库交互功能。

## 技术栈

- **构建工具**: Maven
- **Java 版本**: 1.8
- **Spring 版本**: 5.0.5.RELEASE
- **Web 框架**: Spring MVC
- **数据库**: MySQL
- **连接池**: Druid
- **日志**: Log4j

## 项目结构

```
springmvc_01_quick/
├── pom.xml                    # Maven 配置文件
├── README.md                  # 项目说明文档
├── src/
│   ├── main/
│   │   ├── java/com/binyi/   # 主应用代码
│   │   ├── resources/        # 配置文件
│   │   │   ├── applicationContext.xml
│   │   │   ├── jdbc.properties
│   │   │   ├── log4j.properties
│   │   │   └── spring-mvc.xml
│   │   └── webapp/           # Web 资源
│   │       ├── 403.jsp
│   │       ├── 404.jsp
│   │       ├── 500.jsp
│   │       ├── failer.jsp
│   │       ├── index.jsp
│   │       ├── login.jsp
│   │       ├── css/
│   │       │   └── style.css
│   │       ├── img/
│   │       ├── pages/        # 页面
│   │       │   ├── aside.jsp
│   │       │   ├── header.jsp
│   │       │   ├── main.jsp
│   │       │   ├── role-add.jsp
│   │       │   ├── role-list.jsp
│   │       │   ├── syslog-list.jsp
│   │       │   ├── user-add.jsp
│   │       │   └── user-list.jsp
│   │       └── plugins/      # 插件
│   │           └── adminLTE/
│   └── test-classes/         # 测试类
└── target/                   # 编译输出
```

## 快速开始

### 编译项目

```bash
mvn clean package
```

### 使用 Jetty 运行 (开发)

可以使用 Maven 的 Jetty 插件在开发环境中直接运行 webapp（无需打包）：

```bash
# 在项目根目录运行（默认端口 8080）
mvn jetty:run

# 指定端口，例如运行在 9090
mvn -Djetty.port=9090 jetty:run
```

启动后，访问 `http://localhost:8080`（或指定端口）查看应用。

### 3. 访问应用

启动后，应用会默认在 `http://localhost:8080` 上运行。

## 数据库配置

项目使用 MySQL 数据库，配置文件位于 `src/main/resources/` 目录下：

- `jdbc.properties` - JDBC 连接配置
- `log4j.properties` - 日志配置
- `spring-mvc.xml` - Spring MVC 配置

## 主要功能

- 用户管理（增删改查）
- 角色管理
- 系统日志记录
- 分页查询
- 表单验证

## 依赖说明

| 依赖 | 版本 | 说明 |
|------|------|------|
| mysql-connector-java | 5.1.32 | MySQL 驱动 |
| c3p0 | 0.9.1.2 | 数据库连接池 |
| druid | 1.1.10 | 高性能连接池 |
| spring-context | 5.0.5.RELEASE | Spring 上下文 |
| spring-test | 5.0.5.RELEASE | Spring 测试 |
| spring-web | 5.0.5.RELEASE | Spring Web 支持 |
| spring-webmvc | 5.0.5.RELEASE | Spring MVC 核心 |
| jackson-* | 2.9.0 | JSON 处理 |
| commons-fileupload | 1.3.1 | 文件上传 |
| commons-io | 2.3 | IO 工具 |
| commons-logging | 1.2 | 日志框架 |
| slf4j-log4j12 | 1.7.7 | SLF4J 实现 |
| log4j | 1.2.17 | 日志框架 |
| jstl | 1.2 | JSTL 标签库 |

## 开发建议

1. **代码规范**: 遵循项目中的代码规范
2. **单元测试**: 使用 Maven 的测试插件编写单元测试
3. **日志配置**: 使用 Log4j 配置日志输出
4. **数据库连接**: 使用 Druid 连接池优化性能

## 许可证

MIT License

# langchao_spring_mvc — 运行与演示说明

简要说明如何在本地构建、运行该 Spring MVC 项目以及如何复现事务演示（正常提交与异常回滚）。

**先决条件**
- 已安装 JDK 17（或兼容的 Java 17 环境）。
- 已安装 Maven（推荐通过 Homebrew 在 macOS 上安装：`brew install maven`）。

**项目结构要点**
- Spring 配置文件：`src/main/resources/applicationContext.xml`、`src/main/resources/spring-mvc.xml`。
- 数据库配置：`src/main/resources/db.properties`（使用 SQLite，默认文件 `database.db` 在项目根目录）。
- 示例控制器/Servlet：`src/main/java/com/langchao/web/UserServlet.java`（支持列出账户与转账演示）。
- DAO/Service：`src/main/java/com/langchao/dao` 与 `src/main/java/com/langchao/service`（含事务边界 `@Transactional`）。
- 演示输出/证据已保存到仓库的 `docs/` 目录（文本快照）。

**构建**
在项目根目录运行：

```bash
mvn -DskipTests clean package
```

**用 Jetty 本地运行（开发）**
在项目根目录执行：

```bash
mvn jetty:run
```

这会把应用部署到 `http://localhost:8080`（默认）。若想在后台运行并把日志保存到 `target/jetty.log`：

```bash
mvn jetty:run > target/jetty.log 2>&1 &
```

停止 Jetty（在前台运行时按 `Ctrl+C`，或找出后台进程并杀掉）：

```bash
lsof -iTCP:8080 -sTCP:LISTEN
# kill <PID>
```

**演示：列出账户 / 发起转账**
1. 列出账户（查看当前余额）：

```bash
curl -i "http://localhost:8080/userServlet?action=list"
```

2. 正常转账（示例：`lucy` → `tom` 转 500）：

```bash
curl -i "http://localhost:8080/userServlet?from=lucy&to=tom&amount=500&simulate=false"
```

3. 模拟异常（在借记后抛出异常以触发回滚）：

```bash
curl -i "http://localhost:8080/userServlet?from=lucy&to=tom&amount=500&simulate=true"
```

演示要点：Service 层使用 `@Transactional`（Spring 的 `DataSourceTransactionManager`），正常调用应提交变更；当 `simulate=true` 时会在借记后抛出运行时异常，期望回滚，账户余额不应发生持久化改变。

**数据库（SQLite）**
- DB 驱动：`org.xerial:sqlite-jdbc`（在 `pom.xml` 中）。
- 默认连接地址在 `src/main/resources/db.properties`（`jdbc.url=jdbc:sqlite:database.db`），数据库文件 `database.db` 位于项目根目录。
- 如果想清除/重建演示数据，可删除 `database.db`，下次应用启动或 DAO 的 `init` 方法会重新创建并插入示例账户数据。

**已做的主要更改（供审阅）**
- 清理 `pom.xml` 中重复的 `junit` 依赖并加入 `jetty-maven-plugin` 与 `sqlite-jdbc`。
- 在 `applicationContext.xml` 中添加 `dataSource`、`jdbcTemplate` 与事务管理器（`DataSourceTransactionManager`）并启用 `<tx:annotation-driven/>`。
- 新增 `Account` POJO，扩展 `UserDao` 与实现 `UserDaoImpl`（基于 `JdbcTemplate`），实现 `UserServiceImpl.transfer(...)` 并加 `@Transactional`。
- 修改 `UserServlet` 支持 `action=list` 与通过查询参数发起转账（并可用 `simulate` 参数触发异常）。

**仓库中的证据**
- `docs/state_before.txt` — 初始账户快照。
- `docs/transfer_normal_http.txt` — 正常转账的 HTTP 响应文本快照。
- `docs/state_after_normal.txt` — 正常转账后的账户快照。
- `docs/transfer_exception_http.txt` — 模拟异常时的 HTTP 响应快照。
- `docs/state_after_exception.txt` — 异常后账户快照（应显示回滚，无余额变化）。

**常见问题与排查**
- 如果 Jetty 无法启动，检查端口占用（8080）并改端口或停止占用进程。
- 若 JDBC 驱动加载失败，确认 `sqlite-jdbc` 已在 `pom.xml` 中并已 `mvn package` 成功。
- 若数据看起来未初始化，删除 `database.db` 并重启应用以重新执行 DAO 的 `init` 方法。

如需我把 README 翻译为英文、生成示例请求的截图，或把 `database.db` 移到 `data/` 目录并更新 `db.properties`，告诉我我会继续执行。

----
（本 README 于 2026-03-21 根据当前仓库状态自动生成。）

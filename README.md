# JSON Editor with Spring Boot 2.x and AdminLTE

这是一个基于Java、Spring Boot 2.x和AdminLTE的JSON编辑器应用。

## 功能特性

- **JSON格式化**：自动格式化输入的JSON文本
- **语法高亮**：使用Prism.js进行JSON语法高亮
- **验证功能**：实时验证JSON语法正确性
- **响应式设计**：基于AdminLTE的现代化UI界面
- **错误提示**：友好的错误信息显示

## 技术栈

- Java 8+
- Spring Boot 2.x
- Thymeleaf (模板引擎)
- AdminLTE 3.x
- Bootstrap 4.x
- Prism.js (语法高亮)
- jQuery

## 项目结构

```
json-editor-demo/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/jsoneditor/
│       │       ├── JsonEditorApplication.java
│       │       ├── config/
│       │       │   └── WebConfig.java
│       │       └── controller/
│       │           └── JsonEditorController.java
│       └── resources/
│           ├── static/
│           │   └── css/
│           │       └── custom.css
│           └── templates/
│               ├── layout.html
│               └── index.html
└── pom.xml
```

## 运行说明

### 1. 构建项目

```bash
cd json-editor-demo
mvn clean package
```

### 2. 运行应用

```bash
java -jar target/json-editor-demo-1.0.0.jar
```

或者直接运行：

```bash
mvn spring-boot:run
```

### 3. 访问应用

打开浏览器访问：http://localhost:8080

## 使用方法

1. 在左侧文本框中输入或粘贴JSON数据
2. 点击"格式化JSON"按钮进行格式化和验证
3. 如果JSON有效，右侧会显示格式化后的结果
4. 如果JSON无效，会显示具体的错误信息

## 自定义

- **修改样式**：编辑 `src/main/resources/static/css/custom.css`
- **添加功能**：在 `JsonEditorController.java` 中添加新的API端点
- **调整布局**：修改 `src/main/resources/templates/` 下的HTML模板

## 依赖说明

- **AdminLTE**：通过CDN引入，无需本地安装
- **Prism.js**：用于JSON语法高亮
- **Font Awesome**：提供图标支持
- **jQuery**：AdminLTE的依赖

## 扩展建议

1. **保存功能**：添加本地存储或数据库保存功能
2. **文件上传**：支持上传JSON文件进行编辑
3. **导出功能**：支持导出格式化后的JSON文件
4. **历史记录**：保存编辑历史
5. **多标签页**：支持同时编辑多个JSON文档

## 许可证

MIT License
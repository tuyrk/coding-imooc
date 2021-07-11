# 第8章 项目：可视化UML工作流引擎web系统：前端页面设计与实现

> 前端主要基于LayUI-mini+BPMN-JS，将掌握LayUI组件使用，ajax请求接口数据并渲染展示到页面上等。BPMN-JS首先基于官网中功能接口，并结合项目扩展官网接口、高亮显示接口可以绘制历史流程，元素点击事件可以绑定表单等操作。

### 8-1 BPMNJS扩展-BPMN下载(上)

### 8-2 BPMNJS扩展-BPMN下载（下）

### 8-3 BPMNJS扩展-BPMN在线部署

### 8-4 BPMNJS扩展-BPMN导入（上）

上传文件

```java
@PostMapping("/upload")
public AjaxResponse<?> upload(@RequestParam("processFile") MultipartFile multipartFile) {
  if (multipartFile.isEmpty()) {
    return AjaxResponse.error("文件为空");
  }
  String originalFilename = multipartFile.getOriginalFilename();  // 文件名
  String suffixName = FilenameUtils.getExtension(originalFilename);  // 后缀名
  // 本地路径格式转上传路径格式
  String filepath = bpmnPathMapping.replace("file:", ""); // 上传后的路径
  // String filepath = request.getSession().getServletContext().getRealPath("/") + "bpmn/";

  String filename = UUID.randomUUID() + "." + suffixName; // 新文件名
  File file = new File(filepath + filename);
  if (!file.getParentFile().exists()) {
    file.getParentFile().mkdirs();
  }
  try {
    multipartFile.transferTo(file);
  } catch (Exception e) {
    return AjaxResponse.error(e.getLocalizedMessage());
  }
  return AjaxResponse.success(filename);
}
```

### 8-5 BPMNJS扩展-BPMN导入（下）

静态资源路径配置

```java
@Configuration
public class PathMapping implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // 默认也有这个路径映射
    registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/");
    registry.addResourceHandler("/bpmn/**").addResourceLocations(bpmnPathMapping);
  }
}
```



### 8-6 BPMNJS扩展-查看与高亮历史

### 8-7 layuimini部署

### 8-8 登录页面

### 8-9 列表页面

### 8-10 前端与接口

### 8-11 动态表格
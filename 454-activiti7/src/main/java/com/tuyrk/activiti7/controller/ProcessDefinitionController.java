package com.tuyrk.activiti7.controller;

import com.tuyrk.activiti7.SecurityUtil;
import com.tuyrk.activiti7.exception.Activiti7Exception;
import com.tuyrk.activiti7.mapper.ActivitiMapper;
import com.tuyrk.activiti7.util.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipInputStream;

/**
 * 流程定义
 *
 * @author tuyrk@qq.com
 */
@Slf4j
@RestController
@RequestMapping("/processDefinition")
public class ProcessDefinitionController {


    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActivitiMapper activitiMapper;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private ProcessRuntime processRuntime;

    @Value("${activiti7.test}")
    private boolean test;
    @Value("${activiti7.bpmn-path-mapping}")
    private String bpmnPathMapping;

    /**
     * 添加流程定义-上传BPMN流媒体
     */
    @PostMapping("/uploadStreamAndDeployment")
    public AjaxResponse<?> uploadStreamAndDeployment(@RequestParam("processFile") MultipartFile multipartFile,
                                                     @RequestParam(value = "deploymentName", defaultValue = "") String deploymentName) throws IOException {
        // 获取上传的文件名
        String fileName = multipartFile.getOriginalFilename();
        // 得到输入流（字节流）对象
        InputStream fileInputStream = multipartFile.getInputStream();
        // 文件的扩展名
        String extension = FilenameUtils.getExtension(fileName);

        // ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();//创建处理引擎实例
        // repositoryService = processEngine.getRepositoryService();//创建仓库服务实例

        Deployment deployment;
        if ("zip".equals(extension)) {
            ZipInputStream zip = new ZipInputStream(fileInputStream);
            deployment = repositoryService.createDeployment() // 初始化流程
                    .addZipInputStream(zip)
                    .name(deploymentName)
                    .deploy();
        } else {
            deployment = repositoryService.createDeployment() // ¬初始化流程
                    .addInputStream(fileName, fileInputStream)
                    .name(deploymentName)
                    .deploy();
        }

        return AjaxResponse.success(deployment);
    }

    /**
     * 添加流程定义-部署BPMN字符
     */
    @PostMapping("/addDeploymentByString")
    public AjaxResponse<?> addDeploymentByString(@RequestParam("stringBPMN") String stringBPMN,
                                                 @RequestParam(value = "deploymentName", defaultValue = "") String deploymentName) {
        Deployment deployment = repositoryService.createDeployment()
                .addString("CreateWithBPMNJS.bpmn", stringBPMN)
                .name(deploymentName)
                .deploy();
        return AjaxResponse.success(deployment);
    }

    /**
     * 获取流程定义列表
     */
    @GetMapping("/getDefinitions")
    public AjaxResponse<?> getDefinitions() {
        List<HashMap<String, Object>> listMap = new ArrayList<>();
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
        processDefinitionList.sort((x, y) -> y.getVersion() - x.getVersion());

        for (ProcessDefinition processDefinition : processDefinitionList) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("processDefinitionID", processDefinition.getId());
            hashMap.put("name", processDefinition.getName());
            hashMap.put("key", processDefinition.getKey());
            hashMap.put("resourceName", processDefinition.getResourceName());
            hashMap.put("deploymentID", processDefinition.getDeploymentId());
            hashMap.put("version", processDefinition.getVersion());
            listMap.add(hashMap);
        }

        return AjaxResponse.success(listMap);
    }

    /**
     * 获取流程定义XML
     */
    @GetMapping("/getDefinitionXML")
    public AjaxResponse<?> getProcessDefineXML(@RequestParam("deploymentId") String deploymentId,
                                               @RequestParam("resourceName") String resourceName) throws IOException {
        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
        String processDefineXML = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        return AjaxResponse.success(processDefineXML);
    }

    /**
     * 获取流程部署列表
     */
    @GetMapping("/getDeployments")
    public AjaxResponse<?> getDeployments() {
        List<HashMap<String, Object>> listMap = new ArrayList<>();
        List<Deployment> deploymentList = repositoryService.createDeploymentQuery().list();
        for (Deployment deployment : deploymentList) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", deployment.getId());
            hashMap.put("name", deployment.getName());
            hashMap.put("deploymentTime", deployment.getDeploymentTime());
            listMap.add(hashMap);
        }

        return AjaxResponse.success(listMap);
    }

    /**
     * 删除流程定义
     */
    @PostMapping("/deleteDefinition")
    public AjaxResponse<?> delDefinition(@RequestParam("deploymentId") String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
        return AjaxResponse.success(null);
    }

    /**
     * 上传文件
     *
     * @param multipartFile 文件信息
     * @return 文件名
     */
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


    /**
     * @param deploymentFileUUID
     * @return
     */
    @PostMapping("/addDeploymentByFileNameBPMN")
    public AjaxResponse<?> addDeploymentByFileNameBPMN(@RequestParam("deploymentFileUUID") String deploymentFileUUID,
                                                       @RequestParam("deploymentName") String deploymentName) {
        try {
            String filename = "resources/bpmn/" + deploymentFileUUID;
            Deployment deployment = repositoryService.createDeployment()//初始化流程
                    .addClasspathResource(filename)
                    .name(deploymentName)
                    .deploy();
            //System.out.println(deployment.getName());
            return AjaxResponse.success(deployment.getId());
        } catch (Exception e) {
            throw new Activiti7Exception("BPMN部署流程失败:" + e.getLocalizedMessage());
        }

    }


    @GetMapping("/getDefinitionsWithTest")
    public AjaxResponse<?> getDefinitionsWithTest() {
        if (test) {
            securityUtil.logInAs("wukong");
        }
        Page<org.activiti.api.process.model.ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 50));
        System.out.println("流程定义数量： " + processDefinitionPage.getTotalItems());
        for (org.activiti.api.process.model.ProcessDefinition pd : processDefinitionPage.getContent()) {
            System.out.println("getId：" + pd.getId());
            System.out.println("getName：" + pd.getName());
            System.out.println("getStatus：" + pd.getKey());
            System.out.println("getStatus：" + pd.getFormKey());
        }

        return AjaxResponse.success(processDefinitionPage.getContent());
    }
}

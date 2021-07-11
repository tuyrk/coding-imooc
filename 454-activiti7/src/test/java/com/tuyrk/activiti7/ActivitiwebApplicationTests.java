package com.tuyrk.activiti7;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ActivitiwebApplicationTests {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected HistoryService historyService;

    @Autowired
    protected ProcessRuntime processRuntime;
    @Autowired
    protected TaskRuntime taskRuntime;
    @Autowired
    protected SecurityUtil securityUtil;

    @Test
    private void contextLoads() {
    }
}

package com.bettem.flow;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class FlowableSpringbootApplicationTests {

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Test
    void testDeploy() {
        System.out.println("processEngine = " + processEngine);
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("请假流程-e.bpmn20.xml")
                .name("holiday")
                .deploy();
        System.out.println("deployment.getId() = " + deployment.getId());
        System.out.println("deployment.getName() = " + deployment.getName());
    }

    @Test
    void testStart() {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("assignee0","张三");
        hashMap.put("assignee1","张三");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById("holiday-e:2:f0b5bf05-ce9b-11ef-b01a-5ce0c5580637", hashMap);
    }

    @Test
    void testComplete() {
        Task task = taskService.createTaskQuery()
                .processDefinitionId("holiday-e:2:f0b5bf05-ce9b-11ef-b01a-5ce0c5580637")
                .taskAssignee("张三")
                .singleResult();
        if(task != null){
            taskService.complete(task.getId());
        }
    }


}

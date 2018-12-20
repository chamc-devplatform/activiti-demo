package com.zyf.activiti.engine.first;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class ProcessTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	@Test
	public void deployByInputStream() throws IOException {
		String resourceName = "leaveProcess.bpmn";
		// 定义的文件信息的流读取
		InputStream inputStream = DeploymentBuilderTest.class.getClassLoader()
				.getResource("com/zyf/activiti/diagrams/leaveProcess.bpmn").openStream();
		// 构造DeploymentBuilder对象
		DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService()
				.createDeployment().name("leaveProcess")
				.addInputStream(resourceName, inputStream);
		// 部署
		Deployment deployment = deploymentBuilder.deploy();
		
		long count = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("leaveProcess").latestVersion().count();
		System.out.println(count);
		System.out.println("----deployByInputStream----");
		System.out.println("部署ID：" + deployment.getId());
		System.out.println("部署名称:" + deployment.getName());

	}
	
	@Test
	public void startProcessInstanceByKey() {
		processEngine.getRuntimeService().startProcessInstanceByKey("leaveProcess");
		this.queryProcessInstances();
	}
	
	
	//@Test
	public void queryProcessInstances() {
		List<ProcessInstance> instances = processEngine.getRuntimeService().createProcessInstanceQuery().list();
		
		System.out.println("leaveProcess 流程实例列表：");
		for(ProcessInstance instance : instances) {
			System.out.println("instanceId: " + instance.getId() + "  processDefinitionId: " + instance.getProcessDefinitionId());
		}
		System.out.println("---------------");
	}
	
	@Test
	public void queryTasks() {
		
		List<Task> tasks = processEngine.getTaskService().createTaskQuery().processDefinitionKey("leaveProcess").list();
		System.out.println("leaveProcess 待办列表：");
		for(Task task : tasks) {
			System.out.println("taskDefinitionKey: " + task.getTaskDefinitionKey() + " taskId: " +task.getId());
		}
		System.out.println("---------------");
	}
	
	@Test
	public void completeTask() {
		TaskService taskService = processEngine.getTaskService();
		Task task = taskService.createTaskQuery().processDefinitionKey("leaveProcess").singleResult();
		taskService.complete(task.getId());
		this.queryTasks();
	}
	
}

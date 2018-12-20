package com.zyf.activiti.engine.first;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ProcessEngine;
//import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;

public class DeploymentBuilderTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	// ProcessEngine processEngine =
	// ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration().buildProcessEngine();
	/**
	 * 路径资源方式部署
	 */
	@Test
	public void deployByClasspathResource() {
		Deployment deployment = processEngine.getRepositoryService()// 与流程定义和部署对象相关的Service
				.createDeployment()// 创建一个部署对象
				.name("deployByClasspathResource")// 添加部署名称
				.addClasspathResource("com/zyf/activiti/diagrams/deployByClasspathResource.bpmn")// 从classpath的资源中加载，一次只能加载一个文件
				.addClasspathResource("com/zyf/activiti/diagrams/deployByClasspathResource.png")
				.deploy();// 完成部署
		System.out.println("----deployByClasspathResource----");
		System.out.println("部署ID：" + deployment.getId());
		System.out.println("部署名称:" + deployment.getName());

	}

	/**
	 * 文本方式部署流程 先把文本内容转化为string 再进行部署
	 */
	@Test
	public void deployByString() {
		String resourceName = "deployByString.bpmn";
		// 读取文件获取文件中定义的xml信息
		String text = readTxtFile(
				"D:/04.WorkPlace/activiti/zong-first-activiti/src/main/resources/com/zyf/activiti/diagrams/deployByString.bpmn");
		// 构造DeploymentBuilder对象
		DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService().createDeployment()
				.addString(resourceName, text).name("deployByString");
		// 部署
		Deployment deployment = deploymentBuilder.deploy();

		System.out.println("----deployByString----");
		System.out.println("部署ID：" + deployment.getId());
		System.out.println("部署名称:" + deployment.getName());
	}
	
	@Test
	public void deployByInputStream() throws IOException {
		String resourceName = "deployByInputStream.bpmn";
		// 定义的文件信息的流读取
		InputStream inputStream = DeploymentBuilderTest.class.getClassLoader()
				.getResource("com/zyf/activiti/diagrams/deployByInputStream.bpmn").openStream();
		// 构造DeploymentBuilder对象
		DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService()
				.createDeployment().name("deployByInputStream")
				.addInputStream(resourceName, inputStream);
		// 部署
		Deployment deployment = deploymentBuilder.deploy();
		
		System.out.println("----deployByInputStream----");
		System.out.println("部署ID：" + deployment.getId());
		System.out.println("部署名称:" + deployment.getName());

	}

	@Test
	public void deployByBpmnModel() throws UnsupportedEncodingException {
		BpmnModel bpmnModel = createBpmnModel();
		BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
		String bpmn20Xml = new String(bpmnXMLConverter.convertToXML(bpmnModel), "UTF-8");
		System.out.println(bpmn20Xml);

		String resourceName = "deployByBpmnModel.bpmn";
		DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService()
				.createDeployment().addBpmnModel(resourceName, bpmnModel).name("deployByBpmnModel");
		Deployment deployment = deploymentBuilder.deploy();
		
		System.out.println("----deployByBpmnModel----");
		System.out.println("部署ID：" + deployment.getId());
		System.out.println("部署名称:" + deployment.getName());
	}

	private BpmnModel createBpmnModel() {
		// 注意所有的setId中的id必须是全局唯一的不能重复
		// 2.setSourceRef和setTargetRef中的值必须是id值，不是name值
		// 流程的定义是：开始节点(id:start1,name:开始节点)-->连线1(id:flow1,name:开始节点->任务节点1)
		// 任务节点1(id:userTask1,name:任务节点1)->连线2(id:flow2,name:任务节点1->任务节点2)
		// 任务节点2(id:userTask2,name:任务节点2)->连线3(id:flow3,name:任务节点2->结束节点)结束节点(id:endEvent,name:结束节点)
		// 开始节点->任务节点1
		SequenceFlow flow1 = new SequenceFlow();
		flow1.setId("flow1");
		flow1.setName("开始节点->任务节点1");
		flow1.setSourceRef("start1");
		flow1.setTargetRef("userTask1");
		// 任务节点1->任务节点2
		SequenceFlow flow2 = new SequenceFlow();
		flow2.setId("flow2");
		flow2.setName("任务节点1->任务节点2");
		flow2.setSourceRef("userTask1");
		flow2.setTargetRef("userTask2");

		// 任务节点1->任务节点2
		SequenceFlow flow3 = new SequenceFlow();
		flow3.setId("flow3");
		flow3.setName("任务节点2->结束节点");
		flow3.setSourceRef("userTask2");
		flow3.setTargetRef("endEvent");

		// 声明BpmnModel对象
		BpmnModel bpmnModel = new BpmnModel();
		// 声明Process对象 一个BpmnModel可以包含多个Process对象
		Process process = new Process();
		process.setId("bpmnModelProcess");
		process.setName("BpmnModel创建流程");
		// 开始节点的封装
		StartEvent start = new StartEvent();
		start.setName("开始节点");
		start.setId("start1");
		start.setOutgoingFlows(Arrays.asList(flow1));
		// 任务节点1
		UserTask userTask1 = new UserTask();
		userTask1.setName("任务节点1");
		userTask1.setId("userTask1");
		userTask1.setIncomingFlows(Arrays.asList(flow1));
		userTask1.setOutgoingFlows(Arrays.asList(flow2));
		// 任务节点2
		UserTask userTask2 = new UserTask();
		userTask2.setName("任务节点2");
		userTask2.setId("userTask2");
		userTask2.setIncomingFlows(Arrays.asList(flow2));
		userTask2.setOutgoingFlows(Arrays.asList(flow3));
		// 结束节点
		EndEvent endEvent = new EndEvent();
		endEvent.setName("结束节点");
		endEvent.setId("endEvent");
		endEvent.setIncomingFlows(Arrays.asList(flow3));
		// 将所有的FlowElement添加到process中
		process.addFlowElement(start);
		process.addFlowElement(flow1);
		process.addFlowElement(userTask1);
		process.addFlowElement(flow2);
		process.addFlowElement(userTask2);
		process.addFlowElement(flow3);
		process.addFlowElement(endEvent);
		bpmnModel.addProcess(process);
		return bpmnModel;
	}
	
	@Test
	public void getDeployments() {
		List<ProcessDefinition> processList = processEngine.getRepositoryService().createProcessDefinitionQuery().list();
		for(ProcessDefinition process : processList){
			System.out.println(process.getResourceName());
		}
		List<Deployment> deployments = processEngine.getRepositoryService().createDeploymentQuery().list();
		for(Deployment deployment : deployments){
			System.out.println(deployment.getName());
		}
	}

	/**
	 * 根据文件的路径获取文件中的内容
	 * 
	 * @param filePath
	 * @return
	 */
	private String readTxtFile(String filePath) {
		StringBuffer stringBuffer = new StringBuffer();
		InputStreamReader read = null;
		try {
			String encoding = "UTF-8";// UTF-8编码
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					stringBuffer.append(lineTxt);
				}
				return stringBuffer.toString();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
		} finally {
			try {
				read.close();
			} catch (IOException e) {
			}
		}
		return "";
	}
}

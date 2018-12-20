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
	 * ·����Դ��ʽ����
	 */
	@Test
	public void deployByClasspathResource() {
		Deployment deployment = processEngine.getRepositoryService()// �����̶���Ͳ��������ص�Service
				.createDeployment()// ����һ���������
				.name("deployByClasspathResource")// ��Ӳ�������
				.addClasspathResource("com/zyf/activiti/diagrams/deployByClasspathResource.bpmn")// ��classpath����Դ�м��أ�һ��ֻ�ܼ���һ���ļ�
				.addClasspathResource("com/zyf/activiti/diagrams/deployByClasspathResource.png")
				.deploy();// ��ɲ���
		System.out.println("----deployByClasspathResource----");
		System.out.println("����ID��" + deployment.getId());
		System.out.println("��������:" + deployment.getName());

	}

	/**
	 * �ı���ʽ�������� �Ȱ��ı�����ת��Ϊstring �ٽ��в���
	 */
	@Test
	public void deployByString() {
		String resourceName = "deployByString.bpmn";
		// ��ȡ�ļ���ȡ�ļ��ж����xml��Ϣ
		String text = readTxtFile(
				"D:/04.WorkPlace/activiti/zong-first-activiti/src/main/resources/com/zyf/activiti/diagrams/deployByString.bpmn");
		// ����DeploymentBuilder����
		DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService().createDeployment()
				.addString(resourceName, text).name("deployByString");
		// ����
		Deployment deployment = deploymentBuilder.deploy();

		System.out.println("----deployByString----");
		System.out.println("����ID��" + deployment.getId());
		System.out.println("��������:" + deployment.getName());
	}
	
	@Test
	public void deployByInputStream() throws IOException {
		String resourceName = "deployByInputStream.bpmn";
		// ������ļ���Ϣ������ȡ
		InputStream inputStream = DeploymentBuilderTest.class.getClassLoader()
				.getResource("com/zyf/activiti/diagrams/deployByInputStream.bpmn").openStream();
		// ����DeploymentBuilder����
		DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService()
				.createDeployment().name("deployByInputStream")
				.addInputStream(resourceName, inputStream);
		// ����
		Deployment deployment = deploymentBuilder.deploy();
		
		System.out.println("----deployByInputStream----");
		System.out.println("����ID��" + deployment.getId());
		System.out.println("��������:" + deployment.getName());

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
		System.out.println("����ID��" + deployment.getId());
		System.out.println("��������:" + deployment.getName());
	}

	private BpmnModel createBpmnModel() {
		// ע�����е�setId�е�id������ȫ��Ψһ�Ĳ����ظ�
		// 2.setSourceRef��setTargetRef�е�ֵ������idֵ������nameֵ
		// ���̵Ķ����ǣ���ʼ�ڵ�(id:start1,name:��ʼ�ڵ�)-->����1(id:flow1,name:��ʼ�ڵ�->����ڵ�1)
		// ����ڵ�1(id:userTask1,name:����ڵ�1)->����2(id:flow2,name:����ڵ�1->����ڵ�2)
		// ����ڵ�2(id:userTask2,name:����ڵ�2)->����3(id:flow3,name:����ڵ�2->�����ڵ�)�����ڵ�(id:endEvent,name:�����ڵ�)
		// ��ʼ�ڵ�->����ڵ�1
		SequenceFlow flow1 = new SequenceFlow();
		flow1.setId("flow1");
		flow1.setName("��ʼ�ڵ�->����ڵ�1");
		flow1.setSourceRef("start1");
		flow1.setTargetRef("userTask1");
		// ����ڵ�1->����ڵ�2
		SequenceFlow flow2 = new SequenceFlow();
		flow2.setId("flow2");
		flow2.setName("����ڵ�1->����ڵ�2");
		flow2.setSourceRef("userTask1");
		flow2.setTargetRef("userTask2");

		// ����ڵ�1->����ڵ�2
		SequenceFlow flow3 = new SequenceFlow();
		flow3.setId("flow3");
		flow3.setName("����ڵ�2->�����ڵ�");
		flow3.setSourceRef("userTask2");
		flow3.setTargetRef("endEvent");

		// ����BpmnModel����
		BpmnModel bpmnModel = new BpmnModel();
		// ����Process���� һ��BpmnModel���԰������Process����
		Process process = new Process();
		process.setId("bpmnModelProcess");
		process.setName("BpmnModel��������");
		// ��ʼ�ڵ�ķ�װ
		StartEvent start = new StartEvent();
		start.setName("��ʼ�ڵ�");
		start.setId("start1");
		start.setOutgoingFlows(Arrays.asList(flow1));
		// ����ڵ�1
		UserTask userTask1 = new UserTask();
		userTask1.setName("����ڵ�1");
		userTask1.setId("userTask1");
		userTask1.setIncomingFlows(Arrays.asList(flow1));
		userTask1.setOutgoingFlows(Arrays.asList(flow2));
		// ����ڵ�2
		UserTask userTask2 = new UserTask();
		userTask2.setName("����ڵ�2");
		userTask2.setId("userTask2");
		userTask2.setIncomingFlows(Arrays.asList(flow2));
		userTask2.setOutgoingFlows(Arrays.asList(flow3));
		// �����ڵ�
		EndEvent endEvent = new EndEvent();
		endEvent.setName("�����ڵ�");
		endEvent.setId("endEvent");
		endEvent.setIncomingFlows(Arrays.asList(flow3));
		// �����е�FlowElement��ӵ�process��
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
	 * �����ļ���·����ȡ�ļ��е�����
	 * 
	 * @param filePath
	 * @return
	 */
	private String readTxtFile(String filePath) {
		StringBuffer stringBuffer = new StringBuffer();
		InputStreamReader read = null;
		try {
			String encoding = "UTF-8";// UTF-8����
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // �ж��ļ��Ƿ����
				read = new InputStreamReader(new FileInputStream(file), encoding);// ���ǵ������ʽ
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					stringBuffer.append(lineTxt);
				}
				return stringBuffer.toString();
			} else {
				System.out.println("�Ҳ���ָ�����ļ�");
			}
		} catch (Exception e) {
			System.out.println("��ȡ�ļ����ݳ���");
		} finally {
			try {
				read.close();
			} catch (IOException e) {
			}
		}
		return "";
	}
}

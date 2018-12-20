package com.zyf.activiti.engine.first;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.junit.Test;


public class ProcessEngineTest {

	/**
	 * ��ȡĬ�ϵ�����ͨ�� classpath�µ�activiti.cfg.xml
	 */
	@Test
	public void getDefaultProcessEngine() {
		ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();
		System.out.println(pe.getName());
		TaskService taskService = pe.getTaskService();
		assertNotNull(taskService);
	}
	
	/**
	 * ��ȡָ��·���µ���������
	 */
	@Test
	public void buildProcessEngineByInputStream() {

		InputStream in = ProcessEngineTest.class.getClassLoader()
				.getResourceAsStream("com/zyf/activiti/activiti.cfg.xml");
		ProcessEngineConfiguration pcf = ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(in);
		ProcessEngine processEngine = pcf.buildProcessEngine();
		System.out.println(processEngine.getName());
	}
	
	/**
	 * ��ȡָ��·����ָ��bean����������
	 */
	@Test
	public void buildProcessEngineByInputStream2() {

		InputStream in = ProcessEngineTest.class.getClassLoader()
				.getResourceAsStream("com/zyf/activiti/activiti.cfg.xml");
		ProcessEngineConfiguration pcf = ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(in, "processEngineConfigurationDefault");
		ProcessEngine processEngine = pcf.buildProcessEngine();
		System.out.println(processEngine.getName());
	}
	
	/**
	 * ��̷�ʽ������׼��������
	 */
	@Test
	public void buildProcessEngine() {
	
		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
				.createStandaloneProcessEngineConfiguration();
		// ���ݿ�������Ϣ
		processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
		// ���ӵ����ݿ������ַ���
		processEngineConfiguration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti");
		// ���ӵ����ݿ��û���
		processEngineConfiguration.setJdbcUsername("root");
		// ���ӵ����ݿ�����
		processEngineConfiguration.setJdbcPassword("root123");
		
		//processEngineConfiguration.setDatabaseSchemaUpdate("drop-create");
		// �������ĺ��Ķ���ProcessEnginee����
		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
		System.out.println(processEngine.getProcessEngineConfiguration().getDatabaseSchemaUpdate());
	}
}

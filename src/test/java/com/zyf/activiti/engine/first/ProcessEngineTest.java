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
	 * 获取默认的引擎通过 classpath下的activiti.cfg.xml
	 */
	@Test
	public void getDefaultProcessEngine() {
		ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();
		System.out.println(pe.getName());
		TaskService taskService = pe.getTaskService();
		assertNotNull(taskService);
	}
	
	/**
	 * 获取指定路径下的流程引擎
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
	 * 获取指定路径下指定bean的流程引擎
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
	 * 编程方式创建标准流程引擎
	 */
	@Test
	public void buildProcessEngine() {
	
		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
				.createStandaloneProcessEngineConfiguration();
		// 数据库驱动信息
		processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
		// 连接的数据库连接字符串
		processEngineConfiguration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti");
		// 连接的数据库用户名
		processEngineConfiguration.setJdbcUsername("root");
		// 连接的数据库密码
		processEngineConfiguration.setJdbcPassword("root123");
		
		//processEngineConfiguration.setDatabaseSchemaUpdate("drop-create");
		// 工作流的核心对象，ProcessEnginee对象
		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
		System.out.println(processEngine.getProcessEngineConfiguration().getDatabaseSchemaUpdate());
	}
}

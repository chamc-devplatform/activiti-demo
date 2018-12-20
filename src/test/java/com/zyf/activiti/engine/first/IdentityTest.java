package com.zyf.activiti.engine.first;

import org.activiti.engine.IdentityService;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class IdentityTest {

	@Rule
	ActivitiRule activitiRule = new ActivitiRule();
	
	@Test
	public void testUser() {
		IdentityService identityService = activitiRule.getIdentityService();
		identityService.createUserQuery().userId("Zongaaaa").singleResult();
	}
}

package com.zyf.activiti;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.util.IoUtil;
import org.springframework.beans.factory.annotation.Autowired;


public class DataInit {

	@Autowired
    protected IdentityService identityService;
	
	@PostConstruct
    public void init() {
		System.out.println("init groups");
		initDemoGroups();
		System.out.println("init users");
		initDemoUsers();
	}
	
	 protected void initDemoGroups() {
	        String[] assignmentGroups = new String[]{"deptLeader", "hr"};
	        for (String groupId : assignmentGroups) {
	            createGroup(groupId, "assignment");
	        }

	        String[] securityGroups = new String[]{"user", "admin"};
	        for (String groupId : securityGroups) {
	            createGroup(groupId, "security-role");
	        }
	    }

	    protected void createGroup(String groupId, String type) {
	        if (identityService.createGroupQuery().groupId(groupId).count() == 0) {
	            Group newGroup = identityService.newGroup(groupId);
	            newGroup.setName(groupId.substring(0, 1).toUpperCase() + groupId.substring(1));
	            newGroup.setType(type);
	            identityService.saveGroup(newGroup);
	        }
	    }

	    protected void initDemoUsers() {
	        createUser("Zongaaaa", "aaaa", "Zong", "000000", "aaaa.zong@kafeitu.me",
	                "", Arrays.asList("user", "admin"), null);

	        createUser("Zhangcccc", "cccc", "Zhang", "000000", "cccc.zhang@kafeitu.me",
	                "", Arrays.asList("hr", "user"), null);

	        createUser("Lieeee", "eeee", "Li", "000000", "eeee.li@kafeitu.me",
	                "", Arrays.asList("deptLeader", "user"), null);

	        createUser("Tangffff", "ffff", "Tang", "000000", "ffff.tang@kafeitu.me",
	                "", Arrays.asList("user", "admin"), null);
	    }

	    protected void createUser(String userId, String firstName, String lastName, String password,
	                              String email, String imageResource, List<String> groups, List<String> userInfo) {

	        if (identityService.createUserQuery().userId(userId).count() == 0) {

	            // Following data can already be set by demo setup script

	            User user = identityService.newUser(userId);
	            user.setFirstName(firstName);
	            user.setLastName(lastName);
	            user.setPassword(password);
	            user.setEmail(email);
	            identityService.saveUser(user);

	            if (groups != null) {
	                for (String group : groups) {
	                    identityService.createMembership(userId, group);
	                }
	            }
	        }

	        // Following data is not set by demo setup script

	        // image
	        if (imageResource != null) {
	            byte[] pictureBytes = IoUtil.readInputStream(this.getClass().getClassLoader().getResourceAsStream(imageResource), null);
	            Picture picture = new Picture(pictureBytes, "image/jpeg");
	            identityService.setUserPicture(userId, picture);
	        }

	        // user info
	        if (userInfo != null) {
	            for (int i = 0; i < userInfo.size(); i += 2) {
	                identityService.setUserInfo(userId, userInfo.get(i), userInfo.get(i + 1));
	            }
	        }

	    }
}

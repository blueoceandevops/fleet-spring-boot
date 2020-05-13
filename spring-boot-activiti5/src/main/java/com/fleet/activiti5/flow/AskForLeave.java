//package com.fleet.activiti.flow;
//
//import org.activiti.engine.ProcessEngine;
//import org.activiti.engine.ProcessEngines;
//import org.activiti.engine.repository.Deployment;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class AskForLeave {
//	private  ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
//	
//	@Test
//	public void testDeploy() {
//		Deployment deployment = processEngine.getRepositoryService()
//				.createDeployment()
//				.addClasspathResource("processes/AskForLeave.bpmn")
//				.addClasspathResource("processes/AskForLeave.png")
//				.name("请假流程")
//				.deploy();
//		System.out.println(deployment.getId());
//		System.out.println(deployment.getName());
//	}
//
//}

//package com.fleet.activiti5;
//
//import com.fleet.activiti5.service.ProcessService;
//import com.fleet.entity.activiti.Approval;
//import com.fleet.entity.activiti.ProcessInfo;
//import com.fleet.entity.activiti.Turn;
//import com.fleet.util.jdbc.entity.Page;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class Activiti5ApplicationTests {
//
//    @Autowired
//    ProcessService processService;
//
//    @Test
//    public void myAppliedList() {
//        String userId = "1";
//        Page page = new Page();
//        page.setTotalRows(21);
//        page.setPageRows(10);
//        page.setPageIndex(2);
//        processService.myAppliedList(userId, page);
//    }
//
//    // @Test
//    public void myApprovedList() {
//        String userId = "1";
//        Page page = new Page();
//        processService.myApprovedList(userId, page);
//    }
//
//    // @Test
//    public void start() {
//        ProcessInfo<List<Integer>> processInfo = new ProcessInfo<>();
//        processInfo.setDefinitionKey("AskForLeave");
//        processInfo.setBusinessKey("AskForLeave:1");
//        processInfo.setProcessName("这是请假流程");
//        processInfo.setInitiator("1");
//        processInfo.setPhone("11111");
//        processInfo.setEmail("1222");
//        List<Integer> details = new ArrayList<>();
//        details.add(2);
//        details.add(5);
//        processInfo.setDetails(details);
//        processInfo.setRemark("这是测试");
//
//        Map<String, String> assignees = new HashMap<>();
//        assignees.put("填写申请单", "1");
//        assignees.put("领导审批", "1");
//        processInfo.setAssignees(assignees);
//        processService.start(processInfo);
//    }
//
//    // @Test
//    public void apply() {
//        ProcessInfo<List<Integer>> processInfo = new ProcessInfo<>();
//        processInfo.setDefinitionKey("AskForLeave");
//        processInfo.setBusinessKey("AskForLeave:4");
//        processInfo.setProcessName("这是请假流程");
//        processInfo.setInitiator("1");
//        processInfo.setPhone("11111");
//        processInfo.setEmail("1222");
//        List<Integer> details = new ArrayList<>();
//        details.add(2);
//        details.add(5);
//        processInfo.setDetails(details);
//        processInfo.setRemark("这是测试");
//
//        Map<String, String> assignees = new HashMap<>();
//        assignees.put("填写申请单", "1");
//        assignees.put("领导审批", "2");
//        processInfo.setAssignees(assignees);
//        processService.apply(processInfo);
//    }
//
//    // @Test
//    public void myTaskList() {
//        // processService.myTaskList(1);
//    }
//
//    // @Test
//    public void completeTask() {
//        Approval approval = new Approval();
//        approval.setFlag("提交");
//        approval.setTaskId("7510");
//        approval.setRemark("这是提交");
//        processService.completeTask(approval);
//
//        // Approval approval = new Approval();
//        // approval.setFlag("重新提交");
//        // approval.setTaskId("17505");
//        // approval.setRemark("这是重新提交");
//        // processService.completeTask(approval);
//
//        // Approval approval = new Approval();
//        // approval.setFlag("驳回");
//        // approval.setTaskId("15004");
//        // approval.setRemark("这是驳回");
//        // processService.completeTask(approval);
//    }
//
//    // @Test
//    public void stop() {
//        processService.stop("AskForLeave:3");
//    }
//
//    // @Test
//    public void getByBusinessKey() {
//        processService.getByBusinessKey("AskForLeave:2");
//    }
//
//    // @Test
//    public void getByTaskId() {
//        processService.getByTaskId("5007");
//    }
//
//    // @Test
//    public void getApprovalLog() {
//        processService.getApprovalLog("AskForLeave:2");
//    }
//
//    // @Test
//    public void turnTask() {
//        Turn turn = new Turn();
//        turn.setTaskId("11");
//        turn.setAssignee("2");
//        turn.setRemark("这是转交测试");
//        processService.turnTask(turn);
//    }
//
//    // @Test
//    public void delegateTask() {
//        Turn turn = new Turn();
//        turn.setTaskId("17507");
//        turn.setAssignee("2");
//        turn.setRemark("这是委派测试");
//        processService.delegateTask(turn);
//    }
//
//    // @Test
//    public void resolveTask() {
//        Turn turn = new Turn();
//        turn.setTaskId("17507");
//        turn.setRemark("这是委派完成测试");
//        processService.resolveTask(turn);
//    }
//
//    // @Test
//    public void suspendProcess() {
//        processService.suspendProcess("AskForLeave:2");
//    }
//
//    // @Test
//    public void activateProcess() {
//        processService.activateProcess("AskForLeave:3");
//    }
//
//}

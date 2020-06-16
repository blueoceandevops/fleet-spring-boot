package com.fleet.activiti6;

import com.alibaba.fastjson.JSON;
import com.fleet.activiti6.entity.*;
import com.fleet.activiti6.page.Page;
import com.fleet.activiti6.page.PageUtil;
import com.fleet.activiti6.service.ProcessService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Activiti6ApplicationTests {

    @Resource
    ProcessService processService;

    @Test
    public void myTaskList() {
        String userId = "2";
        Page page = new Page();
        page.setPageIndex(1);
        page.setPageRows(10);
        PageUtil<TaskInfo<?>> pageUtil = processService.myTaskList(userId, page);
        System.out.println(JSON.toJSONString(pageUtil));
    }

    @Test
    public void myAppliedList() {
        String userId = "1";
        Page page = new Page();
        page.setPageIndex(1);
        page.setPageRows(10);
        processService.myAppliedList(userId, page);
    }

    @Test
    public void myApprovedList() {
        String userId = "1";
        Page page = new Page();
        page.setPageIndex(1);
        page.setPageRows(10);
        processService.myApprovedList(userId, page);
    }

    @Test
    public void start() {
        ProcessInfo<List<Integer>> processInfo = new ProcessInfo<>();
        processInfo.setProcessDefinitionKey("AskForLeave");
        processInfo.setBusinessKey("AskForLeave:1");
        processInfo.setProcessName("这是请假流程");
        processInfo.setInitiator("1");
        processInfo.setPhone("11111");
        processInfo.setEmail("1222");
        List<Integer> details = new ArrayList<>();
        details.add(1);
        details.add(3);
        processInfo.setDetails(details);
        processInfo.setRemark("这是测试");

        Map<String, String> assignees = new HashMap<>();
        assignees.put("填写申请单", "1");
        assignees.put("领导审批", "1");
        processInfo.setAssignees(assignees);
        processService.start(processInfo);
    }

    @Test
    public void apply() {
        ProcessInfo<List<Integer>> processInfo = new ProcessInfo<>();
        processInfo.setProcessDefinitionKey("AskForLeave");
        processInfo.setBusinessKey("AskForLeave:1");
        processInfo.setProcessName("这是请假流程");
        processInfo.setInitiator("1");
        processInfo.setPhone("11111");
        processInfo.setEmail("1222");
        List<Integer> details = new ArrayList<>();
        details.add(1);
        details.add(3);
        processInfo.setDetails(details);
        processInfo.setRemark("这是测试");

        Map<String, String> assignees = new HashMap<>();
        assignees.put("填写申请单", "1");
        assignees.put("领导审批", "2");
        processInfo.setAssignees(assignees);
        processService.apply(processInfo);
    }

    @Test
    public void completeTask() {
//        Approval approval = new Approval();
//        approval.setFlag("提交");
//        approval.setTaskId("7510");
//        approval.setRemark("这是提交");
//        processService.completeTask(approval);

        // Approval approval = new Approval();
        // approval.setFlag("重新提交");
        // approval.setTaskId("17505");
        // approval.setRemark("这是重新提交");
        // processService.completeTask(approval);

        Approval approval = new Approval();
        approval.setFlag("驳回");
        approval.setTaskId("2520");
        approval.setRemark("驳回");
        processService.completeTask(approval);
    }

    @Test
    public void stop() {
        processService.stop("AskForLeave:1");
    }

    @Test
    public void getByBusinessKey() {
        ProcessInfo<?> processInfo = processService.getByBusinessKey("AskForLeave:1");
        System.out.println(JSON.toJSONString(processInfo));
    }

    @Test
    public void getByProcessInstanceId() {
        ProcessInfo<?> processInfo = processService.getByProcessInstanceId("5007");
        System.out.println(JSON.toJSONString(processInfo));
    }

    @Test
    public void getByTaskId() {
        ProcessInfo<?> processInfo = processService.getByTaskId("23");
        System.out.println(JSON.toJSONString(processInfo));
    }

    @Test
    public void getTaskOperation() {
        List<String> taskOperationList = processService.getTaskOperation("23");
        System.out.println(JSON.toJSONString(taskOperationList));
    }

    @Test
    public void getApprovalLog() {
        List<ApprovalLog> approvalLogList = processService.getApprovalLog("AskForLeave:1");
        System.out.println(JSON.toJSONString(approvalLogList));
    }

    @Test
    public void turnTask() {
        Turn turn = new Turn();
        turn.setTaskId("23");
        turn.setAssignee("2");
        turn.setRemark("转交");
        processService.turnTask(turn);
    }

    @Test
    public void delegateTask() {
        Turn turn = new Turn();
        turn.setTaskId("17507");
        turn.setAssignee("2");
        turn.setRemark("委派");
        processService.delegateTask(turn);
    }

    @Test
    public void resolveTask() {
        Turn turn = new Turn();
        turn.setTaskId("17507");
        turn.setRemark("委派完成");
        processService.resolveTask(turn);
    }

    @Test
    public void suspendProcess() {
        processService.suspendProcess("AskForLeave:1");
    }

    @Test
    public void activateProcess() {
        processService.activateProcess("AskForLeave:1");
    }
}

package com.fleet.activiti5.controller;

import com.fleet.activiti5.entity.Approval;
import com.fleet.activiti5.entity.ProcessInfo;
import com.fleet.activiti5.entity.TaskInfo;
import com.fleet.activiti5.page.Page;
import com.fleet.activiti5.page.PageUtil;
import com.fleet.activiti5.service.ProcessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/process")
public class ProcessController {

    @Resource
    private ProcessService processService;

    /**
     * 我的待办列表
     */
    @RequestMapping("/myTaskList")
    public PageUtil<TaskInfo<?>> myTaskList(String userId, Page page) {
        return processService.myTaskList(userId, page);
    }

    /**
     * 我的申请列表
     */
    @RequestMapping("/myAppliedList")
    public PageUtil<ProcessInfo<?>> myAppliedList(String userId, Page page) {
        return processService.myAppliedList(userId, page);
    }

    /**
     * 我的审批列表
     */
    @RequestMapping("/myApprovedList")
    public PageUtil<ProcessInfo<?>> myApprovedList(String userId, Page page) {
        return processService.myApprovedList(userId, page);
    }

    /**
     * 创建流程实例
     */
    @RequestMapping("/start")
    public TaskInfo<?> start(ProcessInfo<List<Integer>> processInfo) {
        return processService.start(processInfo);
    }

    /**
     * 流程申请（创建流程实例并且自动完成 apply 填写申请单节点）
     */
    @RequestMapping("/apply")
    public String apply(ProcessInfo<List<Integer>> processInfo) {
        return processService.apply(processInfo);
    }

    @RequestMapping("/completeTask")
    public void completeTask(@RequestBody Approval approval) {
        processService.completeTask(approval);
    }

    @RequestMapping("/getTaskOperation")
    public List<String> getTaskOperation(String taskId) {
        return processService.getTaskOperation(taskId);
    }

    @RequestMapping("/processImage")
    public void processImage(String processDefinitionKey, HttpServletResponse response) {
        ResponseEntity<byte[]> responseEntity = processService.getProcessImage(processDefinitionKey);
        try {
            byte data[] = responseEntity.getBody();
            OutputStream out = response.getOutputStream();
            out.write(data);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    @RequestMapping("/processRateImage")
    public void processRateImage(String businessKey, HttpServletResponse response) {
        ResponseEntity<byte[]> responseEntity = processService.getProcessRateImage(businessKey);
        try {
            byte data[] = responseEntity.getBody();
            OutputStream out = response.getOutputStream();
            out.write(data);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}

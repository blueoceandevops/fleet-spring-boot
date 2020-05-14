package com.fleet.activiti5.service;

import com.fleet.activiti5.entity.*;
import com.fleet.activiti5.page.Page;
import com.fleet.activiti5.page.PageUtil;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProcessService {

    /**
     * 我的待办列表
     */
    PageUtil<TaskInfo<?>> myTaskList(String userId, Page page);

    /**
     * 我的申请列表
     */
    PageUtil<ProcessInfo<?>> myAppliedList(String userId, Page page);

    /**
     * 我的审批列表
     */
    PageUtil<ProcessInfo<?>> myApprovedList(String userId, Page page);

    /**
     * 创建流程实例
     */
    TaskInfo<?> start(ProcessInfo<?> processInfo);

    /**
     * 流程申请（创建流程实例并且自动完成 apply 填写申请单节点）
     */
    String apply(ProcessInfo<?> processInfo);

    /**
     * 完成当前节点审批
     */
    void completeTask(Approval approval);

    /**
     * 流程中止
     */
    void stop(String businessKey);

    /**
     * 流程详情
     */
    ProcessInfo<?> getByBusinessKey(String businessKey);

    /**
     * 流程详情
     */
    ProcessInfo<?> getByProcessId(String processInstanceId);

    /**
     * 流程详情
     */
    ProcessInfo<?> getByTaskId(String taskId);

    /**
     * 获取节点操作
     */
    List<String> getTaskOperation(String taskId);

    /**
     * 流程图
     */
    ResponseEntity<byte[]> getProcessImage(String processDefinitionKey);

    /**
     * 流程图（附加进度）
     */
    ResponseEntity<byte[]> getProcessRateImage(String businessKey);

    /**
     * 获取流程审批记录
     */
    List<ApprovalLog> getApprovalLog(String businessKey);

    /**
     * 转交任务
     */
    void turnTask(Turn turn);

    /**
     * 委派任务
     */
    String delegateTask(Turn turn);

    /**
     * 委派人处理任务
     */
    String resolveTask(Turn turn);

    /**
     * 流程挂起
     */
    String suspendProcess(String businessKey);

    /**
     * 流程激活
     */
    String activateProcess(String businessKey);
}

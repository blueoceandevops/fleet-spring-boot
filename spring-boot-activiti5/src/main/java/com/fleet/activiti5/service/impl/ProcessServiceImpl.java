package com.fleet.activiti5.service.impl;

import com.fleet.activiti5.entity.*;
import com.fleet.activiti5.page.Page;
import com.fleet.activiti5.page.PageUtil;
import com.fleet.activiti5.service.ProcessService;
import org.activiti.bpmn.model.*;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;

@Transactional
@Service("processService")
public class ProcessServiceImpl implements ProcessService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    HistoryService historyService;

    @Resource
    IdentityService identityService;

    @Resource
    RepositoryService repositoryService;

    @Resource
    ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public PageUtil<TaskInfo<?>> myTaskList(String userId, Page page) {
        PageUtil<TaskInfo<?>> pageUtil = new PageUtil<>();
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(userId)
                .orderByTaskCreateTime().asc()
                .listPage(page.getFromPageIndex(), page.getPageRows());

        List<TaskInfo<?>> taskInfoList = new ArrayList<>();
        if (taskList != null) {
            for (Task task : taskList) {
                TaskInfo<?> taskInfo = getTaskInfo(task);
                taskInfoList.add(taskInfo);
            }
        }

        long count = taskService.createTaskQuery()
                .taskAssignee(userId)
                .count();

        pageUtil.setList(taskInfoList);
        page.setTotalRows((int) count);
        pageUtil.setPage(page);
        return pageUtil;
    }

    private TaskInfo<?> getTaskInfo(Task task) {
        String taskId = task.getId();
        String processInstanceId = task.getProcessInstanceId();

        String initiator = (String) taskService.getVariable(taskId, "initiator");
        ProcessInfo<?> processInfo = (ProcessInfo<?>) taskService.getVariable(taskId, "info");

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (processInstance == null) {
            return null;
        }

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (historicProcessInstance == null) {
            return null;
        }

        // 将 task 转换成我们需要的格式 taskInfo
        TaskInfo<?> taskInfo = new TaskInfo<>();
        taskInfo.setProcessInstanceId(processInstanceId);
        taskInfo.setProcessDefinitionKey(processInfo.getProcessDefinitionKey());
        taskInfo.setBusinessKey(processInstance.getBusinessKey());
        taskInfo.setProcessName(processInfo.getProcessName());
        taskInfo.setInitiator(initiator);
        taskInfo.setPhone(processInfo.getPhone());
        taskInfo.setEmail(processInfo.getEmail());
        taskInfo.setStartTime(historicProcessInstance.getStartTime());
        taskInfo.setRemark(processInfo.getRemark());
        taskInfo.setTaskId(taskId);
        taskInfo.setTaskName(task.getName());
        taskInfo.setCreateTime(task.getCreateTime());
        taskInfo.setAssignee(task.getAssignee());
        return taskInfo;
    }

    @Override
    public PageUtil<ProcessInfo<?>> myAppliedList(String userId, Page page) {
        PageUtil<ProcessInfo<?>> pageUtil = new PageUtil<>();
        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery()
                .variableValueEquals("initiator", userId)
                .orderByProcessInstanceStartTime().desc()
                .listPage(page.getFromPageIndex(), page.getPageRows());

        List<ProcessInfo<?>> processInfoList = new ArrayList<>();
        if (historicProcessInstanceList != null) {
            for (HistoricProcessInstance historicProcessInstance : historicProcessInstanceList) {
                ProcessInfo<?> processInfo = getProcessInfo(historicProcessInstance);
                processInfoList.add(processInfo);
            }
        }

        long count = historyService.createHistoricProcessInstanceQuery()
                .variableValueEquals("initiator", userId)
                .count();

        pageUtil.setList(processInfoList);
        page.setTotalRows((int) count);
        pageUtil.setPage(page);
        return pageUtil;
    }

    private ProcessInfo<?> getProcessInfo(HistoricProcessInstance historicProcessInstance) {
        String processInstanceId = historicProcessInstance.getId();
        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName("info")
                .singleResult();
        if (historicVariableInstance == null) {
            return null;
        }

        ProcessInfo<?> processInfo = (ProcessInfo<?>) historicVariableInstance.getValue();
        if (historicProcessInstance.getEndTime() == null) {
            processInfo.setState(1);
            Task task = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            if (task != null) {
                processInfo.setAssignee(task.getAssignee());
            }
        } else {
            // 查询流程是否终止
            HistoricVariableInstance terminated = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .variableName("terminated")
                    .singleResult();
            if (terminated != null) {
                processInfo.setState(3);
            } else {
                processInfo.setState(2);
            }
        }

        processInfo.setProcessInstanceId(processInstanceId);
        processInfo.setStartTime(historicProcessInstance.getStartTime());
        processInfo.setEndTime(historicProcessInstance.getEndTime());
        return processInfo;
    }

    @Override
    public PageUtil<ProcessInfo<?>> myApprovedList(String userId, Page page) {
        PageUtil<ProcessInfo<?>> pageUtil = new PageUtil<>();
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .taskAssignee(userId)
                .activityType("userTask")
                .finished()
                .orderByHistoricActivityInstanceStartTime().desc()
                .listPage(page.getFromPageIndex(), page.getPageRows());

        List<ProcessInfo<?>> processInfoList = new ArrayList<>();
        if (historicActivityInstanceList != null) {
            for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
                ProcessInfo<?> processInfo = getProcessInfo(historicActivityInstance);
                processInfoList.add(processInfo);
            }
        }

        long count = historyService.createHistoricActivityInstanceQuery()
                .taskAssignee(userId)
                .activityType("userTask")
                .finished()
                .count();

        pageUtil.setList(processInfoList);
        page.setTotalRows((int) count);
        pageUtil.setPage(page);
        return pageUtil;
    }

    private ProcessInfo<?> getProcessInfo(HistoricActivityInstance historicActivityInstance) {
        String processInstanceId = historicActivityInstance.getId();
        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName("info")
                .singleResult();
        if (historicVariableInstance == null) {
            return null;
        }

        ProcessInfo<?> processInfo = (ProcessInfo<?>) historicVariableInstance.getValue();
        if (historicActivityInstance.getEndTime() == null) {
            processInfo.setState(1);
            Task task = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            if (task != null) {
                processInfo.setAssignee(task.getAssignee());
            }
        } else {
            // 查询流程是否终止
            HistoricVariableInstance terminated = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .variableName("terminated")
                    .singleResult();
            if (terminated != null) {
                processInfo.setState(3);
            } else {
                processInfo.setState(2);
            }
        }

        processInfo.setProcessInstanceId(processInstanceId);
        processInfo.setStartTime(historicActivityInstance.getStartTime());
        processInfo.setEndTime(historicActivityInstance.getEndTime());
        return processInfo;
    }

    @Override
    public Long getTotal(String processDefinitionKey) {
        return historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .count();
    }

    @Override
    public TaskInfo<?> start(ProcessInfo<?> processInfo) {
        // 先判断business的唯一性
        String businessKey = processInfo.getBusinessKey();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        if (historicProcessInstance != null) {
            return null;
        }

        identityService.setAuthenticatedUserId(processInfo.getInitiator());

        Map<String, Object> vars = new HashMap<>();
        vars.put("info", processInfo);
        vars.put("initiator", processInfo.getInitiator());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processInfo.getProcessDefinitionKey(), processInfo.getBusinessKey(), vars);
        if (processInstance == null) {
            return null;
        }

        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        return getTaskInfo(task);
    }

    @Override
    public String apply(ProcessInfo<?> processInfo) {
        TaskInfo<?> taskInfo = start(processInfo);
        if (taskInfo == null) {
            return "失败";
        }

        Approval approval = new Approval();
        approval.setFlag("提交");
        approval.setRemark("提交");
        approval.setTaskId(taskInfo.getTaskId());
        completeTask(approval);
        return "成功";
    }

    @Override
    public String reApply(String taskId, ProcessInfo<?> processInfo) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        if (task == null) {
            return "任务不存在";
        }
        String processInstanceId = task.getProcessInstanceId();

        // 添加审批记录
        taskService.addComment(taskId, processInstanceId, "重新提交");

        Map<String, Object> vars = new HashMap<>();
        vars.put("info", processInfo);
        vars.put("操作", "提交");
        taskService.setVariablesLocal(taskId, vars);

        // 完成任务
        taskService.complete(taskId, vars);
        return "成功";
    }

    @Override
    public String completeTask(Approval approval) {
        Task task = taskService.createTaskQuery()
                .taskId(approval.getTaskId())
                .singleResult();
        if (task == null) {
            return "任务不存在";
        }
        String processInstanceId = task.getProcessInstanceId();

        // 添加审批记录
        taskService.addComment(approval.getTaskId(), processInstanceId, approval.getRemark());

        Map<String, Object> vars = new HashMap<>();
        vars.put("操作", approval.getFlag());
        taskService.setVariablesLocal(approval.getTaskId(), vars);

        // 完成任务
        taskService.complete(approval.getTaskId(), vars);
        return "成功";
    }

    @Override
    public String resetAssignees(String businessKey, Map<String, String> assignees) {
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        if (task == null) {
            return "任务不存在";
        }
        String taskId = task.getId();
        ProcessInfo<?> processInfo = (ProcessInfo<?>) taskService.getVariable(taskId, "info");
        for (String key : assignees.keySet()) {
            processInfo.getAssignees().put(key, assignees.get(key));
        }

        Map<String, Object> vars = new HashMap<>();
        vars.put("info", processInfo);
        taskService.setVariables(taskId, vars);

        if (task.getName() != null) {
            if (assignees.containsKey(task.getName())) {
                if (assignees.get(task.getName()) != null && !assignees.get(task.getName()).equals(task.getAssignee())) {
                    Turn turn = new Turn();
                    turn.setTaskId(taskId);
                    turn.setAssignee(assignees.get(task.getName()));
                    turn.setRemark("修改审批人后转交给新的审批人");
                    turnTask(turn);
                }
            }
        }
        return "成功";
    }

    @Override
    public String stop(String businessKey) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        if (historicProcessInstance == null) {
            return "businessKey:" + businessKey + "不存在";
        }
        String processInstanceId = historicProcessInstance.getId();

        runtimeService.deleteProcessInstance(processInstanceId, "终止");
        historyService.deleteHistoricProcessInstance(processInstanceId);
        return "成功";
    }

    @Override
    public ProcessInfo<?> getByBusinessKey(String businessKey) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        if (historicProcessInstance == null) {
            return null;
        }

        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(historicProcessInstance.getId())
                .variableName("info")
                .singleResult();
        if (historicVariableInstance == null) {
            return null;
        }
        return (ProcessInfo<?>) historicVariableInstance.getValue();
    }

    @Override
    public ProcessInfo<?> getByProcessInstanceId(String processInstanceId) {
        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName("info")
                .singleResult();
        if (historicVariableInstance == null) {
            return null;
        }
        return (ProcessInfo<?>) historicVariableInstance.getValue();
    }

    @Override
    public ProcessInfo<?> getByTaskId(String taskId) {
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .taskId(taskId)
                .singleResult();
        if (historicTaskInstance == null) {
            return null;
        }

        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(historicTaskInstance.getProcessInstanceId())
                .variableName("info")
                .singleResult();
        if (historicVariableInstance == null) {
            return null;
        }
        return (ProcessInfo<?>) historicVariableInstance.getValue();
    }

    @Override
    public List<String> getTaskOperation(String taskId) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        if (task == null) {
            return new ArrayList<>();
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        if (bpmnModel == null) {
            return new ArrayList<>();
        }
        Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();
        if (flowElements == null) {
            return new ArrayList<>();
        }
        FlowElement flowElement = getFlowElement(flowElements, task.getTaskDefinitionKey());
        if (flowElement == null) {
            return new ArrayList<>();
        }
        List<SequenceFlow> outgoingFlows = ((UserTask) flowElement).getOutgoingFlows();
        if (outgoingFlows == null) {
            return new ArrayList<>();
        }
        return getSequenceFlowOperation(flowElements, outgoingFlows);
    }

    private List<String> getSequenceFlowOperation(Collection<FlowElement> flowElements, List<SequenceFlow> outgoingFlows) {
        List<String> rList = new ArrayList<>();
        for (SequenceFlow sequenceFlow : outgoingFlows) {
            rList.addAll(analyzeOperation(sequenceFlow.getConditionExpression()));
            FlowElement flowElement = getFlowElement(flowElements, sequenceFlow.getTargetRef());
            if (flowElement instanceof ExclusiveGateway) {
                ExclusiveGateway exclusiveGateway = (ExclusiveGateway) flowElement;
                rList.addAll(getSequenceFlowOperation(flowElements, exclusiveGateway.getOutgoingFlows()));
            }
        }
        return rList;
    }

    private FlowElement getFlowElement(Collection<FlowElement> flowElements, String taskDefinitionKey) {
        for (FlowElement flowElement : flowElements) {
            if (flowElement.getId().equals(taskDefinitionKey)) {
                return flowElement;
            }
        }
        return null;
    }

    // 提取条件中的用户操作
    private List<String> analyzeOperation(String condition) {
        List<String> rList = new ArrayList<>();
        if (condition != null) {
            condition = condition.substring(2, condition.length() - 1);
            String[] conditionGroup = condition.split("\\|\\||&&");
            for (String group : conditionGroup) {
                String[] pd = group.split("==");
                if ("操作".equals(pd[0].trim())) {
                    rList.add(pd[1].trim().replaceAll("'", ""));
                }
            }
        }
        return rList;
    }

    @Override
    public ResponseEntity<byte[]> getProcessImage(String processDefinitionKey) {
        ResponseEntity<byte[]> entity = null;
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .latestVersion()
                    .singleResult();
            if (processDefinition == null) {
                return null;
            }
            String diagramResourceName = processDefinition.getDiagramResourceName();
            if (diagramResourceName == null) {
                return null;
            }

            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
            ProcessDiagramGenerator processDiagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
            InputStream in = processDiagramGenerator.generateDiagram(bpmnModel, "png", Collections.<String>emptyList(), Collections.<String>emptyList(), "宋体", "宋体", null, null, 1.0);

            byte[] bytes = new byte[in.available()];
            in.read(bytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/png");
            headers.add("Connection", "close");
            headers.add("Accept-Ranges", "bytes");
            entity = new ResponseEntity<>(bytes, headers, HttpStatus.OK);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public ResponseEntity<byte[]> getProcessRateImage(String businessKey) {
        ResponseEntity<byte[]> entity = null;
        try {
            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceBusinessKey(businessKey)
                    .singleResult();
            if (processInstance == null) {
                return null;
            }

            List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .orderByHistoricActivityInstanceStartTime().asc()
                    .list();
            if (historicActivityInstanceList == null) {
                return null;
            }

            List<String> highLightedActivityIds = new ArrayList<>();
            for (HistoricActivityInstance tempActivity : historicActivityInstanceList) {
                String activityId = tempActivity.getActivityId();
                highLightedActivityIds.add(activityId);
            }

            BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
            List<String> highLightedFlows = getHighLightedFlows(bpmnModel, historicActivityInstanceList);

            ProcessDiagramGenerator processDiagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
            InputStream in = processDiagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivityIds, highLightedFlows, "宋体", "宋体", null, null, 1.0);

            byte[] bytes = new byte[in.available()];
            in.read(bytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/png");
            headers.add("Connection", "close");
            headers.add("Accept-Ranges", "bytes");
            entity = new ResponseEntity<>(bytes, headers, HttpStatus.OK);
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return entity;
    }

    /**
     * 获取需要高亮的线
     *
     * @param bpmnModel
     * @param historicActivityInstanceList
     */
    private static List<String> getHighLightedFlows(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstanceList) {
        // 高亮流程已发生流转的线id集合
        List<String> highLightedFlows = new ArrayList<>();

        // 全部活动节点
        List<FlowNode> flowNodeList = new ArrayList<>();
        // 已完成的历史活动节点
        List<HistoricActivityInstance> finishedList = new ArrayList<>();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId());
            flowNodeList.add(flowNode);
            if (historicActivityInstance.getEndTime() != null) {
                finishedList.add(historicActivityInstance);
            }
        }

        // 遍历已完成的活动实例，从每个实例的outgoingFlows中找到已执行的
        for (HistoricActivityInstance finished : finishedList) {
            // 获得当前活动对应的节点信息及 outgoingFlows 信息
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(finished.getActivityId());
            List<SequenceFlow> sequenceFlows = flowNode.getOutgoingFlows();
            // 并行网关或兼容网关
            if ("parallelGateway".equals(finished.getActivityType()) || "inclusiveGateway".equals(finished.getActivityType())) {
                // 遍历历史活动节点，找到匹配流程目标节点
                for (SequenceFlow sequenceFlow : sequenceFlows) {
                    FlowNode nextFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef());
                    if (flowNodeList.contains(nextFlowNode)) {
                        highLightedFlows.add(nextFlowNode.getId());
                    }
                }
            } else {
                for (SequenceFlow sequenceFlow : sequenceFlows) {
                    for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
                        if (historicActivityInstance.getActivityId().equals(sequenceFlow.getTargetRef())) {
                            highLightedFlows.add(sequenceFlow.getId());
                        }
                    }
                }
            }
        }
        return highLightedFlows;
    }

    @Override
    public List<ApprovalLog> getApprovalLog(String businessKey) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        if (historicProcessInstance == null) {
            return new ArrayList<>();
        }

        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(historicProcessInstance.getId())
                .activityType("userTask")
                .orderByHistoricActivityInstanceStartTime().asc()
                .list();
        if (historicActivityInstanceList == null) {
            return new ArrayList<>();
        }

        List<ApprovalLog> approvalLogList = new ArrayList<>();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            String taskId = historicActivityInstance.getTaskId();

            HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                    .taskId(taskId)
                    .variableName("turnLog")
                    .singleResult();
            if (historicVariableInstance != null) {
                @SuppressWarnings("unchecked")
                List<TurnLog> turnLogList = (List<TurnLog>) historicVariableInstance.getValue();
                if (turnLogList != null) {
                    for (TurnLog turnLog : turnLogList) {
                        ApprovalLog approvalLog = new ApprovalLog();
                        approvalLog.setTaskName(turnLog.getTaskName());
                        approvalLog.setAssignee(turnLog.getAssignee());
                        approvalLog.setFlag(turnLog.getFlag());
                        approvalLog.setRemark(turnLog.getRemark());
                        approvalLog.setOperTime(turnLog.getOperTime());
                        approvalLogList.add(approvalLog);
                    }
                }
            }

            ApprovalLog approvalLog = new ApprovalLog();
            approvalLog.setTaskName(historicActivityInstance.getActivityName());
            approvalLog.setAssignee(historicActivityInstance.getAssignee());
            approvalLog.setOperTime(historicActivityInstance.getEndTime());

            historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                    .taskId(taskId)
                    .variableName("操作")
                    .singleResult();
            if (historicVariableInstance != null) {
                approvalLog.setFlag((String) historicVariableInstance.getValue());
            }

            Comment comment = taskService.getComment(taskId);
            if (comment != null) {
                approvalLog.setRemark(comment.getFullMessage());
            }
            approvalLogList.add(approvalLog);
        }
        return approvalLogList;
    }

    @Override
    public String turnTask(Turn turn) {
        String taskId = turn.getTaskId();

        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        if (task == null) {
            return "任务不存在";
        }

        @SuppressWarnings("unchecked")
        List<TurnLog> turnLogList = (List<TurnLog>) taskService.getVariableLocal(taskId, "turnLog");
        if (turnLogList == null) {
            turnLogList = new ArrayList<>();
        }
        TurnLog turnLog = new TurnLog();
        turnLog.setTaskName(task.getName());
        turnLog.setAssignee(task.getAssignee());
        turnLog.setOperTime(new Date());
        turnLog.setFlag("转交");
        turnLog.setRemark(turn.getRemark());
        turnLogList.add(turnLog);
        taskService.setVariableLocal(taskId, "turnLog", turnLogList);

        taskService.setAssignee(taskId, turn.getAssignee());
        return "成功";
    }

    @Override
    public String delegateTask(Turn turn) {
        String taskId = turn.getTaskId();

        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        if (task == null) {
            return "任务不存在";
        }

        @SuppressWarnings("unchecked")
        List<TurnLog> turnLogList = (List<TurnLog>) taskService.getVariableLocal(taskId, "turnLog");
        if (turnLogList == null) {
            turnLogList = new ArrayList<>();
        }
        TurnLog turnLog = new TurnLog();
        turnLog.setTaskName(task.getName());
        turnLog.setAssignee(task.getAssignee());
        turnLog.setOperTime(new Date());
        turnLog.setFlag("委派");
        turnLog.setRemark(turn.getRemark());
        turnLogList.add(turnLog);
        taskService.setVariableLocal(taskId, "turnLog", turnLogList);

        taskService.delegateTask(taskId, turn.getAssignee());
        return "成功";
    }

    @Override
    public String resolveTask(Turn turn) {
        String taskId = turn.getTaskId();

        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        if (task == null) {
            return "任务不存在";
        }

        @SuppressWarnings("unchecked")
        List<TurnLog> turnLogList = (List<TurnLog>) taskService.getVariableLocal(taskId, "turnLog");
        if (turnLogList == null) {
            turnLogList = new ArrayList<>();
        }
        TurnLog turnLog = new TurnLog();
        turnLog.setTaskName(task.getName());
        turnLog.setAssignee(task.getAssignee());
        turnLog.setOperTime(new Date());
        turnLog.setFlag("委派完成");
        turnLog.setRemark(turn.getRemark());
        turnLogList.add(turnLog);
        taskService.setVariableLocal(taskId, "turnLog", turnLogList);

        taskService.resolveTask(taskId);
        return "成功";
    }

    @Override
    public String suspendProcess(String businessKey) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        if (processInstance == null) {
            return "流程不存在";
        }
        if (processInstance.isSuspended()) {
            return "已挂起";
        }
        runtimeService.suspendProcessInstanceById(processInstance.getId());
        return "成功";
    }

    @Override
    public String activateProcess(String businessKey) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        if (processInstance == null) {
            return "流程不存在";
        }
        if (!processInstance.isSuspended()) {
            return "已激活";
        }
        runtimeService.activateProcessInstanceById(processInstance.getId());
        return "成功";
    }
}

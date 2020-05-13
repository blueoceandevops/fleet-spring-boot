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
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
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

        long count = taskService.createTaskQuery().taskAssignee(userId).count();

        pageUtil.setList(taskInfoList);
        page.setTotalRows((int) count);
        pageUtil.setPage(page);
        return pageUtil;
    }

    private TaskInfo<?> getTaskInfo(Task task) {
        String processInstanceId = task.getProcessInstanceId();

        String taskId = task.getId();
        String taskName = task.getName();
        Date createTime = task.getCreateTime();
        String assignee = task.getAssignee();

        String initiator = (String) taskService.getVariable(taskId, "initiator");
        ProcessInfo<?> info = (ProcessInfo<?>) taskService.getVariable(taskId, "info");

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        String businessKey = processInstance.getBusinessKey();

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        Date startTime = historicProcessInstance.getStartTime();

        // 将task转换成我们需要的格式taskInfo
        TaskInfo<?> taskInfo = new TaskInfo<>();
        taskInfo.setProcessId(processInstanceId);
        taskInfo.setDefinitionKey(info.getDefinitionKey());
        taskInfo.setBusinessKey(businessKey);
        taskInfo.setProcessName(info.getProcessName());
        taskInfo.setInitiator(initiator);
        taskInfo.setPhone(info.getPhone());
        taskInfo.setEmail(info.getEmail());
        taskInfo.setStartTime(startTime);
        taskInfo.setRemark(info.getRemark());
        taskInfo.setTaskId(taskId);
        taskInfo.setTaskName(taskName);
        taskInfo.setCreateTime(createTime);
        taskInfo.setAssignee(assignee);
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
        ProcessInfo<?> processInfo = (ProcessInfo<?>) historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName("info")
                .singleResult()
                .getValue();

        if (historicProcessInstance.getEndTime() == null) {
            processInfo.setState(1);
            String assignee = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult()
                    .getAssignee();
            processInfo.setAssignee(assignee);
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

        processInfo.setProcessId(processInstanceId);
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
        ProcessInfo<?> processInfo = (ProcessInfo<?>) historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName("info")
                .singleResult()
                .getValue();

        if (historicActivityInstance.getEndTime() == null) {
            processInfo.setState(1);
            String assignee = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult()
                    .getAssignee();
            processInfo.setAssignee(assignee);
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

        processInfo.setProcessId(processInstanceId);
        processInfo.setStartTime(historicActivityInstance.getStartTime());
        processInfo.setEndTime(historicActivityInstance.getEndTime());
        return processInfo;
    }

    @Override
    public TaskInfo<?> start(ProcessInfo<?> processInfo) {
        String definitionKey = processInfo.getDefinitionKey();
        String businessKey = processInfo.getBusinessKey();
        String initiator = processInfo.getInitiator();
        identityService.setAuthenticatedUserId(initiator);

        Map<String, Object> vars = new HashMap<>();
        // 通用参数
        vars.put("info", processInfo);
        vars.put("initiator", processInfo.getInitiator());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(definitionKey, businessKey, vars);
        String processInstanceId = processInstance.getId();
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        return getTaskInfo(task);
    }

    @Override
    public String apply(ProcessInfo<?> processInfo) {
        // 先判断business的唯一性
        String businessKey = processInfo.getBusinessKey();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        if (historicProcessInstance != null) {
            return "businessKey:" + businessKey + "已存在";
        }

        TaskInfo<?> taskInfo = start(processInfo);

        Approval approval = new Approval();
        approval.setFlag("提交");
        approval.setRemark("提交");
        approval.setTaskId(taskInfo.getTaskId());
        completeTask(approval);
        return "成功";
    }

    @Override
    public void completeTask(Approval approval) {
        String processInstanceId = taskService.createTaskQuery()
                .taskId(approval.getTaskId())
                .singleResult()
                .getProcessInstanceId();

        // 添加审批记录
        taskService.addComment(approval.getTaskId(), processInstanceId, approval.getRemark());

        Map<String, Object> vars = new HashMap<>();
        vars.put("操作", approval.getFlag());
        taskService.setVariablesLocal(approval.getTaskId(), vars);

        // 完成任务
        taskService.complete(approval.getTaskId(), vars);
    }

    @Override
    public void stop(String businessKey) {
        String processInstanceId = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult()
                .getId();

        runtimeService.deleteProcessInstance(processInstanceId, "终止");
        historyService.deleteHistoricProcessInstance(processInstanceId);
    }

    @Override
    public ProcessInfo<?> getByBusinessKey(String businessKey) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        String processInstanceId = historicProcessInstance.getId();
        return (ProcessInfo<?>) historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName("info")
                .singleResult()
                .getValue();
    }

    @Override
    public ProcessInfo<?> getByProcessId(String processInstanceId) {
        return (ProcessInfo<?>) historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName("info").singleResult().getValue();
    }

    @Override
    public ProcessInfo<?> getByTaskId(String taskId) {
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .taskId(taskId)
                .singleResult();
        String processInstanceId = historicTaskInstance.getProcessInstanceId();
        return (ProcessInfo<?>) historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).variableName("info")
                .singleResult()
                .getValue();
    }

    @Override
    public List<String> getTaskOperation(String taskId) {
        List<String> operationList = new ArrayList<>();
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        if (task == null) {
            return operationList;
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        if (bpmnModel == null) {
            return operationList;
        }
        Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();
        if (flowElements == null) {
            return operationList;
        }
        FlowElement flowElement = getFlowElement(flowElements, task.getTaskDefinitionKey());
        if (flowElement == null) {
            return operationList;
        }
        List<SequenceFlow> outgoingFlows = ((UserTask) flowElement).getOutgoingFlows();
        return getSequenceFlowOperation(flowElements, outgoingFlows);
    }

    private List<String> getSequenceFlowOperation(Collection<FlowElement> flowElements, List<SequenceFlow> outgoingFlows) {
        List<String> operationList = new ArrayList<>();
        for (SequenceFlow sequenceFlow : outgoingFlows) {
            operationList.addAll(analyzeOperation(sequenceFlow.getConditionExpression()));
            FlowElement flowElement = getFlowElement(flowElements, sequenceFlow.getTargetRef());
            if (flowElement instanceof ExclusiveGateway) {
                ExclusiveGateway exclusiveGateway = (ExclusiveGateway) flowElement;
                operationList.addAll(getSequenceFlowOperation(flowElements, exclusiveGateway.getOutgoingFlows()));
            }
        }
        return operationList;
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
        List<String> operationList = new ArrayList<>();
        if (condition != null) {
            condition = condition.substring(2, condition.length() - 1);
            String[] conditionGroup = condition.split("\\|\\||&&");
            for (String group : conditionGroup) {
                String[] pd = group.split("==");
                if (pd[0].trim().equals("操作")) {
                    operationList.add(pd[1].trim().replaceAll("'", ""));
                }
            }
        }
        return operationList;
    }

    @Override
    public ResponseEntity<byte[]> getProcessImage(String processDefinitionKey) {
        ResponseEntity<byte[]> entity = null;
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).latestVersion().singleResult();
            String diagramResourceName = processDefinition.getDiagramResourceName();
            if (diagramResourceName == null) {
                return entity;
            }

            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
            ProcessDiagramGenerator processDiagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
            InputStream in = processDiagramGenerator.generateDiagram(bpmnModel, "png", Collections.<String>emptyList(), Collections.<String>emptyList(), "宋体", "宋体", null, null, 1.0);

            Integer len = in.available();
            byte[] bt = new byte[len];
            in.read(bt);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/png");
            headers.add("Connection", "close");
            headers.add("Accept-Ranges", "bytes");
            entity = new ResponseEntity<byte[]>(bt, headers, HttpStatus.OK);
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
            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
            String processInstanceId = processInstance.getId();
            String processDefinitionId = processInstance.getProcessDefinitionId();

            List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
            List<String> highLightedActivityIds = new ArrayList<String>();
            for (HistoricActivityInstance tempActivity : historicActivityInstanceList) {
                String activityId = tempActivity.getActivityId();
                highLightedActivityIds.add(activityId);
            }

            ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
            List<String> highLightedFlows = getHighLightedFlows(processDefinitionEntity, historicActivityInstanceList);

            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
            ProcessDiagramGenerator processDiagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
            InputStream in = processDiagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivityIds, highLightedFlows, "宋体", "宋体", null, null, 1.0);

            Integer len = in.available();
            byte[] bt = new byte[len];
            in.read(bt);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/png");
            headers.add("Connection", "close");
            headers.add("Accept-Ranges", "bytes");
            entity = new ResponseEntity<byte[]>(bt, headers, HttpStatus.OK);
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return entity;
    }

    /**
     * 获取需要高亮的线
     *
     * @param processDefinitionEntity
     * @param historicActivityInstanceList
     */
    private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstanceList) {
        List<String> highFlows = new ArrayList<>();
        for (int i = 0; i < historicActivityInstanceList.size() - 1; i++) {
            ActivityImpl nextActivityImpl = processDefinitionEntity.findActivity(historicActivityInstanceList.get(i + 1).getActivityId());

            ActivityImpl activityImpl = processDefinitionEntity.findActivity(historicActivityInstanceList.get(i).getActivityId());
            List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();
            for (PvmTransition pvmTransition : pvmTransitions) {
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
                if (nextActivityImpl.equals(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }
        return highFlows;
    }

    @Override
    public List<ApprovalLog> getApprovalLog(String businessKey) {
        List<ApprovalLog> approvalLogList = new ArrayList<>();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
        if (historicProcessInstance == null) {
            return approvalLogList;
        }
        String processInstanceId = historicProcessInstance.getId();
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).activityType("userTask").orderByHistoricActivityInstanceStartTime().asc().list();
        if (historicActivityInstanceList == null) {
            return approvalLogList;
        }

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            String taskId = historicActivityInstance.getTaskId();

            HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery().taskId(taskId).variableName("turnLog").singleResult();
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

            historicVariableInstance = historyService.createHistoricVariableInstanceQuery().taskId(taskId).variableName("操作").singleResult();
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
    public void turnTask(Turn turn) {
        String taskId = turn.getTaskId();
        String newAssignee = turn.getAssignee();
        String remark = turn.getRemark();

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String taskName = task.getName();
        String oldAssignee = task.getAssignee();

        @SuppressWarnings("unchecked")
        List<TurnLog> turnLogList = (List<TurnLog>) taskService.getVariableLocal(taskId, "turnLog");
        if (turnLogList == null) {
            turnLogList = new ArrayList<>();
        }
        TurnLog turnLog = new TurnLog();
        turnLog.setTaskName(taskName);
        turnLog.setAssignee(oldAssignee);
        turnLog.setOperTime(new Date());
        turnLog.setFlag("转交");
        turnLog.setRemark(remark);
        turnLogList.add(turnLog);
        taskService.setVariableLocal(taskId, "turnLog", turnLogList);

        taskService.setAssignee(taskId, newAssignee);
    }

    @Override
    public void delegateTask(Turn turn) {
        String taskId = turn.getTaskId();
        String newAssignee = turn.getAssignee();
        String remark = turn.getRemark();

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String taskName = task.getName();
        String oldAssignee = task.getAssignee();

        @SuppressWarnings("unchecked")
        List<TurnLog> turnLogList = (List<TurnLog>) taskService.getVariableLocal(taskId, "turnLog");
        if (turnLogList == null) {
            turnLogList = new ArrayList<>();
        }
        TurnLog turnLog = new TurnLog();
        turnLog.setTaskName(taskName);
        turnLog.setAssignee(oldAssignee);
        turnLog.setOperTime(new Date());
        turnLog.setFlag("委派");
        turnLog.setRemark(remark);
        turnLogList.add(turnLog);
        taskService.setVariableLocal(taskId, "turnLog", turnLogList);

        taskService.delegateTask(taskId, newAssignee);
    }

    @Override
    public String resolveTask(Turn turn) {
        String taskId = turn.getTaskId();
        String remark = turn.getRemark();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return "任务不存在";
        }
        String taskName = task.getName();
        String oldAssignee = task.getAssignee();

        @SuppressWarnings("unchecked")
        List<TurnLog> turnLogList = (List<TurnLog>) taskService.getVariableLocal(taskId, "turnLog");
        if (turnLogList == null) {
            turnLogList = new ArrayList<>();
        }
        TurnLog turnLog = new TurnLog();
        turnLog.setTaskName(taskName);
        turnLog.setAssignee(oldAssignee);
        turnLog.setOperTime(new Date());
        turnLog.setFlag("委派完成");
        turnLog.setRemark(remark);
        turnLogList.add(turnLog);
        taskService.setVariableLocal(taskId, "turnLog", turnLogList);

        taskService.resolveTask(taskId);
        return "成功";
    }

    @Override
    public String suspendProcess(String businessKey) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
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
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
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

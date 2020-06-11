package com.fleet.activiti5.listener;

import com.fleet.activiti5.entity.ProcessInfo;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.Map;

public class AssigneeTaskListener implements TaskListener {

    private static final long serialVersionUID = 1L;

    /**
     * 设置审批人
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        ProcessInfo<?> processInfo = (ProcessInfo<?>) delegateTask.getVariable("info");
        Map<String, String> assignees = processInfo.getAssignees();
        delegateTask.setAssignee(assignees.get(delegateTask.getName()));
    }
}

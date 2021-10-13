package org.ih.notification;

import org.ih.task.ITask;
import org.ih.task.TaskType;

import java.util.LinkedList;
import java.util.List;

public class NotificationTask extends ITask<NotificationTaskExecutor> {

    private final List<EmailInformation> information;

    public NotificationTask() {
        this.information = new LinkedList<>();
    }

    @Override
    public NotificationTaskExecutor getExecutor() {
        return new NotificationTaskExecutor();
    }

    @Override
    public TaskType getType() {
        return TaskType.SINGLE;
    }

    @Override
    public String getUniqueTaskId() {
        return "Notification Task";
    }

    @Override
    public String getTaskName() {
        return "Notification Task";
    }

    public List<EmailInformation> getInformation() {
        return information;
    }

    public void addInformation(String email, String subject, String body) {
        this.information.add(new EmailInformation(email, subject, body));
    }
}

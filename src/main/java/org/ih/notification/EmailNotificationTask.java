package org.ih.notification;

import org.ih.task.Task;
import org.ih.task.TaskType;

import java.util.LinkedList;
import java.util.List;

/**
 * Task for submitting notifications via email
 */
public class EmailNotificationTask extends Task<EmailNotificationTaskExecutor> {

    private final List<EmailInformation> information;

    public EmailNotificationTask() {
        this.information = new LinkedList<>();
    }

    @Override
    public EmailNotificationTaskExecutor getExecutor() {
        return new EmailNotificationTaskExecutor();
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

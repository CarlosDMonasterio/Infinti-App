package org.ih.notification;

import org.ih.common.logging.Logger;
import org.ih.task.TaskExecutor;

/**
 * Executor for email notifications
 *
 * @author Hector Plahar
 */
public class EmailNotificationTaskExecutor implements TaskExecutor<EmailNotificationTask> {

    private boolean cancel;

    @Override
    public void execute(EmailNotificationTask task) {
        if (task.getInformation().isEmpty()) {
            Logger.info("Cannot send email. No information set");
            return;
        }

        for (EmailInformation info : task.getInformation()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // ignore
            }

            if (cancel) {
                Logger.info("Email notification task cancelled");
                return;
            }

            try {
                Email email = new Email();
                email.send(info.getEmail(), info.getSubject(), info.getBody());
            } catch (Exception e) {
                Logger.error(e);
            }
        }
    }

    @Override
    public boolean cancel() {
        Logger.info("Requesting cancellation of Notification task");
        this.cancel = true;
        return true;
    }
}

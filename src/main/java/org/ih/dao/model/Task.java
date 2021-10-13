package org.ih.dao.model;

import org.ih.dao.DatabaseModel;
import org.ih.dto.TaskTransfer;
import org.ih.task.TaskStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Task data model. All tasks should subclass this for their data storage needs
 *
 * @author Hector Plahar
 */
@Entity
@Table(name = "Task")
public class Task implements DatabaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "task_id")
    @SequenceGenerator(name = "task_id", sequenceName = "task_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "task_name")
    private String name;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;

    @Column(name = "submit_time")
    private Date submitTime;

    @Column(name = "execution_time")
    private Date executionTime;

    @Column(name = "end_time")
    private Date endTime;

    public Task() {
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Date getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }

    @Override
    public TaskTransfer toDataObject() {
        TaskTransfer transfer = new TaskTransfer();
        transfer.setId(id);
        transfer.setSubmitTime(getSubmitTime().getTime());
        return transfer;
    }
}

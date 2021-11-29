package org.ih.dao.model;

import org.ih.dao.DatabaseModel;
import org.ih.dto.Question;

import javax.persistence.*;

// todo : this class should be a question answer and reference question
@Entity
@Table(name = "Question")
public class QuestionModel implements DatabaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "question_id")
    @SequenceGenerator(name = "question_id", sequenceName = "question_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "label")
    private String label;

    @Column(name = "comments")
    private String comments;

    @Column(name = "answer")
    private Boolean answer;

    @ManyToOne(optional = false)   // must have a survey for questions
    private SurveyModel survey;

    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public SurveyModel getSurvey() {
        return survey;
    }

    public void setSurvey(SurveyModel survey) {
        this.survey = survey;
    }

    @Override
    public Question toDataObject() {
        Question question = new Question();
        question.setId(this.id);
        question.setLabel(this.getLabel());
        question.setAnswer(this.answer ? this.answer : false);
        question.setComments(comments);
        return question;
    }
}

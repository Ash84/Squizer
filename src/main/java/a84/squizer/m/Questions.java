package a84.squizer.m;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Questions {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int questionid;

    private int quiz;
    private String question;
    private String hint;
    private int state;
    private String type;

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuestionid() {
        return questionid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getHint() {
        return hint;
    }

    public int getQuiz() {
        return quiz;
    }

    public void setQuiz(int quiz) {
        this.quiz = quiz;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Questions{" +
                "questionid=" + questionid +
                ", quiz=" + quiz +
                ", question='" + question + '\'' +
                ", hint='" + hint + '\'' +
                ", state=" + state +
                ", type='" + type + '\'' +
                '}';
    }
}

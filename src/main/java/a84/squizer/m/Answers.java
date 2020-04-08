package a84.squizer.m;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Answers {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int answerid;
    private int questionid;
    private String answer;
    private int verify;
    private double value;

    public void setAnswerid(int answerid) {
        this.answerid = answerid;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getAnswerid() {
        return answerid;
    }

    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }

    @Override
    public String toString() {
        return "Answers{" +
                "answer_id=" + answerid +
                ", questionid=" + questionid +
                ", answer='" + answer + '\'' +
                ", verify=" + verify +
                ", value=" + value +
                '}';
    }
}

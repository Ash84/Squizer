package a84.squizer.m;

import org.aspectj.weaver.patterns.TypePatternQuestions;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

public class QuestionAnswer {
        private Questions question;
        private List<Answers> answersList;

    public QuestionAnswer(Questions question, List<Answers> answersList) {
        this.question = question;
        this.answersList = answersList;
    }

    public Questions getQuestion() {
        return question;
    }

    public void setQuestion(Questions question) {
        this.question = question;
    }

    public List<Answers> getAnswersList() {
        return answersList;
    }

    public void setAnswersList(List<Answers> answersList) {
        this.answersList = answersList;
    }

    @Override
    public String toString() {
        return "QuestionAnswer{" +
                "question=" + question +
                ", answersList=" + answersList +
                '}';
    }
}

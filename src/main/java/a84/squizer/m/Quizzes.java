package a84.squizer.m;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Quizzes {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int quiz_id;

    private String title;
    private int state;

    public int getQuiz_id() {
        return quiz_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

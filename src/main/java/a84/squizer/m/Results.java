package a84.squizer.m;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Results {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int resultid;

    private int userid;
    private int quizid;
    private double score;
    private double maxscore;

    public double getMaxscore() {
        return this.maxscore;
    }

    public void setMaxscore(double maxscore) {
        this.maxscore = maxscore;
    }
    private int tries;

    public int getUserid() {
        return userid;
    }

    public void setQuizid(int quizid) {
        this.quizid = quizid;
    }

    public int getQuizid() {
        return quizid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getResultid() {
        return resultid;
    }


    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }
}

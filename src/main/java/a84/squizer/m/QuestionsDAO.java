package a84.squizer.m;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionsDAO extends JpaRepository<Questions, Integer> {
    List<Questions> findAll();
    List<Questions> findAllByState(int state);
    Questions findById(int questionid);
    void deleteById(int questionid);
    List<Questions> findAllByquestionid(int questionid);
    List<Questions> findAllByQuiz(int quiz);
    boolean existsByQuestion(String question);
}
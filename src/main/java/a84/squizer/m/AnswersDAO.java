package a84.squizer.m;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswersDAO extends JpaRepository<Answers, Integer> {
    List<Answers> findAllByquestionid(int questionid);
    Answers findById(int answerid);
	void deleteByquestionid(int questionid);
	Answers findByquestionid(int questionid);
}
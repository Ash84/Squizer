package a84.squizer.m;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizzesDAO extends JpaRepository<Quizzes, Integer> {

    boolean existsByTitle(String title);
    List<Quizzes> findAll();
    List<Quizzes> findAllByState(int state);
    Quizzes findById(int quiz_id);
    void deleteById(Integer integer);
    Quizzes findByTitle(String title);
}
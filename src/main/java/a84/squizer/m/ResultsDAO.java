package a84.squizer.m;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultsDAO extends JpaRepository<Results, Integer> {

	List<Results> findAllByuserid(int userid);

}
package a84.squizer.m;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Integer> {

    boolean existsByName(String name);
    User findByName(String name);
    User findByUserid(int userid);
}

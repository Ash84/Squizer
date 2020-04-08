//package a84.employee;
//
//
//import a84.employee.m.User;
//import a84.employee.m.UserDAO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class FirstRun implements CommandLineRunner {
//
//    @Autowired
//    UserDAO userDAO;
//
//    @Override
//    public void run(String[] args) {
//        System.out.println("hello");
//
//        List<User> lu = userDAO.findAll();
//        for (int i = 0; i < lu.size(); i++)
//            System.out.println(lu.get(i));
//
//    }
//}

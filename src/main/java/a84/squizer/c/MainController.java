package a84.squizer.c;

import a84.squizer.m.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@SessionAttributes({"log", "role", "quizid", "name", "time", "count", "timequiz", "countquiz"})

public class MainController {

    @Autowired
    UserDAO userDAO;

    @Autowired
    QuestionsDAO questionsDAO;

    @Autowired
    QuizzesDAO quizzesDAO;

    @Autowired
    ResultsDAO resultsDAO;

    @Autowired
    AnswersDAO answersDAO;

    public static long lasttime = 0;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam(name="name", required=false) String name,
                        @RequestParam(name="password", required=false) String password,
                        Model model){
        model.addAttribute("error_msg", null);
        if (model.containsAttribute("log") && (boolean) model.getAttribute("log")) {
            List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
            if (quizzes.size() > 5) {
                quizzes = quizzes.subList(0, 5);
            }
            model.addAttribute("quizzes", quizzes);
            return "index";
        } else if (userDAO.existsByName(name)) {
            User thisuser = userDAO.findByName(name);
            String thispass = BCrypt.hashpw(password, thisuser.getSalt());
            if (thisuser.getHashedPassword().equals(thispass)) {
                model.addAttribute("log", true);
                model.addAttribute("name", name);
                model.addAttribute("role", thisuser.getRole());
                List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
                if (quizzes.size() > 5) {
                    quizzes = quizzes.subList(0, 5);
                }
                model.addAttribute("quizzes", quizzes);
                return "index";
            } else {
                model.addAttribute("error_msg", "Les mots de passe ne correspondent pas.");
                return "login";

            }
        } else if (name != null & !userDAO.existsByName(name)) {
            model.addAttribute("error_msg", "Ce pseudo n'existe pas.");
            return "login";
        } else {
            return "login";
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        if (model.containsAttribute("log") && (boolean)model.getAttribute("log") ){
            List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
            if (quizzes.size() > 5) {
                quizzes = quizzes.subList(0, 5);
            }
            model.addAttribute("quizzes", quizzes);
            return "index";
        }
        return "login";
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session, ModelMap model) {
        model.remove("log");
        model.remove("role");
        model.remove("name");
        model.remove("quizid");
        session.invalidate();
        List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
        if (quizzes.size() > 5) {
            quizzes = quizzes.subList(0, 5);
        }
        model.addAttribute("quizzes", quizzes);
        return "index";
    }

    @RequestMapping(value = "/index")
    public String index(Model model){
        List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
        if (quizzes.size() > 5) {
            quizzes = quizzes.subList(0, 5);
        }
        model.addAttribute("quizzes", quizzes);
        return "index";
    }

    @RequestMapping(value = "/user")
    public String user(Model model, ModelMap modelmap){
        if (!model.containsAttribute("log") || !(boolean)model.getAttribute("log") ){
            List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
            if (quizzes.size() > 5) {
                quizzes = quizzes.subList(0, 5);
            }
            model.addAttribute("quizzes", quizzes);
            return "index";
        }
        User user = userDAO.findByName((String)modelmap.getAttribute("name"));
        List <Results> results = resultsDAO.findAllByuserid(user.getUserid());
        ArrayList <QuizandResult> quizandresults = new ArrayList<QuizandResult>();
        for (Results res : results) {
            QuizandResult buff = new QuizandResult(res, quizzesDAO.findById(res.getQuizid()));
            quizandresults.add(buff);
        }
        model.addAttribute("quizresults", quizandresults);
        model.addAttribute("userid", user.getUserid());
        return "user";
    }

    @RequestMapping(value = "deleteresult")
    public String user(Model model, ModelMap modelmap, @RequestParam(name="resultid", required = false) String resultid){
        if (!model.containsAttribute("log") || !(boolean)model.getAttribute("log") ){
            List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
            if (quizzes.size() > 5) {
                quizzes = quizzes.subList(0, 5);
            }
            model.addAttribute("quizzes", quizzes);
            return "index";
        }
        User user = userDAO.findByName((String)modelmap.getAttribute("name"));
        if (resultid != null && resultsDAO.existsById(Integer.parseInt(resultid))) {
            resultsDAO.deleteById(Integer.parseInt(resultid));
        }
        List <Results> results = resultsDAO.findAllByuserid(user.getUserid());
        ArrayList <QuizandResult> quizandresults = new ArrayList<QuizandResult>();
        for (Results res : results) {
            QuizandResult buff = new QuizandResult(res, quizzesDAO.findById(res.getQuizid()));
            quizandresults.add(buff);
        }
        model.addAttribute("quizresults", quizandresults);
        model.addAttribute("userid", user.getUserid());
        return "user";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(
            @RequestParam(name="name", required = true) String name,
            @RequestParam(name="password", required = true) String password,
            @RequestParam(name="passwordc", required = true) String passwordc,
            Model model) {

        List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
        if (quizzes.size() > 5) {
            quizzes = quizzes.subList(0, 5);
        }
        model.addAttribute("quizzes", quizzes);
        long thistime = new java.util.Date().getTime();
        if (model.containsAttribute("time")) {
            long lasttime = (long) model.getAttribute("time");
            int count = (int)model.getAttribute("count");
            if ((long) thistime - lasttime >= 3600){
                model.addAttribute("time", thistime);
                model.addAttribute("count", 0);
            }
            else if (count < 5) {
                model.addAttribute("count", count+1);
            }
            else {
                model.addAttribute("error_msg", "Trop de requêtes d'inscriptions sur le site, veuillez patienter (~1min)");
                return "register";
            }
        }
        else {
            model.addAttribute("time", thistime);
            model.addAttribute("count", 0);
        }
        if (name.length() < 5) {
            model.addAttribute("error_msg", "Votre nom est trop court ! 5 symboles minimum.");
            return "register";
        }
        if (password.equals(passwordc)) {
            boolean number = false;
            for (int i=0; i <= 9; i++) {
                if (password.contains("" + i)) {
                    number = true;
                }
            }
            boolean found = userDAO.existsByName(name);
            if (password.length() < 4) {
                model.addAttribute("error_msg", "Erreur : Le mot de passe saisi est trop court.");
                return "register";
            }
            if (!number) {
                model.addAttribute("error_msg", "Erreur : Le mot de passe doit contenir au moins un chiffre.");
                return "register";
            }
            if (found) {
                model.addAttribute("error_msg", "Erreur: Ce pseudonyme est déjà pris.");
                return "register";
            }
            User u = new User();
            u.setName(name);
            String salt = BCrypt.gensalt();
            u.setSalt(salt);
            u.setHashedPassword(BCrypt.hashpw(password, salt));
            u.setRole(0);
            userDAO.save(u);
            return "index";
        }
        else {
            model.addAttribute("error_msg", "Erreur: Les mots de passe ne correspondent pas.");
        }
        return "register";
    }

    @RequestMapping(value = "/register")
    public String register() {
        return "register";
    }

    @RequestMapping(value = "/")
    public String quiz(Model model){
        List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
        if (quizzes.size() > 5) {
            quizzes = quizzes.subList(0, 5);
        }
        model.addAttribute("quizzes", quizzes);
        return "index";
    }


    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    public String admin(@RequestParam(name="namequiz", required = false) String namequiz,
                        @RequestParam(name="draft", required = false) String draft,
                        @RequestParam(name="save", required = false) String save,
                        @RequestParam(name="editingquiz", required = false) String editingquiz,
                        @RequestParam(name="classicreturn", required = false) String classicreturn,
                        Model model) {

        if (!((int) model.getAttribute("role") == 2 || (int) model.getAttribute("role") == 1)){
            return "index";
        }
        boolean found = quizzesDAO.existsByTitle(namequiz);
        if (!found && !(editingquiz != null)) {
            if(draft != null){
                Quizzes q = new Quizzes();
                q.setTitle(namequiz);
                q.setState(0);
                quizzesDAO.save(q);
            }
            else if (save != null) {
                Quizzes q = new Quizzes();
                q.setTitle(namequiz);
                q.setState(0);
                model.addAttribute("error_msg", "Vous ne pouvez pas publier un Quiz avec moins de 4 questions.");
                quizzesDAO.save(q);
            }
            else {
                if (classicreturn == null)
                model.addAttribute("error_msg", "Quelque chose s'est mal déroulé, veuillez réessayer.");
            }
        }
        else if (editingquiz != null){
            if(draft != null){
                Quizzes q = quizzesDAO.findByTitle(editingquiz);
                q.setTitle(namequiz);
                q.setState(0);
                quizzesDAO.save(q);
            }
            else if (save != null) {
                Quizzes q = quizzesDAO.findByTitle(editingquiz);
                if (questionsDAO.findAllByQuiz(q.getQuiz_id()).size() < 4) {
                    q.setState(0);
                    model.addAttribute("error_msg", "Vous ne pouvez pas publier un Quiz avec moins de 4 questions.");
                }
                else {
                    q.setState(1);
                }
                q.setTitle(namequiz);
                quizzesDAO.save(q);
            }
        }
        else {
            model.addAttribute("error_msg", "Ce nom de Quiz est déjà utilisé.");
        }
        List<Quizzes> quizzes = quizzesDAO.findAll();
        model.addAttribute("quizzes", quizzes);
        return "admin";
    }

    @RequestMapping(value = "/manageusers", method = RequestMethod.POST)
    public String manageusers(Model model, @RequestParam(name="delete", required = false) String delete,
                                           @RequestParam(name="givmod", required = false) String givmod,
                                           @RequestParam(name="givcasu", required = false) String givcasu) {

        if( !model.containsAttribute("role") || (int) model.getAttribute("role") != 2){
            List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
            if (quizzes.size() > 5) {
                quizzes = quizzes.subList(0, 5);
            }
            return "index";
        }

        if (delete != null) {
            int x = Integer.parseInt(delete);
            userDAO.deleteById(x);
        }
        if (givmod != null) {
            int x = Integer.parseInt(givmod);
            User user = userDAO.findByUserid(x);
            user.setRole(1);
            userDAO.deleteById(x);
            userDAO.save(user);
        }
        if (givcasu != null) {
            int x = Integer.parseInt(givcasu);
            User user = userDAO.findByUserid(x);
            user.setRole(0);
            userDAO.deleteById(x);
            userDAO.save(user);
        }
        List<User> users = userDAO.findAll();
        ArrayList<User> userstodisplay = new ArrayList<User>();
        for (User user : users){
            if (user.getUserid() != 0) {
                userstodisplay.add(user);
            }
        }
        model.addAttribute("users", userstodisplay);
        return "manageusers";
    }

    @RequestMapping(value = "deleteaccount", method = RequestMethod.POST)
    public String deleteaccount(Model model, HttpSession session, ModelMap modelmap, @RequestParam(name="userid", required = true) String userid) {

        if( !model.containsAttribute("log") || (boolean) model.getAttribute("log") == false){
            List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
            if (quizzes.size() > 5) {
                quizzes = quizzes.subList(0, 5);
            }
            return "index";
        }
        if (userid != null) {
            int x = Integer.parseInt(userid);
            userDAO.deleteById(x);
        }
        modelmap.remove("log");
        modelmap.remove("role");
        modelmap.remove("name");
        modelmap.remove("quizid");
        session.invalidate();
        return "index";
    }

    @RequestMapping(value = "modpass", method = RequestMethod.POST)
    public String modpass(Model model,  @RequestParam(name="pass", required = false) String userid,
                                        @RequestParam(name="oldpassword", required = false) String oldpass,
                                        @RequestParam(name="passwordn", required = false) String newpass,
                                        @RequestParam(name="passwordc", required = false) String newpassc) {

        if (!model.containsAttribute("log") || !(boolean)model.getAttribute("log") ){
            List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
            if (quizzes.size() > 5) {
                quizzes = quizzes.subList(0, 5);
            }
            model.addAttribute("quizzes", quizzes);
            return "index";
        }        
        User user = userDAO.findByUserid(Integer.parseInt(userid));
        List <Results> results = resultsDAO.findAllByuserid(user.getUserid());
        ArrayList <QuizandResult> quizandresults = new ArrayList<QuizandResult>();
        for (Results res : results) {
            QuizandResult buff = new QuizandResult(res, quizzesDAO.findById(res.getQuizid()));
            quizandresults.add(buff);
        }
        model.addAttribute("quizresults", quizandresults);
        model.addAttribute("userid", user.getUserid());
        String hasholdpass = BCrypt.hashpw(oldpass, user.getSalt());
        if (user.getHashedPassword().equals(hasholdpass)) {
            if (newpass.equals(newpassc)) {
                boolean number = false;
                for (int i=0; i <= 9; i++) {
                    if (newpass.contains("" + i)) {
                        number = true;
                    }
                }
                if (newpass.length() < 4) {
                    model.addAttribute("error_msg", "Erreur : Le nouveau mot de passe saisi est trop court.");
                    return "user";
                }
                else if (!number) {
                    model.addAttribute("error_msg", "Erreur : Le nouveau mot de passe doit contenir au moins un chiffre.");
                    return "user";
                }
                else {
                    userDAO.deleteById(user.getUserid());
                    String salt = BCrypt.gensalt();
                    user.setSalt(salt);
                    user.setHashedPassword(BCrypt.hashpw(newpass, salt));
                    userDAO.save(user);
                }
            }
            else {
                model.addAttribute("error_msg", "Erreur : L'ancien mot de passe ne correspond pas.");
            }
        }
        return "user";
    }

    @RequestMapping(value = "/manageusers", method = RequestMethod.GET)
    public String manageusers(Model model) {

        if( !model.containsAttribute("role") || (int) model.getAttribute("role") != 2){
            List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
            if (quizzes.size() > 5) {
                quizzes = quizzes.subList(0, 5);
            }
            return "index";
        }
        List<User> users = userDAO.findAll();
        ArrayList<User> userstodisplay = new ArrayList<User>();
        for (User user : users){
            if (user.getUserid() != 0) {
                userstodisplay.add(user);
            }
        }
        model.addAttribute("users", userstodisplay);
        return "manageusers";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(Model model) {
        if(model.containsAttribute("role") && (int) model.getAttribute("role") == 2) {
            List<Quizzes> quizzes = quizzesDAO.findAll();
            model.addAttribute("quizzes", quizzes);
            return "admin";
        }
        List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
        if (quizzes.size() > 5) {
            quizzes = quizzes.subList(0, 5);
        }
        return "index";
    }

    @RequestMapping(value = "/editquiz", method = RequestMethod.POST)
    public String editquiz(Model model, @RequestParam(name="quiz_id", required = false) int quizid) {
        List<Quizzes> quizzes = quizzesDAO.findAll();
        Quizzes quiztoedit = quizzesDAO.findById(quizid);
        model.addAttribute("quizzes", quizzes);
        model.addAttribute("quiztoedit", quiztoedit.getTitle());
        if(model.containsAttribute("role") && (int) model.getAttribute("role") == 2)
            return "admin";
        return "index";
    }

    @RequestMapping(value = "/editquiz", method = RequestMethod.GET)
    public String editquiz(Model model) {
        
        if(model.containsAttribute("role") && (int) model.getAttribute("role") == 2) {
            List<Quizzes> quizzes = quizzesDAO.findAll();
            model.addAttribute("quizzes", quizzes);
            return "admin";
        }
        List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
        if (quizzes.size() > 5) {
            quizzes = quizzes.subList(0, 5);
        }
        return "index";
    }



    @RequestMapping(value = "/deletequiz", method = RequestMethod.POST)
    public String deletequiz(Model model, @RequestParam(name="quiz_id", required = false) int quiz_id) {
        quizzesDAO.deleteById(quiz_id);
        List<Quizzes> quizzes = quizzesDAO.findAll();
        model.addAttribute("quizzes", quizzes);
        return "admin";
    }

    @RequestMapping(value = "/deletequiz", method = RequestMethod.GET)
    public String deletequiz(Model model) {
        model.addAttribute("error_msg", "Le site a rencontré une situation inattendue, vous avez été redirigés...");
        if (!model.containsAttribute("log") || !(boolean)model.getAttribute("log") ){
            return "redirecttologin";
        }
        if(model.containsAttribute("role") && (int) model.getAttribute("role") == 2 || model.containsAttribute("role") && (int) model.getAttribute("role") == 1) {
            List<Quizzes> quizzes = quizzesDAO.findAll();
            model.addAttribute("quizzes", quizzes);
            return "admin";
        }
        List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
        if (quizzes.size() > 5) {
            quizzes = quizzes.subList(0, 5);
        }
        return "index";
    }

    @RequestMapping(value = "index/quiz/{quiz_id}")
    public String allQuizzes(Model model, @PathVariable int quiz_id) {
        if (!model.containsAttribute("log") || !(boolean)model.getAttribute("log") ){
            return "redirecttologin";
        }
        List<Questions> questions = questionsDAO.findAllByQuiz(quiz_id);
        ArrayList<QuestionAnswer> questionresponse = new ArrayList<QuestionAnswer>();
        for (int i=0; i<questions.size(); i++) {
            Questions buffer = questions.get(i);
            List<Answers> bufferans = answersDAO.findAllByquestionid(questions.get(i).getQuestionid());
            questionresponse.add(new QuestionAnswer(buffer, bufferans));
        }
        model.addAttribute("quiztitle", quizzesDAO.findById(quiz_id).getTitle());
        model.addAttribute("quizid", quiz_id);
        model.addAttribute("list", questionresponse);
        return "quiz";
    }

    @RequestMapping(value = "/quizalea")
    public String quizalea(Model model) {
        if (!model.containsAttribute("log") || !(boolean)model.getAttribute("log") ){
            return "redirecttologin";
        }
        List <Quizzes> quizzes = quizzesDAO.findAll();
        Random rand = new Random();
        Quizzes quiz = quizzes.get(rand.nextInt(quizzes.size()));
        int quiz_id = quiz.getQuiz_id();
        List<Questions> questions = questionsDAO.findAllByQuiz(quiz_id);
        ArrayList<QuestionAnswer> questionresponse = new ArrayList<QuestionAnswer>();
        for (int i=0; i<questions.size(); i++) {
            Questions buffer = questions.get(i);
            List<Answers> bufferans = answersDAO.findAllByquestionid(questions.get(i).getQuestionid());
            questionresponse.add(new QuestionAnswer(buffer, bufferans));
        }
        model.addAttribute("quiztitle", quizzesDAO.findById(quiz_id).getTitle());
        model.addAttribute("quizid", quiz_id);
        model.addAttribute("list", questionresponse);
        return "quiz";
    }

    @RequestMapping(value = "redirecttologin")
    public String redirecttologin(Model model) {
        return "redirecttologin";
    }

    @RequestMapping(value = "questions", method = RequestMethod.POST)
    public String questions(@RequestParam(name="question", required = false) String question,
                               @RequestParam(name="hint", required = false) String hint,
                               @RequestParam(name="type", required = false) String type,
                               @RequestParam(name="Publier", required = false) String save,
                               @RequestParam(name="Brouillon", required = false) String draft,
                               @RequestParam(name="quizid", required = true) int quizid,
                               @RequestParam(name="editingquestion", required = false) String editingquestion,
                               ModelMap modelmap,
                               Model model) {

        Quizzes quiz = quizzesDAO.findById(quizid);
        model.addAttribute("quiz", quiz);
        model.addAttribute("quizid", quizid);
        model.addAttribute("quizname", "Vous travaillez sur le quiz : " + quiz.getTitle());
        boolean found = questionsDAO.existsByQuestion(question);
        if ((draft != null && hint == null) || (draft != null && type == null) || (draft != null && question == null) ||
            (save != null && hint == null) || (save != null && type == null) || (save != null && question == null)) {
            List<Questions> questions = questionsDAO.findAllByQuiz((int)model.getAttribute("quizid"));
            model.addAttribute("questions", questions);
            model.addAttribute("error_msg", "L'un des champs a mal été rempli.");
            return "questions";
        }
        if (!found && !(editingquestion != null)) {
            if (save != null) {
                Questions q = new Questions();
                q.setQuestion(question);
                q.setHint(hint);
                q.setType(type);
                q.setState(1);
                q.setQuiz((int) model.getAttribute("quizid"));
                questionsDAO.save(q);
                int id = q.getQuestionid();
                List<Answers> answers = answersDAO.findAllByquestionid(id);
                model.addAttribute(answers);
            }
            else if (draft != null) {
                Questions q = new Questions();
                q.setQuestion(question);
                q.setHint(hint);
                q.setType(type);
                q.setState(0);
                q.setQuiz((int) model.getAttribute("quizid"));
                questionsDAO.save(q);
                int id = q.getQuestionid();
                List<Answers> answers = answersDAO.findAllByquestionid(id);
                model.addAttribute(answers);
            }
        }
        if (found && (editingquestion != null)) {
            if (save != null) {
                Questions q = questionsDAO.findById(Integer.parseInt(editingquestion));
                q.setQuestion(question);
                q.setHint(hint);
                q.setType(type);
                q.setState(1);
                q.setQuiz((int) model.getAttribute("quizid"));
                questionsDAO.save(q);
                int id = q.getQuestionid();
                List<Answers> answers = answersDAO.findAllByquestionid(id);
                model.addAttribute(answers);
            }
            else if (draft != null) {
                Questions q = questionsDAO.findById(Integer.parseInt(editingquestion));
                q.setQuestion(question);
                q.setHint(hint);
                q.setType(type);
                q.setState(0);
                q.setQuiz((int) model.getAttribute("quizid"));
                questionsDAO.save(q);
                int id = q.getQuestionid();
                List<Answers> answers = answersDAO.findAllByquestionid(id);
                model.addAttribute(answers);
            }
        }

        List<Questions> questions = questionsDAO.findAllByQuiz((int)model.getAttribute("quizid"));
        model.addAttribute("questions", questions);
        return "questions";
    }

    @RequestMapping(value = "/editquestion", method = RequestMethod.POST)
    public String editquestion(Model model, @RequestParam(name="questionid", required = false) int questionid) {
        List<Questions> questions = questionsDAO.findAllByQuiz((int)model.getAttribute("quizid"));
        model.addAttribute("questions", questions);
        Questions questiontoedit = questionsDAO.findById((int)questionid);
        model.addAttribute("questiontoedit", questiontoedit);
        if(model.containsAttribute("role") && (int) model.getAttribute("role") == 2)
            return "questions";
        return "index";
    }
    

    @RequestMapping(value = "/deletequestion", method = RequestMethod.POST)
    public String deletequestion(Model model, @RequestParam(name="questionid", required = true) int questionid) {
        questionsDAO.deleteById((int)questionid);
        List<Questions> questions = questionsDAO.findAllByQuiz((int)model.getAttribute("quizid"));
        model.addAttribute("questions", questions);
        return "questions";
    }

    @RequestMapping(value = "questions", method = RequestMethod.GET)
    public String questions(Model model) {

        if(model.containsAttribute("role") && (int) model.getAttribute("role") == 2) {
            List<Quizzes> quizzes = quizzesDAO.findAll();
            model.addAttribute("quizzes", quizzes);
            return "admin";
        }
        List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
        if (quizzes.size() > 5) {
            quizzes = quizzes.subList(0, 5);
        }
        return "index";
    }

    @RequestMapping(value = "answers", method = RequestMethod.POST)
    public String answers(@RequestParam(name="questionid", required = true) int questionid,
                               @RequestParam(name="editinganswer", required = false) String editinganswer,
                               ModelMap modelmap,
                               Model model) {

        if (!((int) model.getAttribute("role") == 2 || (int) model.getAttribute("role") == 1)){
            List<Quizzes> quizzes = quizzesDAO.findAll();
            model.addAttribute("quizzes", quizzes);
            return "index";
        }
        Questions question = questionsDAO.findById(questionid);
        model.addAttribute("questionname", "Vous travaillez sur la question : " + question.getQuestion());
        model.addAttribute("questionid", questionid);
        model.addAttribute("quizid", question.getQuiz());
        List<Answers> answers = answersDAO.findAllByquestionid(questionid);
        if (answers.size() > 1 && question.getType().compareTo("texte") == 0) {
            answers = answers.subList(0, 1);
            model.addAttribute("error_msg", "Vous ne pouvez saisir qu'une seule réponse texte");
        }
        model.addAttribute("answers", answers);
        return "answers";
    }

    @RequestMapping(value = "answer", method = RequestMethod.POST)
    public String answer(      @RequestParam(name="questionid", required = true) int questionid,
                               @RequestParam(name="editinganswer", required = false) String editinganswer,
                               @RequestParam(name="answer", required = false) String answerphrase,
                               @RequestParam(name="value", required = false) double value,
                               @RequestParam(name="verify", required = false) int verify,
                               ModelMap modelmap,
                               Model model) {

        if (!((int) model.getAttribute("role") == 2 || (int) model.getAttribute("role") == 1)){
            List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
            if (quizzes.size() > 5) {
                quizzes = quizzes.subList(0, 5);
            }
            return "index";
        }

        Questions question = questionsDAO.findById(questionid);
        model.addAttribute("questionname", "Vous travaillez sur la question : " + question.getQuestion());
        model.addAttribute("questionid", questionid);
        model.addAttribute("quizid", question.getQuiz());

        Answers answer = new Answers();
        answer.setAnswer(answerphrase);
        answer.setQuestionid(questionid);
        if (question.getType().compareTo("bouton") == 0 && value != 0 && verify == 0) {
            answer.setValue(0);
            model.addAttribute("error_msg", "Un bouton attendu comme faux n'a pas de valeur de points.");
        }
        else {
            answer.setValue(value);
        }
        if (question.getType().compareTo("texte") == 0) {
            answer.setVerify(1);
            model.addAttribute("error_msg", "Une réponse texte sera toujours considérée comme attendue vraie.");
        }
        else
            answer.setVerify(verify);
        List<Answers> answers = answersDAO.findAllByquestionid(questionid);
        if (answers.size() == 0 && question.getType().compareTo("texte") == 0) {
            answersDAO.save(answer);
        }
        else if (question.getType().compareTo("texte") != 0)
            answersDAO.save(answer);
        else
            model.addAttribute("error_msg", "Vous ne pouvez saisir qu'une seule réponse texte. Celle-ci sera toujours attendue comme vraie.");
        answers = answersDAO.findAllByquestionid(questionid);
        model.addAttribute("answers", answers);
        return "answers";
    }


    @RequestMapping(value = "/deleteanswer", method = RequestMethod.POST)
    public String quizresults(Model model, ModelMap modelmap, @RequestParam(name="answerid", required = true) int answerid) {

        if (!((int) model.getAttribute("role") == 2 || (int) model.getAttribute("role") == 1)){
            List<Quizzes> quizzes = quizzesDAO.findAll();
            model.addAttribute("quizzes", quizzes);
            return "index";
        }
        int questionid = answersDAO.findById(answerid).getQuestionid();
        answersDAO.deleteById(answerid);
        List<Answers> answers = answersDAO.findAllByquestionid(questionid);
        model.addAttribute("answers", answers);
        model.addAttribute("questionid", questionid);
        return "answers";
    }

    @RequestMapping(value = "/quizresults", method = RequestMethod.POST)
    public String quizresults(Model model, ModelMap modelmap, @RequestParam Map<String,String> allRequestParams, @RequestParam MultiValueMap<String, String> multiParams) {
        Map parameterMap = allRequestParams;
        List<Quizzes> quizzes = quizzesDAO.findAllByState(1);
        if (quizzes.size() > 5) {
            quizzes = quizzes.subList(0, 5);
        }
        model.addAttribute("quizzes", quizzes);
        long thistime = new java.util.Date().getTime();
        if (model.containsAttribute("lasttimequiz")) {
            long lasttimequiz = (long) model.getAttribute("lasttimequiz");
            int count = (int)model.getAttribute("countquiz");
            if ((long) thistime - lasttimequiz >= 3600){
                model.addAttribute("lasttimequiz", thistime);
                model.addAttribute("countquiz", 0);
            }
            else if (count < 5) {
                model.addAttribute("countquiz", count+1);
            }
            else {
                model.addAttribute("error_msg", "Trop de requêtes de votre part. Veuillez patienter (~1min)");
                return "index";
            }
        }
        else {
            model.addAttribute("lasttimequiz", thistime);
            model.addAttribute("countquiz", 0);
        }
        int quizid = (int) modelmap.getAttribute("quizid");
        List<Questions> questions = questionsDAO.findAllByQuiz(quizid);
        String rep;
        List<String> repbox;
        double points = 0;
        double totalpoints = 0;
        String check;
        for (Questions question : questions) {
            check = "" + question.getQuestionid();
            if (parameterMap.get(check) != null) {
                if (question.getType().compareTo("checkbox") == 0) {
                    repbox = multiParams.get(check);
                    List<Answers> answers = answersDAO.findAllByquestionid(question.getQuestionid());
                    for (Answers answer : answers) {
                        if(answer.getVerify() == 1) {
                            for (String repmul : repbox) {
                                if (repmul.compareTo("" + answer.getAnswerid()) == 0) {
                                    points += answer.getValue();
                                }
                            }
                        }
                        else {
                            boolean falseanswer = false;
                            for (String repmul : repbox) {
                                if (repmul.compareTo("" + answer.getAnswerid()) == 0) {
                                     falseanswer = true;
                                }
                            }
                            if (!falseanswer) {
                                points += answer.getValue();
                            }
                        }
                        totalpoints += answer.getValue();
                    }
                }
                else if (question.getType().compareTo("texte") == 0) {
                    rep = (String) parameterMap.get(check);
                    Answers answer = answersDAO.findByquestionid(question.getQuestionid());
                    if (rep.compareTo(answer.getAnswer()) == 0) {
                        points += answer.getValue();
                        totalpoints += answer.getValue();
                    }
                    else {
                        totalpoints += answer.getValue();
                    }
                }
                else {
                    rep = (String) parameterMap.get(check);
                    List<Answers> answers = answersDAO.findAllByquestionid(question.getQuestionid());
                    for (Answers answer : answers) {
                        if ((rep.compareTo("" + answer.getAnswerid()) == 0) && (answer.getVerify() == 1)) {
                            points += answer.getValue();
                        }
                        totalpoints += answer.getValue();
                    }
                }
            }
            else {
                List<Answers> answers = answersDAO.findAllByquestionid(question.getQuestionid());
                for (Answers answer : answers) {
                    if (answer.getVerify() == 1) {
                        totalpoints += answer.getValue();
                    }
                    else {
                        points += answer.getValue();
                        totalpoints += answer.getValue();
                    }
                }
            }
        }
        model.addAttribute("score", points);
        model.addAttribute("maxscore", totalpoints);
        Results result = new Results();
        Results prevresult = new Results();
        boolean prevresultfound = false;
        result.setScore(points);
        result.setMaxscore(totalpoints);
        result.setQuizid(quizid);
        User user = userDAO.findByName((String)model.getAttribute("name"));
        result.setUserid((int)user.getUserid());
        List <Results> userresults = resultsDAO.findAllByuserid((int)user.getUserid());
        for (Results res : userresults) {
            if(res.getQuizid() == quizid){
                prevresult = res;
                prevresultfound = true;
            }
        }
        if (prevresultfound) {
            if (prevresult.getMaxscore() != totalpoints) {
                model.addAttribute("resultsentence", "Le modèle de quiz a changé ! Votre ancien score a été supprimé. Vous avez désormais " + points + " / " + totalpoints + " pts !");
            } 
            else if (prevresult.getScore() < points) {
                model.addAttribute("resultsentence", "Vous avez amélioré votre résultat ! L'ancien score était de " + prevresult.getScore() + " / " + totalpoints +" ! Vous avez désormais " + points + " / " + totalpoints + " pts !");
                result.setScore(points);
                result.setTries(prevresult.getTries()+1);
            }
            else if (prevresult.getScore() < points) {
                model.addAttribute("resultsentence", "Vous avez fait pareil que la dernière fois... L'ancien score était de " + prevresult.getScore() + " / " + totalpoints +" ! Vous avez à nouveau " + points + " / " + totalpoints + " pts.");
                result.setTries(prevresult.getTries()+1);
            }
            else {
                model.addAttribute("resultsentence", "Vous avez fait moins bien que la dernière fois... L'ancien score était de " + prevresult.getScore() + " / " + totalpoints +" ! Vous avez cette fois-ci " + points + " / " + totalpoints + " pts.");
                result.setTries(prevresult.getTries()+1);
            }
            resultsDAO.deleteById(prevresult.getResultid());
            resultsDAO.save(result);
            modelmap.remove("quizid");
            return "quizresults";
        }
        else {
            result.setTries(1);
            resultsDAO.save(result);
            model.addAttribute("resultsentence", "Vous avez " + points + " / " + totalpoints + " pts !");
            model.addAttribute("quiztitle", quizzesDAO.findById(quizid).getTitle());
            modelmap.remove("quizid");
            return "quizresults";
        }
    }
}
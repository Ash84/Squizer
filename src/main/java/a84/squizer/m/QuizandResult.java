package a84.squizer.m;

public class QuizandResult {
        private Results results;
        private Quizzes quizzes;

    public QuizandResult() {
    }

    public QuizandResult(Results results, Quizzes quizzes) {
        this.results = results;
        this.quizzes = quizzes;
    }

    public Results getResults() {
        return this.results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

    public Quizzes getQuizzes() {
        return this.quizzes;
    }

    public void setQuizzes(Quizzes quizzes) {
        this.quizzes = quizzes;
    }

    public QuizandResult results(Results results) {
        this.results = results;
        return this;
    }

    public QuizandResult quizzes(Quizzes quizzes) {
        this.quizzes = quizzes;
        return this;
    }

    @Override
    public String toString() {
        return "{" +
            " results='" + getResults() + "'" +
            ", quizzes='" + getQuizzes() + "'" +
            "}";
    }
}

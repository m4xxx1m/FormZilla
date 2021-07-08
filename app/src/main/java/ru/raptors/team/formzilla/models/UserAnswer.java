package ru.raptors.team.formzilla.models;

public class UserAnswer {
    public String questionID;
    public String answer;
    public User user;

    public UserAnswer(String answer, User user) {
        this.answer = answer;
        this.user = user;
    }

    public UserAnswer(String questionID, String answer, User user) {
        this.questionID = questionID;
        this.answer = answer;
        this.user = user;
    }
}

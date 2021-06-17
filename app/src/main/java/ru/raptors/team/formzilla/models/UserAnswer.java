package ru.raptors.team.formzilla.models;

public class UserAnswer {
    public String answer;
    public User user;

    public UserAnswer(String answer, User user) {
        this.answer = answer;
        this.user = user;
    }
}

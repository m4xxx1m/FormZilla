package ru.raptors.team.formzilla.interfaces;

import java.io.Serializable;

public interface OnAnsweredListener extends Serializable {
    void onAnswered(String answer, String userID);
}

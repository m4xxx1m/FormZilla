package ru.raptors.team.formzilla.interfaces;

import android.content.Context;

import java.io.Serializable;

public interface OnAnsweredListener extends Serializable {
    void onAnswered(String answer, String userID, Context context);
}

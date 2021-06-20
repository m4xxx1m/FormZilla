package ru.raptors.team.formzilla.interfaces;

import android.content.Context;

public interface Saveable {
    public void save(Context context);
    public void loadFromPhone(Context context);
}

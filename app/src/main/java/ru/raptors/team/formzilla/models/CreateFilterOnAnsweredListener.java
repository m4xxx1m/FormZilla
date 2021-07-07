package ru.raptors.team.formzilla.models;

import android.content.Context;

import ru.raptors.team.formzilla.enums.OnAnsweredListenerEnum;
import ru.raptors.team.formzilla.interfaces.OnAnsweredListener;

public class CreateFilterOnAnsweredListener implements OnAnsweredListener {

    public String category;
    public User user;

    @Override
    public void onAnswered(String answer, String userID, Context context) {
        Filter filter;
        filter = new Filter(answer, category);
        filter.staff.add(new User(userID));
        User user = User.getNowUser(context);
        user.addFilter(filter);
        user.uploadFiltersOnFirebase(context);
        user.save(context);
    }

    public void setCreatingFilterCategory(String category)
    {
        this.category = category;
    }

    public void setUserForFilterCreating(User user)
    {
        this.user = user;
    }

    @Override
    public String toString() {
        return "0" + "%%regex" + category + "%%regex" + user.getID() + "%%regex";
    }
}

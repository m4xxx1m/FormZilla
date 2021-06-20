package ru.raptors.team.formzilla.models;

import ru.raptors.team.formzilla.enums.OnAnsweredListenerEnum;
import ru.raptors.team.formzilla.interfaces.OnAnsweredListener;

public class CreateFilterOnAnsweredListener implements OnAnsweredListener {

    public String category;
    public User user;

    @Override
    public void onAnswered(String answer, String userID) {
        Filter filter;
        filter = new Filter(answer, category);
        filter.staff.add(new User(userID));
        user.addFilter(filter);
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
        return OnAnsweredListenerEnum.CreateFilter + " " + category + " " + user.getID() + " ";
    }
}

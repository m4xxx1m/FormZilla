package ru.raptors.team.formzilla.enums;

public enum OnAnsweredListenerEnum {
    CreateFilter;

    public static OnAnsweredListenerEnum fromInteger(int onAnsweredListenerEnum) {
        switch(onAnsweredListenerEnum) {
            case 0:
                return CreateFilter;
        }
        return null;
    }
}

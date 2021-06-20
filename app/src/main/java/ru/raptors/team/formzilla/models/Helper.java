package ru.raptors.team.formzilla.models;

import java.util.UUID;

public class Helper {
    // сюда выносятся вспомогательные методы

    public static String generateID()
    {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}

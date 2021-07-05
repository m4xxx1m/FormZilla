package ru.raptors.team.formzilla.models;

import java.text.Normalizer;
import java.util.UUID;
import java.util.regex.Pattern;

public class Helper {
    // сюда выносятся вспомогательные методы

    public static String generateID()
    {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String generateLogin() {
        String[] w1 = {"Super", "Mega", "Pro", "Unreal", "Anti", "", "Cool", "Big", "Little" ,"Mr.", "Alpha", "", "sA_", "Ultra",
                "MC.", "DJ.", "Pro100", "", "GG_", "SaD_", "Sad", "Cyber","Binary", "", "Black", "Anime", "White", "Trash", "Close",
                "", "Crazy", "Squeeze", "Easy", "Delta", "Gold", "Silver", "Emerald" };
        String[] w2 = {"God", "Killer", "Crab", "Sniper", "Hacker", "Daddy", "Dad", "Mum", "Ass", "Dick", "Lololo", "Qwerty", "King",
                "Noob", "Void", "Winner", "Lemon", "Pisos", "Meat", "Angel", "Nitro", "Ace", "Crusher", "Dragon", "Man", "Boy", "Hit",
                "Someone", "Cookies", "Noname", "Neko", "Schoolboy", "Kitten", "Chicken", "Time", "Hydra", "Acid", "Riper", "Volcano",
                "Gangster", "Chastener", "Droid", "Lucker", "Unlucker", "Legend", "Burger", "Penguin", "Crystal", "Agent", "Cock",
                "Rooster", "Storm", "Zadrot", "Gold", "Silver", "Emerald", "Robot", "Troll", "Doctor" };
        String[] w3 = {"228", "322", "666", "", "_X", "XXL", "1337", "", "123", "007", "_pro", "", "_YT", "_xD", "0_o", "", "^_^", "~",
                "<3", "", "Ox", "X", "=.=", "", "Pro" };
        int l1 = w1.length;
        int l2 = w2.length;
        int l3 = w3.length;
        int rand1 = (int) (Math.random() * l1);
        int rand2 = (int) (Math.random() * l2);
        int rand3 = (int) (Math.random() * l3);

        String nick = w1[rand1] + w2[rand2] +  w3[rand3];
        return nick;
    }

    public static String generatePassword() {
        char symbols[] = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                '1','2','3','4','5','6','7','8','9','0'};  // password symbols
        int len = 6;   // password length
        char[] pass = new char[len];
        for(int i = 0; i < len; i++) {
            pass[i] = symbols[((int)(Math.random() * 36))];
        }

        String password = new String(pass);
        return password;
    }
}

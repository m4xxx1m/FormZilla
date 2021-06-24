package ru.raptors.team.formzilla.models;

import ru.raptors.team.formzilla.enums.FormStatusEnum;
import ru.raptors.team.formzilla.enums.GenderEnum;

public class Gender {
    private GenderEnum genderEnum;

    public Gender(GenderEnum genderEnum) {
        this.genderEnum = genderEnum;
    }

    public Gender(String gender)
    {
        switch (gender)
        {
            case "Male":
            {
                genderEnum = GenderEnum.Male;
                break;
            }
            case "Female":
            {
                genderEnum = GenderEnum.Female;
                break;
            }
            case "Other":
            {
                genderEnum = GenderEnum.Other;
                break;
            }
        }
    }

    @Override
    public String toString() {
        String result = "";
        switch (genderEnum)
        {
            case Male:
            {
                result = "Male";
                break;
            }
            case Female:
            {
                result = "Female";
                break;
            }
            case Other:
            {
                result = "Other";
                break;
            }
        }
        return result;
    }

    public GenderEnum getGender() {
        return genderEnum;
    }

    public void setGender(GenderEnum genderEnum) {
        this.genderEnum = genderEnum;
    }
}

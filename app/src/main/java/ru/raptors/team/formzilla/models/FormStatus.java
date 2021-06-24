package ru.raptors.team.formzilla.models;

import ru.raptors.team.formzilla.enums.FormStatusEnum;

public class FormStatus {
    public FormStatusEnum formStatusEnum;

    public FormStatus(FormStatusEnum formStatusEnum) {
        this.formStatusEnum = formStatusEnum;
    }

    public FormStatus(String status)
    {
        switch (status)
        {
            case "Available":
            {
                formStatusEnum = FormStatusEnum.Available;
                break;
            }
            case "Passed":
            {
                formStatusEnum = FormStatusEnum.Passed;
                break;
            }
            case "Created":
            {
                formStatusEnum = FormStatusEnum.Created;
                break;
            }
        }
    }

    @Override
    public String toString() {
        String result = "";
        switch (formStatusEnum)
        {
            case Available:
            {
                result = "Available";
                break;
            }
            case Passed:
            {
                result = "Passed";
                break;
            }
            case Created:
            {
                result = "Created";
                break;
            }
        }
        return result;
    }

    public FormStatusEnum getFormStatus() {
        return formStatusEnum;
    }

    public void setFormStatus(FormStatusEnum formStatusEnum) {
        this.formStatusEnum = formStatusEnum;
    }
}

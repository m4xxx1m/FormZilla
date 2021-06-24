package ru.raptors.team.formzilla.models;

import ru.raptors.team.formzilla.enums.FormStatusEnum;
import ru.raptors.team.formzilla.enums.QuestionTypeEnum;

public class QuestionType {
    public QuestionTypeEnum questionTypeEnum;

    public QuestionType(QuestionTypeEnum questionTypeEnum) {
        this.questionTypeEnum = questionTypeEnum;
    }

    public QuestionType(String type) {
        switch (type) {
            case "SingleAnswer": {
                questionTypeEnum = QuestionTypeEnum.SingleAnswer;
                break;
            }
            case "MultiAnswer": {
                questionTypeEnum = QuestionTypeEnum.MultiAnswer;
                break;
            }
            case "TextAnswer": {
                questionTypeEnum = QuestionTypeEnum.TextAnswer;
                break;
            }
        }
    }

    @Override
    public String toString() {
        String result = "";
        switch (questionTypeEnum) {
            case SingleAnswer: {
                result = "SingleAnswer";
                break;
            }
            case MultiAnswer: {
                result = "MultiAnswer";
                break;
            }
            case TextAnswer: {
                result = "TextAnswer";
                break;
            }
        }
        return result;
    }

    public QuestionTypeEnum getQuestionType() {
        return questionTypeEnum;
    }

    public void setQuestionType(QuestionTypeEnum questionTypeEnum) {
        this.questionTypeEnum = questionTypeEnum;
    }
}

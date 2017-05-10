package appframe.appframe.dto;

import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2017-01-17.
 */

public class UserAskQuestion extends Http.BaseDto implements Serializable {
    public int AskUserId;
    public int AnswerUserId ;
    public String Question;
    public String Answer;

    public int getAskUserId() {
        return AskUserId;
    }

    public void setAskUserId(int askUserId) {
        AskUserId = askUserId;
    }

    public int getAnswerUserId() {
        return AnswerUserId;
    }

    public void setAnswerUserId(int answerUserId) {
        AnswerUserId = answerUserId;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }
}

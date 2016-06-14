package appframe.appframe.dto;

import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/5/23.
 */
public class AnswerDetailWithQuestionDetail extends Http.BaseDto implements Serializable {
    public Question Question;
    public AnswerDetail Answer;

    public appframe.appframe.dto.Question getQuestion() {
        return Question;
    }

    public void setQuestion(appframe.appframe.dto.Question question) {
        Question = question;
    }

    public AnswerDetail getAnswer() {
        return Answer;
    }

    public void setAnswer(AnswerDetail answer) {
        Answer = answer;
    }
}

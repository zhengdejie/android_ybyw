package appframe.appframe.dto;

import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2017-01-17.
 */

public class RecommendOrderQuestion extends Http.BaseDto implements Serializable {

    public int RecommendOrderId;
    public String Question;
    public String Answer;

    public int getRecommendOrderId() {
        return RecommendOrderId;
    }

    public void setRecommendOrderId(int recommendOrderId) {
        RecommendOrderId = recommendOrderId;
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

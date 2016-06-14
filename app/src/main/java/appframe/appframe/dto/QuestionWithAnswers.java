package appframe.appframe.dto;

import java.io.Serializable;
import java.util.List;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/5/18.
 */
public class QuestionWithAnswers extends Http.BaseDto implements Serializable {
    public Question QuestionDetail;
    public List<AnswerDetail> AnswerDetails ;

    public Question getQuestionDetail() {
        return QuestionDetail;
    }

    public void setQuestionDetail(Question questionDetail) {
        QuestionDetail = questionDetail;
    }

    public List<AnswerDetail> getAnswerDetails() {
        return AnswerDetails;
    }

    public void setAnswerDetails(List<AnswerDetail> answerDetails) {
        AnswerDetails = answerDetails;
    }
}

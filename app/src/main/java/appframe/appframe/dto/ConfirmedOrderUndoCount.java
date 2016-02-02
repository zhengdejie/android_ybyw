package appframe.appframe.dto;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/2/1.
 */
public class ConfirmedOrderUndoCount extends Http.BaseDto{
    public int OngoingCount;
    public int PendingCount;
    public int UnReviewedCount;

    public int getOngoingCount() {
        return OngoingCount;
    }

    public void setOngoingCount(int ongoingCount) {
        OngoingCount = ongoingCount;
    }

    public int getPendingCount() {
        return PendingCount;
    }

    public void setPendingCount(int pendingCount) {
        PendingCount = pendingCount;
    }

    public int getUnReviewedCount() {
        return UnReviewedCount;
    }

    public void setUnReviewedCount(int unReviewedCount) {
        UnReviewedCount = unReviewedCount;
    }
}

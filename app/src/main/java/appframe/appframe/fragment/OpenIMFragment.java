package appframe.appframe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.mobileim.aop.Advice;
import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;
import com.alibaba.mobileim.aop.Pointcut;

import appframe.appframe.R;
import appframe.appframe.activity.MyMessageActivity;
import appframe.appframe.utils.ConversationListUICustomSample;
import appframe.appframe.utils.OpenConversationSampleHelper;

/**
 * Created by Administrator on 2017/7/28.
 */

public class OpenIMFragment  extends BaseFragment implements View.OnClickListener{
    View root;

    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        root  = inflater.inflate(R.layout.fragment_empty, null);
//        startActivity(OpenConversationSampleHelper.getOpenConversationListIntent_Sample(this));


        ConversationListUICustomSample conversationListUICustomSample = new ConversationListUICustomSample(new Pointcut() {
            @Override
            public void registerAdvice(Advice advice) {

            }
        });

        root = conversationListUICustomSample.getCustomEmptyViewInConversationUI(getActivity());
//        AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_UI_POINTCUT, ConversationListUICustomSample.class);
//        init();
        return root;

    }

    public void init()
    {
//        startActivity(new Intent(getActivity(),ConversationListUICustomSample.class));
    }

    @Override
    public void onClick(View v) {

    }
}

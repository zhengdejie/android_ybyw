package appframe.appframe.utils;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMKit;

/**
 * 打开最近会话列表
 * @author zhaoxu
 *
 */
public class OpenConversationSampleHelper {
	/**
	 * 获取打开最近会话列表界面的Intent
	 * 
	 * @param context
	 */
	public static Intent getOpenConversationListIntent_Sample(Activity context){
		YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
		if (imKit == null) {
			Toast.makeText(context, "未初始化", Toast.LENGTH_SHORT).show();
			return null;
		}

		Intent intent = imKit.getConversationActivityIntent();
		
		return intent;
	}
}

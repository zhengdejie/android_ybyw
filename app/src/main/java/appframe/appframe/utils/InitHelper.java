package appframe.appframe.utils;

import android.app.Application;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.wxlib.util.SysUtil;

/**
 * SDK
 * 
 * @author shuheng
 */
public class InitHelper {

	public static void initYWSDK(Application application){

		LoginSampleHelper.getInstance().initSDK_Sample(application);


		YWAPI.enableSDKLogOutput(true);

//		IYWContactService.enableBlackList();

		YWAPI.setEnableAutoLogin(true);
	}
}

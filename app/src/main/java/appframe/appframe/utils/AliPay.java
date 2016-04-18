package appframe.appframe.utils;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import appframe.appframe.app.AppConfig;

/**
 * Created by Administrator on 2016/2/3.
 */
public class AliPay {



    public static String getOrderInfo(String subject, String body, String price, String ConfirmOrderID) {

        SortedMap<String, String> map = null;
        map = new  TreeMap<String,String>();

        // 签约合作者身份ID
        map.put("partner",AppConfig.PARTNER);
        // 签约卖家支付宝账号
        map.put("seller_id",AppConfig.SELLER);
        // 商户网站唯一订单号
        map.put("out_trade_no",ConfirmOrderID);
        // 商品名称
        map.put("subject",subject);
        // 商品详情
        map.put("body",body);
        // 商品金额
        map.put("total_fee",price);
        // 服务器异步通知页面路径
        map.put("notify_url","http://notify.msp.hk/notify.htm");
        // 服务接口名称， 固定值
        map.put("service","mobile.securitypay.pay");
        // 支付类型， 固定值
        map.put("payment_type","1");
        // 参数编码， 固定值
        map.put("_input_charset","utf-8");
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        map.put("it_b_pay","30m");
        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        map.put("return_url","m.alipay.com");
        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        StringBuilder sb= new StringBuilder();
        for(Map.Entry<String,String> order : map.entrySet()){
            sb.append(order.getKey() + "=" + "\"" + order.getValue() + "\"");
            sb.append("&");
        }

        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public  static String getSignType() {
        return "sign_type=\"RSA\"";
    }
}

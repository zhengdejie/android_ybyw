package appframe.appframe.app;

/**
 * Created by dashi on 15/6/21.
 */
public class AppConfig {
    //public static final String QINIU_UPLOAD_TOKEN = "LZqbvXYHI_JuwUXDkaj0aGRYvdIXhFNg1EIpY7Hf:TbPvUXzQTExyHoXF4KBRNHMrzu4=:eyJzY29wZSI6ImRldi10ZXN0IiwiZGVhZGxpbmUiOjE1MTQ3NjQ4MDB9";

    public static final String QINIU_HOST = "http://7xo9lr.com1.z0.glb.clouddn.com/";
    public static final String QINIU_APKVERSION = "APK_Version.txt";

    public static final int MAX_BITMAP_WIDTH = 1280;
    public static final int MAX_BITMAP_HEIGHT = 1280;

    public static final int AVATAR_BITMAP_WIDTH = 70;
    public static final int AVATAR_BITMAP_HEIGHT = 70;

    public static final int ORDER_SIZE = 10;
    public static final String ORDERSTATUS_MAIN = "主页";
    public static final String ORDERSTATUS_PROGRESS = "进行中";
    public static final String ORDERSTATUS_CLOSE = "已关闭";
    public static final String ORDERSTATUS_DONE = "已完成";
    public static final String ORDERSTATUS_APPLY = "已申请";
    public static final String ORDERSTATUS_DELETE = "delete";

    public static boolean RECEIVE_NOTIFICATION = true;
}

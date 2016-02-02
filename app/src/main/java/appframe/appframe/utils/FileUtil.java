package appframe.appframe.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2015/12/30.
 */
public class FileUtil {
    public static File updateDir = null;
    public static File updateFile = null;

    /***
     * 创建文件
     */
    public static void createFile(String name) {
        if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
                .getExternalStorageState())) {
            updateDir = new File(Environment.getExternalStorageDirectory()
                    + "/" + "ybw");
            updateFile = new File(updateDir + "/" + name + ".apk");

            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }
            if (!updateFile.exists()) {
                try {
                    updateFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

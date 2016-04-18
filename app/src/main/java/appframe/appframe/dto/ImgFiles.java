package appframe.appframe.dto;

import android.graphics.Bitmap;

import java.io.File;
import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/4/7.
 */
public class ImgFiles extends Http.BaseDto implements Serializable {
    private File file;
    private String name;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

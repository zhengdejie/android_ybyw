package appframe.appframe.dto;

import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2017-01-05.
 */

public class Recommend extends Http.BaseDto implements Serializable {

    private int Id;
    private String Backgroud;
    private String Title;
    private String Content;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getBackgroud() {
        return Backgroud;
    }

    public void setBackgroud(String backgroud) {
        Backgroud = backgroud;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}

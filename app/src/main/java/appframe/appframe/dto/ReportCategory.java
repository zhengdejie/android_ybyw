package appframe.appframe.dto;

import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/3/22.
 */
public class ReportCategory extends Http.BaseDto implements Serializable {
    private int Id;
    private String CategoryName;
    private String Check = "UnCheck";

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCheck() {
        return Check;
    }

    public void setCheck(String check) {
        Check = check;
    }
}

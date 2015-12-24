package appframe.appframe.dto;


import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/12/22.
 */
public class SelfEvaluationDetail extends Http.BaseDto  {

    public String Description;
    public String Photos;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPhotos() {
        return Photos;
    }

    public void setPhotos(String photos) {
        Photos = photos;
    }
}

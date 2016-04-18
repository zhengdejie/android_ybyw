package appframe.appframe.dto;

import java.io.Serializable;
import java.util.List;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/3/9.
 */
public class SearchOrderTagResponse extends Http.BaseDto implements Serializable {
    private List<String> TagNames;

    public List<String> getTagNames() {
        return TagNames;
    }

    public void setTagNames(List<String> tagNames) {
        TagNames = tagNames;
    }
}

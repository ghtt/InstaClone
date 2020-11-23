
package com.akrasnoyarov.instaclone.api;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("data")
    @Expose
    private List<UserMedia> data = null;
    @SerializedName("paging")
    @Expose
    private Paging paging;

    public List<UserMedia> getData() {
        return data;
    }

    public void setData(List<UserMedia> data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

}

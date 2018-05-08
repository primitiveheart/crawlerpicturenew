package com.entity;

/**
 * Created by admin on 2018/4/26.
 */
public class Crawler {
    //id
    private Long id;
    //url表的id,用-1表示该id不存在
    private Integer urlId;
    //keyword表中的id，用-1表示该id不存在
    private Integer keywordId;
    //图片的url
    private String pictureURL;
    //图片的名称
    private String pictureName;
    //图片的描述
    private String pictureDescription;
    //网页的url
    private String webURL;


    public Crawler(Long id, Integer urlId, Integer keywordId, String pictureURL, String pictureName, String pictureDescription, String webURL) {
        this.id = id;
        this.urlId = urlId;
        this.keywordId = keywordId;
        this.pictureURL = pictureURL;
        this.pictureName = pictureName;
        this.pictureDescription = pictureDescription;
        this.webURL = webURL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUrlId() {
        return urlId;
    }

    public void setUrlId(Integer urlId) {
        this.urlId = urlId;
    }

    public Integer getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Integer keywordId) {
        this.keywordId = keywordId;
    }

    public Crawler(){}

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureDescription() {
        return pictureDescription;
    }

    public void setPictureDescription(String pictureDescription) {
        this.pictureDescription = pictureDescription;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }
}

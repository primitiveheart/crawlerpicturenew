package com.entity;

import java.io.InputStream;

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
    //标题的频率
    private Integer titleFrequence;
    //正文的频率
    private Integer bodyFrequence;
    //图片的来源,0:表示百度图片，1：表示百度搜索，2：表示某个网站
    private Integer pictureSource;


    public Crawler(Long id, Integer urlId, Integer keywordId, String pictureURL, String pictureName,
                   String pictureDescription, String webURL, Integer titleFrequence, Integer bodyFrequence
                ,Integer pictureSource) {
        this.id = id;
        this.urlId = urlId;
        this.keywordId = keywordId;
        this.pictureURL = pictureURL;
        this.pictureName = pictureName;
        this.pictureDescription = pictureDescription;
        this.webURL = webURL;
        this.titleFrequence = titleFrequence;
        this.bodyFrequence = bodyFrequence;
        this.pictureSource = pictureSource;
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

    public Integer getTitleFrequence() {
        return titleFrequence;
    }

    public void setTitleFrequence(Integer titleFrequence) {
        this.titleFrequence = titleFrequence;
    }

    public Integer getBodyFrequence() {
        return bodyFrequence;
    }

    public void setBodyFrequence(Integer bodyFrequence) {
        this.bodyFrequence = bodyFrequence;
    }

    public Integer getPictureSource() {
        return pictureSource;
    }

    public void setPictureSource(Integer pictureSource) {
        this.pictureSource = pictureSource;
    }
}

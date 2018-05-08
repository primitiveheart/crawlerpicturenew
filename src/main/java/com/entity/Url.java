package com.entity;

/**
 * Created by admin on 2018/5/7.
 */
public class Url {
    private Integer id;
    private String urlSite;
    private String keyword;
    //是否搜索，1表示搜索，0表示不搜索
    private String isSearch;
    //是否新增的，1表示新增，0表示旧的
    private String isNew;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrlSite() {
        return urlSite;
    }

    public void setUrlSite(String urlSite) {
        this.urlSite = urlSite;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getIsSearch() {
        return isSearch;
    }

    public void setIsSearch(String isSearch) {
        this.isSearch = isSearch;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }
}

package com.entity;

/**
 * Created by admin on 2018/5/6.
 */
public class Keyword {
    //id
    private Integer id;
    //关键字
    private String keyword;
    //是否搜索，1表示搜索，0表示不搜索 默认是不搜索的
    private String isSearch;
    //该关键字是否的新增的，1表示新增的，0表示已经存在
    private String isNew;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

package com.mapper;

import com.entity.Url;
import org.apache.ibatis.annotations.Param;

import java.net.URL;
import java.util.List;

/**
 * Created by admin on 2018/5/7.
 */
public interface UrlMapper {
    //查询出已有的地址
    List<Url> listUrl();

    List<Url> getKeywordByUrlSite(String urlSite);

    List<Url> getKeywordByIsSearch(String isSearch);

    List<Url> getUrlByIsNew(String isNew);

    Url getUrlByKeyword(String keyword);

    Url getUrlByUrlSiteAndKeyword(@Param("urlSite")String urlSite, @Param("keyword")String keyword);

    void batchUpdateUrlByUrlSite(List<Url> urls);

    void batchInsertUrl(List<Url> urls);
}

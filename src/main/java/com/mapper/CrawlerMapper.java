package com.mapper;

import com.entity.Crawler;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by admin on 2018/5/6.
 */
public interface CrawlerMapper {

    /**
     * 进行批量的插入
     * @param crawlers
     */
    void batchInsertCrawler(List<Crawler> crawlers);

    List<Crawler> listAllCrawler();

    Long getCrawlerTotalBySearchKeywordId(@Param("keywordId") Integer keywordId,
                                          @Param("urlId") Integer urlId);

    Long getCrawlerTotalByCommonSearchKeywordId(@Param("keywordId") Integer keywordId,
                                          @Param("urlId") Integer urlId);

    List<Crawler> getPageBySearchKeywordId(@Param("start") Integer start,
                                         @Param("length") Integer length,
                                         @Param("keywordId") Integer keywordId,
                                         @Param("urlId") Integer urlId);
    List<Crawler> getPageByCommonSearchKeywordId(@Param("start") Integer start,
                                           @Param("length") Integer length,
                                           @Param("keywordId") Integer keywordId,
                                           @Param("urlId") Integer urlId);
}

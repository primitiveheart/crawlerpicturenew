package com.timer;

import com.algorithm.BaiduPictureCrawler;
import com.algorithm.BaiduSearchResultCrawler;
import com.algorithm.WebCrawler;
import com.entity.Keyword;
import com.entity.Url;
import com.mapper.KeywordMapper;
import com.mapper.UrlMapper;
import com.sun.media.sound.UlawCodec;
import com.util.Utils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by admin on 2018/5/6.
 */
public class CrawlerTimerTask extends TimerTask {

    private ServletContextEvent servletContextEvent;

    public CrawlerTimerTask(ServletContextEvent servletContextEvent){
        this.servletContextEvent = servletContextEvent;
    }

    @Override
    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        //该方法中需要开辟多个线程
        //核心内容
        final WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
        final String path = webApplicationContext.getServletContext().getRealPath("/resources/img");
        final KeywordMapper keywordMapper = webApplicationContext.getBean(KeywordMapper.class);
        final List<Keyword> keywords = keywordMapper.getKeywordByIsNew("1");
        final UrlMapper urlMapper = webApplicationContext.getBean(UrlMapper.class);


        try{
            //第一部分:百度搜索和百度搜索
            //第一步:百度图片
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    BaiduPictureCrawler baiduPictureCrawler = webApplicationContext.getBean(BaiduPictureCrawler.class);
                    if(keywords != null &&keywords.size() > 0){
                        for(int i=0; i < keywords.size(); i++){
                            //爬取百度图片数据
                            baiduPictureCrawler.insertBaiduPicResult(keywords.get(i).getKeyword(), keywords.get(i).getId(), path);
                        }
                    }
                }
            });

            //第二步: 百度搜索
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    BaiduSearchResultCrawler baiduSearchResultCrawler = webApplicationContext.getBean(BaiduSearchResultCrawler.class);
                    if(keywords != null &&keywords.size() > 0){
                        //开始爬取数据
                        for(int i=0; i < keywords.size(); i++){
                            //爬取百度搜索数据
                            baiduSearchResultCrawler.insertBaiduSearchResult(keywords.get(i).getKeyword(), keywords.get(i).getId());
                        }
                    }
                }
            });
//            第二部分:对某个网站进行搜索
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    List<Url> urls = urlMapper.getUrlByIsNew("1");

                    if(urls != null && urls.size() > 0){
                        WebCrawler webCrawler = webApplicationContext.getBean(WebCrawler.class);
                        for(int i=0 ;i < urls.size(); i++){
                            webCrawler.insertWebSearchResult(urls.get(i).getUrlSite(), urls.get(i).getKeyword(), urls.get(i).getId());
                        }
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭线程
            executorService.shutdown();
        }

        //线程是否关闭
        //第三部分，需要对新爬虫完的关键字，在url和keyword表进行更新isNew状态为0
        while(true){
            if(executorService.isTerminated()){
                try{
                    //第一步:先查询出来isNew= 1的
                    List<Keyword> keywordList = keywordMapper.getKeywordByIsNew("1");
                    if(keywordList != null && keywordList.size() > 0){
                        for(int i= 0; i < keywordList.size(); i++){
                            keywordList.get(i).setIsNew("0");
                        }

                        //第二步进行更新isNew =0;
                        keywordMapper.batchUpdateKeyword(keywordList);
                    }
                    List<Url> urlList = urlMapper.getUrlByIsNew("1");
                    if(urlList != null &&urlList.size() > 0){
                        for(int i=0; i < urlList.size(); i++){
                            urlList.get(i).setIsNew("0");
                        }
                        //第二步进行更新isNew =0;
                        urlMapper.batchUpdateUrlByUrlSite(urlList);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    break;
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

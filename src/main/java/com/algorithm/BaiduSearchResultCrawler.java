package com.algorithm;

import com.entity.Crawler;
import com.mapper.CrawlerMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2018/4/24.
 */
@Component
public class BaiduSearchResultCrawler {

    @Autowired
    private CrawlerMapper crawlerMapper;


    public void insertBaiduSearchResult(String keyword, Integer id) {
        List<String> urls = new ArrayList<>();
        List<Crawler> crawlers = new ArrayList<Crawler>();
        try{
            //该例子是 java爬虫
            urls = getAllCrawlerResultURLByKeyword(keyword);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        for(int i=0 ; i<urls.size(); i++){
            try{
                List<Crawler> middle = Utils.getHtmlPicRelateContentByURL(urls.get(i), "", id);
                crawlers.addAll(middle);
                if(i % 10 == 0 && crawlers != null && crawlers.size() > 0){
                    crawlerMapper.batchInsertCrawler(crawlers);
                }
            }catch (Exception e){
                throw new RuntimeException(e);
            }finally {
                crawlers.clear();
            }
        }

        try{
            crawlerMapper.batchInsertCrawler(crawlers);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            crawlers.clear();
        }

    }

    //得到所有的url
    public List<String> getAllCrawlerResultURLByKeyword(String keyword) {
        List<String> urls = new ArrayList<>();
        try{
            String url = createUrl(keyword,1);
            String html = Utils.getHtml(url);
            urls = getHtmlRealUrl(html);
            String nextPageUrl = getNextPageUrl(html);
            while(nextPageUrl != ""){
                String nextPageHtml = Utils.getHtml(nextPageUrl);
                List<String> sectionUrls = getHtmlRealUrl(nextPageHtml);
                urls.addAll(sectionUrls);
                nextPageUrl = getNextPageUrl(nextPageHtml);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return urls;
    }

    //得到下一页的url
    public String getNextPageUrl(String html){
        String url = "";
        Pattern pattern = Pattern.compile("href\\=\\\"(\\/s\\?wd\\=[\\w\\d\\%\\&\\=\\_\\-]*?)\\\" class\\=\\\"n\\\"");
        Matcher matcher = pattern.matcher(html);
        while(matcher.find()){
            url = "http://www.baidu.com" +  matcher.group(1);
        }
        return url;
    }

    //得到url是存在广告的
    public  List<String> getHtmlUrl(String html) throws IOException{
        //<a.*?href=["']?((https?://)?/?[^"']+)["']?.*?>(.+)</a>
        //"href=[\"']?((https?://)?/?[^\"']+)[\"']?.*?>(.+)"
        //href\=\"(http\:\/\/www\.baidu\.com\/link\?url\=.*?)\" class\=\"c\-showurl\"'
        //href=\"(http://www\.baidu\.com/link\?url=.*?)\" class=\"c\-showurl\"
        Matcher matcher = Pattern.compile("\"(http://www\\.baidu\\.com/link\\?url=.*?)\\\" class=\\\"c\\-showurl\\\"").matcher(html);
        List<String> listUrl = new ArrayList<String>();
        while(matcher.find()){
            listUrl.add(matcher.group());
        }

        List<String> realUrls = new ArrayList<String>();
        for(int i=0; i < listUrl.size(); i++){
            HttpURLConnection connection = Utils.getHttpURLConnection(listUrl.get(i).split(" ")[0].replaceAll("\"",""));
            connection.setInstanceFollowRedirects(false);
            connection.connect();
            if(connection.getResponseCode() == 302){
                realUrls.add(connection.getHeaderField("Location"));
            }
        }
        return realUrls;
    }

    //这是去重广告的url
    public  List<String> getHtmlRealUrl(String html) throws IOException{
        List<String> listUrl = new ArrayList<String>();
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("div[tpl=se_com_default]");
        for(int i=0; i<elements.size(); i++){
            String divString = elements.get(i).toString();
            Matcher matcher = Pattern.compile("\"(http://www\\.baidu\\.com/link\\?url=.*?)\\\" class=\\\"c\\-showurl\\\"").matcher(divString);
            while (matcher.find()){
                listUrl.add(matcher.group());
            }
        }

        //得到的是真的的url
        List<String> realUrls = new ArrayList<String>();
        for(int i=0; i<listUrl.size(); i++){
            String realUrl = listUrl.get(i).split(" ")[0].replaceAll("\"","");
            String redirectAfterUrl = Utils.getURLByRedirect(realUrl);
            if (redirectAfterUrl != "") {
                realUrls.add(redirectAfterUrl);
            }
        }
        return  realUrls;
    }

    //创建url的字符串
    public  String createUrl(String keyword, int pageNum) throws Exception{
        int pn = (pageNum - 1) * 10;
        keyword = URLEncoder.encode(keyword, "utf-8");
        return String.format("http://www.baidu.com/s?wd=%s&pn=%s", keyword, pn);
    }
}

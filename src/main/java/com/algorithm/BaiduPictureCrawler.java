package com.algorithm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.entity.Crawler;
import com.mapper.CrawlerMapper;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/4/27.
 */
@Component
public class BaiduPictureCrawler {

    private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";

    @Autowired
    private CrawlerMapper crawlerMapper;

    public void insertBaiduPicResult(String keyword, Integer id) {
        try{
            //该示例是：污水处理厂
            JSONObject jsonObject = getJsonObject("污水处理厂", 0);
            String total = jsonObject.getString("bdFmtDispNum").replace("约", "").replace(",","");
            for(int i=0; i < Integer.parseInt(total)/30; i++){
                List<Crawler> crawlersTemp = getPictureURL("污水处理厂", i, id);
                crawlerMapper.batchInsertCrawler(crawlersTemp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Crawler> getPictureURL(String keyword, int pn, Integer id) throws Exception{
        List<Crawler> crawlers = new ArrayList<Crawler>();
        JSONObject json = getJsonObject(keyword, pn);
        JSONArray array = JSONArray.parseArray(json.getString("data"));
        try{
            for(int i=0; i<array.size() - 1; i++){
                String fromURL = "";
                String objectURL = "";
                JSONObject jsonObject = JSONObject.parseObject(array.getString(i));
                JSONArray replaceUrl = (JSONArray) JSONArray.parse(jsonObject.getString("replaceUrl"));
                if(replaceUrl != null){
                    if(replaceUrl.size() == 1){
                        JSONObject middle = JSONObject.parseObject(replaceUrl.getString(0));
                        fromURL = middle.getString("FromURL");
                        objectURL = middle.getString("ObjURL");
                    }else if(replaceUrl.size() == 2){
                        JSONObject middle = JSONObject.parseObject(replaceUrl.getString(1));
                        fromURL = middle.getString("FromURL");
                        objectURL = middle.getString("ObjURL");
                    }
                    String thumbURL = jsonObject.getString("thumbURL");
                    String fromPageTitleEnc = jsonObject.getString("fromPageTitleEnc");

                    HttpURLConnection picSourceConn = Utils.getHttpURLConnection(fromURL);

                    if(picSourceConn != null && picSourceConn.getResponseCode() != 404){
                        String html = getHtml(picSourceConn);
                        if(!StringUtil.isBlank(html)){
                            Document document = Jsoup.parse(html);
//                           Elements element = document.select("div p img").attr("src", objectURL);
                            Elements element = document.select("div p img");
                            if(element.size() != 0){
                                Element parent = element.get(0).parent();
                                String description = "";
                                Element pre = parent.previousElementSibling();
                                if(pre != null && pre.hasText()){
                                    description = pre.text();
                                }else{
                                    Element next = parent.nextElementSibling();
                                    if(next != null && next.hasText()){
                                        description = next.text();
                                    }
                                }
                                Crawler crawler = new Crawler();
                                crawler.setKeywordId(id);
                                crawler.setUrlId(-1);
                                crawler.setPictureURL(thumbURL);
                                crawler.setWebURL(fromURL);
                                crawler.setPictureDescription(description);
                                crawler.setPictureName(fromPageTitleEnc);
                                crawlers.add(crawler);
                            }
                        }
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
//        System.out.println(json);

        return crawlers;
    }

    public JSONObject getJsonObject(String keyword, int pn) throws Exception {
        String url = "https://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&ct=201326592&is=&fp=result&" +
                "queryWord=" + keyword + "&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=&ic=0&word=" + keyword + "&s=&se=&tab=&" +
                "width=&height=&face=0&istype=2&qc=&nc=1&fr=&pn=" + pn * 30 + "&rn=30";

        HttpURLConnection connection = Utils.getHttpURLConnection(url);

        InputStream in = connection.getInputStream();
        byte[] data = readInputStream(in);
        String dataStr = new String(data);
        return JSONObject.parseObject(dataStr);
    }


    public String getHtml(HttpURLConnection connection)throws Exception{
        String line = "";
        StringBuffer sb = new StringBuffer();
        if(connection.getResponseCode() == 200){
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null){
                sb.append(line);
                sb.append("\n");
            }
        }

        return sb.toString();
    }


    public byte[] readInputStream(InputStream in)throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) != -1){
            out.write(buffer, 0, len);
        }
        in.close();
        return out.toByteArray();
    }
}

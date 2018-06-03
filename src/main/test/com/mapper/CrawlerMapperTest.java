package com.mapper;

import com.entity.Crawler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 2018/6/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/springmvc.xml")
public class CrawlerMapperTest {


    @Test
    public void batchInsertCrawler() throws Exception {
        List<Crawler> crawlerList = new ArrayList<>();
        Crawler crawler = new Crawler();
        crawler.setPictureName("sfdgdfg");
        crawler.setBodyFrequence(1);
        crawler.setUrlId(-1);

        crawlerList.add(crawler);
    }

}
package com.mapper;

import com.entity.Keyword;

import java.util.List;

/**
 * Created by admin on 2018/5/6.
 */
public interface KeywordMapper {

    List<Keyword> listKeyword();

    List<Keyword> getKeywordByIsSearch(String isSearch);

    List<Keyword> getKeywordByIsNew(String isNew);

    Keyword getKeywordBykeyword(String keyword);

    void batchUpdateKeyword(List<Keyword> keywords);

    void batchInsertkeyword(List<Keyword> keywords);
}

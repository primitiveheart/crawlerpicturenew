<html>
    <head>
        <meta charset="utf-8">
        <title>主页</title>

        <link href="resources/css/semantic.min.css" rel="stylesheet" type="text/css">


        <script src="resources/js/jquery-3.2.1.js" language="JavaScript"></script>
        <script src="resources/js/semantic.min.js" language="JavaScript"></script>
    </head>
    <body>
    <div class="ui top attached tabular menu">
        <a class="active item" data-tab="baidu">百度</a>
        <a class="item" data-tab="someweb">网站列表</a>
        <a class="item" data-tab="result">查看上一次的结果</a>
    </div>
    <div class="ui bottom attached active tab segment" data-tab="baidu">
        <div class="ui sub header">已有的关键字</div>
        <select multiple="" class="ui fluid normal dropdown keywords">
        <#if keywords?? && keywords?size gt 0>
            <#list keywords as k>
                <option value="${k.keyword}">${k.keyword}</option>
            </#list>
        </#if>
        </select>
        <div class="ui sub header">添加新的关键字</div>
        <div class="ui input">
            <input class="keyword" type="text" placeholder="新增关键字">
            <button class="ui green button keyword_new_add">添加关键字</button>
        </div>
        <div class="ui sub header">显示新增的关键字</div>
        <div class="ui multiple selection dropdown keywords_new_add">
            <input class="keywords_input" type="hidden" value="">
            <i class="dropdown icon"></i>
            <div class="default text">显示新增的关键字</div>
            <div class="menu">
            </div>
        </div>
        <div class="ui divider"></div>
        <button class="ui green button baidu_submit" >提交</button>
    </div>

    <div class="ui bottom attached tab segment" data-tab="someweb">
        <div class="ui sub header">已有的网址</div>
        <select multiple="" class="ui fluid normal dropdown urls">
        <#if urls?? && urls?size gt 0>
            <#list urls as u>
                <option value="${u.urlSite}">${u.urlSite}</option>
            </#list>
        </#if>
        </select>

        <div class="ui sub header">该网址对应的关键字</div>
        <div class="ui multiple selection dropdown url_keywords">
            <input class="url_keywords_input" type="hidden" value="">
            <i class="dropdown icon"></i>
            <div class="default text">该网址对应的关键字</div>
            <div class="menu">
            </div>
        </div>

        <div class="ui sub header">对该网址进行添加关键字</div>
        <div class="ui input">
            <input type="text" class="already_url_keyword_input">
            <button class="ui green button already_url_keyword_new_add">旧网站添加新的关键字</button>
        </div>

        <div class="ui sub header">显示该网址进行添加关键字</div>
        <div class="ui multiple selection dropdown url_keywords_new_add">
            <input class="url_keywords_new_add_input" type="hidden" value="">
            <i class="dropdown icon"></i>
            <div class="default text">显示该网址进行添加关键字</div>
            <div class="menu">
            </div>
        </div>

        <div class="ui divider"></div>

        <div class="ui sub header">添加新增的url(某个网站)(目前只能添加一个新的网址)</div>
        <div class="ui input">
            <input class="url_input" type="text" placeholder="新增的url">
            <button class="ui green button url_new_add">添加网址</button>
        </div>

        <div class="ui sub header">显示新增的网址</div>
        <div class="ui multiple selection dropdown urls_new_add">
            <input class="urls_input" type="hidden" value="">
            <i class="dropdown icon"></i>
            <div class="default text">显示新增的网址</div>
            <div class="menu">
            </div>
        </div>

        <div class="ui sub header">对新网址进行添加关键字</div>
        <div class="ui input">
            <input type="text" class="new_url_keyword_input" placeholder="新增的关键字">
            <button class="ui green button new_url_keyword_new_add">新网站添加新的关键字</button>
        </div>

        <div class="ui sub header">显示新增的网址对应的关键字</div>
        <div class="ui multiple selection dropdown new_url_new_add_keyword_new_add">
            <input class="new_url_new_add_keyword_input" type="hidden" value="">
            <i class="dropdown icon"></i>
            <div class="default text">显示新增的网址对应的关键字</div>
            <div class="menu">
            </div>
        </div>

        <div class="ui divider"></div>
        <button class="ui green button someweb_submit" >提交</button>
    </div>

    <div class="ui bottom attached tab segment" data-tab="result">
        <div class="ui header">搜索的关键字</div>
        <div class="ui  dropdown all_search_keyword">
            <input class="all_search_keyword_input" type="hidden" value="">
            <i class="dropdown icon"></i>
            <div class="default text">搜索的关键字</div>
            <div class="menu">
            </div>
        </div>
        <div class="ui sub header">该搜索的关键字对应的结果</div>
        <div class="ui items">
            <#--<#if crawlers?? && crawlers?size gt 0>-->
                <#--<#list crawlers as c>-->
                    <#--<div class="item">-->
                        <#--<div class="image" style="width: 100;height: 100">-->
                            <#--<img src="${c.pictureURL}">-->
                        <#--</div>-->
                        <#--<div class="content">-->
                            <#--<a class="header">${c.pictureName}</a>-->
                            <#--<div class="meta">-->
                                <#--<span><a href="${c.webURL}">该条信息来源于</a></span>-->
                            <#--</div>-->
                            <#--<div class="description">-->
                                <#--<p>${c.pictureDescription}</p>-->
                            <#--</div>-->
                        <#--</div>-->
                    <#--</div>-->
                <#--</#list>-->
            <#--</#if>-->
        </div>
    </div>

    <script language="JavaScript">
        $(document).ready(function(){
            $('.menu .item').tab();

            var alreadyKeyword = [];

            var newAddKeyword = [];

            var already_Url = "";

            var alreadyUrl_Keyword = []

            var alreayUrl_newKeyword = [];

            var newAddUrl = [];

            var newAddUrlKeyword = [];
            //显示新增的关键字
           var keywordsnewAddDropdown = $(".keywords_new_add").dropdown();

            //新增关键字
            $(".keyword_new_add").on("click", function () {
                var keyword = $(".keyword").val();
                if(keyword != ""){
                    var keywordsInputValue = $(".keywords_input").val();
                    var $div = $("<div class='item' data-value='" + keyword + "'>" + keyword + "</div>");
                    $div.appendTo($(".keywords_new_add .menu"));
                    if(keywordsInputValue == ""){
                        keywordsInputValue += keyword;
                    }else{
                        keywordsInputValue += "," + keyword;
                    }

                    $(".keywords_input").val(keywordsInputValue);
                    keywordsnewAddDropdown.dropdown({
                        onLabelRemove:function(value){
                            newAddKeyword.splice($.inArray(value, newAddKeyword), 1);
                        }
                    });
                    //进行清空
                    $(".keyword").val("");

                    //进行添加关键字
                    if(newAddKeyword.indexOf(keyword) < 0){
                        newAddKeyword.push(keyword);
                    }
                }else{
                    //进行提示新增关键字不能空
                }
            })

            //下拉框
            $(".keywords").dropdown({
                onAdd:function(addedValue, addedText, $addedChoice){
                    alreadyKeyword.push(addedValue);
                },
                onLabelRemove:function(value){
                    alreadyKeyword.splice($.inArray(value, alreadyKeyword), 1);
                }
            });

            $(".baidu_submit").on("click", function () {
                var already = alreadyKeyword.join(",");
                var newAdd = newAddKeyword.join(",");
                $.ajax({
                    url:"submitKeyword.html",
                    async:false,
                    type:"get",
                    data:{
                        already: already,
                        newAdd: newAdd
                    },
                    success:function(){
                        //刷新
                        $(location).attr("href", "home.html");
                    }
                })
            })

            //--------------------网址------------------

            var urlKeywordsDropdoen = $(".url_keywords").dropdown();

            $(".urls").dropdown({
                maxSelections: 1,
                onChange:function(value, text, $choice){
                    var urlSiteMiddle = text;
                    already_Url = text;
                    $.ajax({
                        url:"getKeywordByUrlSite.html",
                        async:false,
                        data:{
                            urlSite:urlSiteMiddle
                        },
                        type:"get",
                        success:function(result){
                            if(result != "" && result.urlKeywords != "" && result.urlKeywords.length > 0){
                                for(var i=0; i < result.urlKeywords.length;i++){
                                    var middle = result.urlKeywords[i].keyword;
                                    var $div = $("<div class='item' data-value='" + middle + "'>" + middle + "</div>");
                                    $div.appendTo($(".url_keywords .menu"));
                                }
                                $(".url_keywords").dropdown({
                                    onChange:function(value, text, $choice){
                                        alreadyUrl_Keyword.push(text);
                                    },
                                    onLabelRemove:function(value){
                                        alreadyUrl_Keyword.splice($.inArray(value, alreadyUrl_Keyword), 1);
                                    }
                                });

                            }
                        }
                    })
                }
            });


            //对旧的网址进行添加新的关键字
            $(".already_url_keyword_new_add").on("click", function () {
                var alreadyUrlNewAddKeyword = $(".already_url_keyword_input").val();
                if(alreadyUrlNewAddKeyword != ""){
                    var urlKeywordsInput = $(".url_keywords_new_add_input").val();
                    var $div = $("<div class='item' data-value='" + alreadyUrlNewAddKeyword + "'>" + alreadyUrlNewAddKeyword + "</div>");
                    $div.appendTo($(".url_keywords_new_add .menu"));
                    if(urlKeywordsInput == ""){
                        urlKeywordsInput += alreadyUrlNewAddKeyword;
                    }else{
                        urlKeywordsInput += "," + alreadyUrlNewAddKeyword;
                    }

                    $(".url_keywords_new_add_input").val(urlKeywordsInput);
                    $(".url_keywords_new_add").dropdown({
                        onLabelRemove:function(value){
                            alreayUrl_newKeyword.splice($.inArray(value, alreadyUrl_Keyword), 1);
                        }
                    });
                    //进行清空
                    $(".already_url_keyword_input").val("");

                    //进行添加关键字
                    if(alreayUrl_newKeyword.indexOf(alreadyUrlNewAddKeyword) < 0){
                        alreayUrl_newKeyword.push(alreadyUrlNewAddKeyword);
                    }

                }else{
                    //进行提示
                }
            })


            //新增的网站
            $(".url_new_add").on("click", function(){
                var url = $(".url_input").val();
                if(url != ""){
                    var urlsInputValue = $(".urls_input").val();
                    var $div = $("<div class='item' data-value='" + url + "'>" + url + "</div>");
                    $div.appendTo($(".urls_new_add .menu"));
                    if(urlsInputValue == ""){
                        urlsInputValue += url;
                    }else{
                        urlsInputValue += "," + url;
                    }

                    $(".urls_input").val(urlsInputValue);
                    $(".urls_new_add").dropdown({
                        onLabelRemove:function(value){
                            newAddUrl.splice($.inArray(value, newAddUrl), 1);
                        }
                    });
                    //进行清空
                    $(".url_input").val("");

                    //进行添加关键字
                    if(newAddUrl.indexOf(url) < 0){
                        newAddUrl.push(url);
                    }
                }else{
                    //进行提示
                }
            })

            //对新网址进行添加关键字

            $(".new_url_new_add_keyword_new_add").dropdown();

            $(".new_url_keyword_new_add").on("click", function(){
                var newUrlKeywordInput = $(".new_url_keyword_input").val();
                if(newUrlKeywordInput != ""){
                    var newUrlNewAddKeyword = $(".new_url_new_add_keyword_input").val();
                    var $div = $("<div class='item' data-value='" + newUrlKeywordInput + "'>" + newUrlKeywordInput + "</div>");
                    $div.appendTo($(".new_url_new_add_keyword_new_add .menu"));
                    if(newUrlNewAddKeyword == ""){
                        newUrlNewAddKeyword += newUrlKeywordInput;
                    }else{
                        newUrlNewAddKeyword += "," + newUrlKeywordInput;
                    }

                    $(".new_url_new_add_keyword_input").val(newUrlNewAddKeyword);
                    $(".new_url_new_add_keyword_new_add").dropdown({
                        onLabelRemove:function(value){
                            newAddUrlKeyword.splice($.inArray(value, newAddUrlKeyword), 1);
                        }
                    });
                    //进行清空
                    $(".new_url_keyword_input").val("");

                    //进行添加关键字
                    newAddUrlKeyword.push(newUrlKeywordInput);
                }else{
                    //进行提示
                }
            })

            //提交对某个网址
            $(".someweb_submit").on("click", function(){
                var alreadyUrl = already_Url;
                var alreadyUrlKeyword = alreadyUrl_Keyword.join(",");
                var alreayUrlNewKeyword = alreayUrl_newKeyword.join(",");
                var newUrl = newAddUrl[0];
                var newUrlKeyword = newAddUrlKeyword.join(",")
                $.ajax({
                    url:"submitUrl.html",
                    async:false,
                    type:"get",
                    data:{
                        alreadyUrl: alreadyUrl,
                        alreadyUrlKeyword: alreadyUrlKeyword,
                        alreayUrlNewKeyword: alreayUrlNewKeyword,
                        newUrl: newUrl,
                        newUrlKeyword : newUrlKeyword
                    },
                    success:function(){
                        //刷新
                        $(location).attr("href", "home.html");
                    }
                })
            })

            //-----------------------查看结果----------------------------
            //选中的搜索关键字
            var searchKeyword = "";
            $.ajax({
                url:"getSearchKeyword.html",
                type:"get",
                success:function(result){
                    if(result != "" && result.keywordString != "" && result.keywordString.length > 0){
                        var middle = result.keywordString;
                        for(var i=0; i < middle.length; i++){
                           if(middle[i].indexOf("_") > 0){
                               var $div = $("<div class='item' data-value='" + middle[i] + "'>" + middle[i].split("_")[0]+ "</div>");
                               $div.appendTo($(".all_search_keyword .menu"));
                           }else{
                               var $div = $("<div class='item' data-value='" + middle[i] + "'>" + middle[i] + "</div>");
                               $div.appendTo($(".all_search_keyword .menu"));
                           }

                        }
                        $(".all_search_keyword").dropdown({
                            onChange:function(value, text, $choice){
                                searchKeyword = value;
                                var totalCounts = 10;//设置默认值

                                $.ajax({
                                    url:"getPageBySearchKeyword.html",
                                    type:"get",
                                    async:false,
                                    data:{
                                        currentPage:1,
                                        pageSize:10,
                                        searchKeyword: searchKeyword
                                    },
                                    success:function(result){
                                        if(result != "" && result.totalCounts != ""){
                                            totalCounts = result.totalCounts;
                                        }

                                        if(result != "" && result.crawlers != "" && result.crawlers.length > 0){
                                            var midlleCrawlers = result.crawlers;
                                            for(var i = 0 ; i < midlleCrawlers.length; i++){
                                                var $item = $('<div class="item"> <div class="image" style="width: 100;height: 100"> <img src="'+midlleCrawlers[i].pictureURL+'"> </div> <div class="content"> <a class="header">'+
                                                        midlleCrawlers[i].pictureName+'</a> <div class="meta"> <span><a href="'+
                                                        midlleCrawlers[i].webURL+'">该条信息来源于</a></span> </div> <div class="description"> <p>'+
                                                        midlleCrawlers[i].pictureDescription+'</p> </div> </div> </div>');
                                                $item.appendTo($(".ui.items"));
                                            }
                                        }

                                    }
                                })

                            }
                        })
                    }
                }
            })

        })
    </script>
    </body>
</html>
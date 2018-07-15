
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link href="resources/css/semantic.min.css" rel="stylesheet" type="text/css">
    <link href="resources/css/pager.css" rel="stylesheet" type="text/css">


    <script src="resources/js/jquery-3.2.1.js" language="JavaScript"></script>
    <script src="resources/js/semantic.min.js" language="JavaScript"></script>
    <script src="resources/js/jquery.pager.js" language="JavaScript"></script>

    <style type="text/css">
        body {
            background-color: #DADADA;
        }
        body > .grid {
            height: 100%;
        }
        .image {
            margin-top: -100px;
        }
        .column {
            max-width: 450px;
        }
    </style>
    <script>
        $(document)
        $(document).ready(function(){
            var $messageForm = $(".ui.form");
            //这是表单验证
            $messageForm.form({
                inline:true,
                on:'blur',
                fields:{
                    username:{
                        identifier:"username",
                        rules:[
                            {
                                type:"empty",
                                prompt:"用户名不能为空"
                            }
                        ]
                    },
                    password:{
                        identifier:"password",
                        rules:[
                            {
                                type:"length[6]",
                                prompt:"您的密码必须至少是6位数"
                            }
                        ]
                    }
                },
                onValid:function(){
                    //主要是进行后台验证
                    var selector = $(this).attr("name");
                    var value = $(this).val();
                    $.ajax({
                        url:"loginValidation.html",
                        data:{
                            name:selector,
                            value:value
                        },
                        success:function(result){
                            if(result.type == "username"){
                                $(".ui.form").form('add prompt', "username", "用户名不存在");
                            }else if(result.type == "password"){
                                $(".ui.form").form('add prompt', "password", "密码不正确");
                            }
                        }
                    })
                }
            });
        })
        </script>
</head>
<body>

<div class="ui middle aligned center aligned grid">
    <div class="column">
        <h2 class="ui teal image header">
            <#--<img class="image"  src="resources/images/logo.jpg">-->
            <div class="content">
                登录你的账户
            </div>
        </h2>
        <form class="ui large form" action="loginValid.html" method="post">
            <div class="ui stacked segment">
                <div class="field">
                    <div class="ui left icon input">
                        <i class="user icon"></i>
                        <input type="text" name="username" placeholder="用户名">
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="lock icon"></i>
                        <input type="password" name="password" placeholder="密码"/>
                    </div>
                </div>
                <div class="ui fluid large teal submit button">登录</div>
            </div>
            <div class="ui error message"></div>
        </form>
        <div class="ui message">
            没有账户？<a href="#">注册</a>
        </div>
    </div>
</div>

</body>

</html>

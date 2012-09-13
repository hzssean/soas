/**
 * Created with IntelliJ IDEA.
 * User: zhisheng.hzs
 * Date: 12-9-11
 * Time: 上午11:07
 * 处理handle页面的js
 */
var url_get="rest/saiku/database/load";
var info={};

function show_fn(i){//显示详情
    $('#new_connection_config').addClass("hide");
    $('#show_connection_config').removeClass("hide");
    //var i=$(this).children('a').attr("href").replace("#","");
    $('#img2_content').html("连接详情："+info[i].connection_name);
    $('#con_name_').val(info[i].connection_name);
    $('#con_type_').val(info[i].connection_type);
    if(info[i].connection_type==="Oracle"){
        $('#con_driver_').val("Oracle Thin")
    }
    else if(info[i].connection_type==="MySQL"){
        $('#con_driver_').val("MySQL")
    }
    $('#con_server_').val(info[i].database_server);
    $('#con_port_').val(info[i].database_port);
    $('#user_').val(info[i].userid);
    $('#password_').val(info[i].password);
}

$(function(){
    $.get(url_get,function(result){//页面加载完毕后读取已有信息
        //var a = $(result).serializeArray();生成一个json对象
        $(result).each(function(index){
            $('.sidebar_inner ul:eq(0)').append("\<li\>" +
                "\<span class=\"root sprite collapsed\"\>\</span\>"+"\<a "+"href=\"#"+index+"\""+"\>"+index+"."+this.connection_name+"\</a\>"+"\<ul\>\</ul\>"+"\</li\>");

        });

        $(".root,.sidebar_inner a").bind("click",function(){
            var i=($(this).attr("href")!=undefined)?$(this).attr("href").replace("#",""):$(this).next().attr("href").replace("#","");//i为所点击的序号
            if($('.dimension_tree>ul>li').eq(i).children("span").hasClass("expand")){//如果已经展开，则不异步请求
                $('.dimension_tree>ul>li').eq(i).children("span").toggleClass("collapsed").toggleClass("expand");
                $('.dimension_tree>ul>li').eq(i).children("ul").empty();
            }
            else{

                info=result[i];
                $.ajax({
                    url:"rest/saiku/database_connect/connect",
                    type:"post",
                    data:info,
                    success:function(data){//保存成功后的函数,获取服务器返回内容，并更新当前页面,
                        $('.dimension_tree>ul>li').eq(i).children("span").toggleClass("collapsed").toggleClass("expand");//展开效果
                        for(var n=0;n<data.length;n++){//加载数据表列表
                            $('.dimension_tree>ul>li').eq(i).children("ul").append("\<li style=\"font-weight: normal;\" aria-disabled=\"false\"\>"+"\<a href=\"#\"\>"+data[n]+"\</a\>"+"\</li\>");
                        }
                        $.get(url_get,function(result){//从保存数据库信息的数据库读取配置信息
                            info=result;//生成一个json对象
                            show_fn(i);//在右边区域显示数据库配置信息
                        } );
                    }
                });
            }

        });
    } );


});


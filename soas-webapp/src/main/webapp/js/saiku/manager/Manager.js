/**
 * Created with IntelliJ IDEA.
 * User: zhisheng.hzs
 * Date: 12-9-6
 * Time: 上午9:48
 * 处理manager页面的js.
 */
var info={};
var url_get="rest/saiku/database/load";
var url_delete="rest/saiku/database/delete"
var name_regex =new RegExp("\\W{1,20}","g");
var server_regex =new RegExp("[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?")//域名验证的，ip也能通过验证
var port_regex =new RegExp("[^0-9]{1,20}","g");
var user_regex =new RegExp("\\W{1,50}","g");
var password_regex =new RegExp("\\W{1,50}","g");
function getFormJson(frm) {//转化成json格式
    var o = {};
    var a = $(frm).serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
}

function show_fn(){//显示详情
    $('#new_connection_config').addClass("hide");
    $('#show_connection_config').removeClass("hide");
    var i=$(this).children('a').attr("href").replace("#","");
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

function fn(){//成功后的函数,跟新当前页面

    $.get(url_get,function(result){
        info=result;//生成一个json对象
        $('.sidebar_inner').empty();
        $(result).each(function(index){
            $('.sidebar_inner').append("\<div class=\"con_list\"\> "+"\<a "+"href=\"#"+index+"\""+"\>"+index+"."+this.connection_name+"\</a\>"+"\</div\> ");
        });
        $(".con_list").bind("click",show_fn);//绑定
        $('#reset').trigger("click");//重置表单,jquery与js是不同的
    } )
}


function verify(){//验证合法性函数
    if($('#con_name').val().length>20){
        alert("输入名称过长，请重新输入");
        $('#con_name').val("");
        return false;
    }
    else if(name_regex.test($('#con_name').val())){
        alert("名字包含非法字符！");
        $('#con_name').val("");
        return false;
    }
    else if($('#con_server').val().length>100){
        alert("输入服务器地址过长，请重新输入");
        $('#con_server').val("");
        return false;
    }
    else if(!server_regex.test($('#con_server').val())){
        alert("输入服务器地址非法，请重新输入");
        $('#con_server').val("");
        return false;
    }
    else if($('#con_port').val().length>10){
        alert("输入数据库端口过长，请重新输入");
        $('#con_port').val("");
        return false;
    }
    else if(port_regex.test($('#con_port').val())){
        alert("输入数据库端口包含非数字，请重新输入");
        $('#con_port').val("");
        return false;
    }
    else if($('#user').val().length>50){
        alert("输入的用户名过长，请限制在50位之内");
        $('#user').val("");
        return false;
    }
    else if(user_regex.test($('#user').val())){
        alert("输入的用户名包含非法字符，请重新输入");
        $('#user').val("");
        return false;
    }
    else if($('#password').val().length>50){
        alert("输入的密码过长，请限制在50位之内");
        $('#password').val("");
        return false;
    }
    else if(password_regex.test($('#password').val())){
        alert("输入的密码包含非法字符，请重新输入");
        $('#password').val("");
        return false;
    }
    return true;
}


$(function(){
    $('#con_type').click(function(){//选择数据库驱动
        if($('#con_type').val()==="Oracle"){
            $(".box_25").removeClass("hide");
            $("#con_driver").val("Oracle Thin");
        }
        else if($('#con_type').val()==="MySQL"){
            $(".box_25").removeClass("hide");
            $('#con_driver').val("MySQL");
        }
        else{$(".box_25").addClass("hide");$('#con_driver').val("");}
    });


    $.get(url_get,function(result){//页面加载完毕后读取已有信息
        info=result;//生成一个json对象
        $(result).each(function(index){
            $('.sidebar_inner').append("\<div class=\"con_list\"\> "+"\<a "+"href=\"#"+index+"\""+"\>"+index+"."+this.connection_name+"\</a\>"+"\</div\> ")
        });
        $(".con_list").bind("click",show_fn);//绑定
    } );



    $('#myForm').bind('submit',function() {//提交表单
        if(!verify()){
            return false;
        };//提交之前验证合法性
        var para={};
        para=getFormJson(this);
        $.ajax({
            url:this.action,
            type:this.method,
            data:para,
            success:fn
        });
        return false;
    });



    $('#re_set').click(function(){
        $('#show_connection_config').addClass("hide");
        $('#new_connection_config').removeClass("hide");
        //$("#myForm").eq(0).reset();
        $('#reset').trigger("click");
    })

    $('#delete_con').click(function(){//删除数据表
        var para={"con_name":$('#con_name_').val()};
        $.ajax({
            url:url_delete,
            type:"post",
            data:para,
            success:fn
        });

    })
});








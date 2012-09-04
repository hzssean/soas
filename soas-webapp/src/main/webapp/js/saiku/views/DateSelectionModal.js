//TODO 新增一个date_test,继承Modal

var DateSelectionModal= Modal.extend({
    type:"DateSelectionModal",
    events: {
        'click a': 'call'//,
       // 'onSelect': 'call'
    },
    initialize:function(args){
        _.extend(this, args);
        //query对象
        my_query = args.workspace.query;
       //member对象
        my_member = new Member({}, {
            cube: args.workspace.selected_cube,
            dimension: args.key
        });

        //axis对象
        my_axis = "undefined";
        if (args.target.parents('.fields_list_body').hasClass('rows')) {
            my_axis = "ROWS";
        }
        if (args.target.parents('.fields_list_body').hasClass('columns')) {
            my_axis = "COLUMNS";
        }
        if (args.target.parents('.fields_list_body').hasClass('filter')) {
            my_axis = "FILTER";
        }

    } ,

    start_this:function(){
            //$(this.target).parent().children('.selections').datepicker({
        $('#date').datepicker({//date的input触发的选择日期插件
                changeYear:true,
                changeMonth:true,
                showButtonPanel:true,
                closeText:"关闭",
                currentText:"今天",
                //gotoCurrent:true,
                dateFormat:'yy-mm-dd',
                //yearRange:'1990:1999',
            onSelect:function(dateText,inst){
                year_select=dateText.split('-')[0];//获得年份信息
                month_select=dateText.split('-')[1];//获得月份信息
                day_select=dateText.split('-')[2];//获得日期
                //$('.ui-datepicker').remove();
                $(this).parent().children('.hasDatepicker ').removeClass("hasDatepicker");
                $('.placeholder ').remove();//修复bug

                //添加save功能

                var updates = [{
                    hierarchy: my_member.hierarchy,
                    uniquename: my_member.level,
                    type: 'level',
                    action: 'delete'
                }];
                var value=unescape(my_member.level.replace("DAY",year_select+month_select+day_select));
                updates.push({
                    uniquename: value,
                    type: 'member',
                    action: 'add'
                });
                my_query.action.put('/axis/' + my_axis + '/dimension/' + my_member.dimension, {
                    success:  function() {
                        my_query.run();
                    },
                    data: {
                        selections: JSON.stringify(updates)
                    }
                });

                //return false; TODO 能正常选择了
                }



        });
        $('#date').trigger("focus");
        $('.ui-datepicker').css({position:"absolute"});//使得不被隐藏起来



    }

});
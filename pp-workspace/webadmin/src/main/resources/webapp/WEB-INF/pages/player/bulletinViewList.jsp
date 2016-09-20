    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        <html xmlns="http://www.w3.org/1999/xhtml">
        <body>
        <table id='tb_bulletin' class="easyui-datagrid" pagination="true" rownumbers="true"
        fitColumns="true" toolbar="#tool_bar_bulletin">
        </table>


        <div id="tool_bar_bulletin">
        <input id="bulletin_uid" class="easyui-searchbox" data-options="prompt:'输入用户uid',searcher:doMailSearch"
        style="width:180px"/>
        &nbsp;
        <a href="#" class="easyui-linkbutton" iconCls="icon-ok" plain="true"
        onclick="$('#bulletin_dlg8').dialog('open')">发邮件</a>
        &nbsp;
        <a href="#" class="easyui-linkbutton" iconCls="icon-no" plain="true" onclick="doRemoveBulletin()">删除</a>
        </div>


        <script type="text/javascript">
        $(function(){
        $.get('player/loadItems/',function(data){
        for(var i=0;i<data.rows.length;i++){
        $("#bulletin_attachmentItems").append("<option value='"+data.rows[i].val+"'>"+data.rows[i].key+"</option>");
        }
        });

        $.get('bulletin/loadTypes',function(data){
        $("#bulletin_addAttachmentItem").die('click').live("click",function(){
        bulletinAppendAttachmentItems(data.TYPE_ITEM);
        });
        $("#bulletin_gold_coin").die('click').live("click",function(){
        bulletinAppendAttachment(data.TYPE_GAMECOIN);
        });

        $("#bulletin_game_coin").die('click').live("click",function(){
        bulletinAppendAttachment(data.TYPE_GAMECOIN);
        });

        $("#bulletin_farm_exp").die('click').live("click",function(){
        bulletinAppendAttachment(data.TYPE_FARMEXP);
        });

        $("#bulletin_family_contribution").die('click').live("click",function(){
        bulletinAppendAttachment(data.TYPE_FAMILY_CONTRIBUTION);
        });

        $("#bulletin_web_credit").die('click').live("click",function(){
        bulletinAppendAttachment(data.TYPE_CREDIT);
        });
        });
        doBulletinSearch(0,true);
        });

        function bulletinAppendAttachmentItems(attachmentType){
        var str = $("#bulletin_attachmentItems").val()+":"
        +$("#bulletin_attachmentItemNum").val()+":"+attachmentType+","
        +$("#bulletin_selectedAttachmentItems").val();
        $("#bulletin_selectedAttachmentItems").val(str);
        }
        function bulletinAppendAttachment(attachmentType){
        var str = "非物品:"
        +$("#bulletin_attachmentItemNum").val()+":"+attachmentType+","
        +$("#bulletin_selectedAttachmentItems").val();
        $("#bulletin_selectedAttachmentItems").val(str);
        }

        function doMailSearch(value,name){
        var reg = /[^\d]+/;
        if(reg.test(value)) {
        alert("请输入数字");
        }
        if(value==""){
            doBulletinSearch(0,true);
        }else{
            doBulletinSearch(value,false);
        }

        }

        function doBulletinSearch(uid,check) {
        var queryParams = {} ;
        queryParams.uid = uid;
        $('#tb_bulletin').datagrid({
        nowrap: false,
        height:"auto",
        fit:true,
        idField:'id', method:'GET', url:'bulletin/list/',queryParams:queryParams , pagination:true, rownumbers:"true",
        fitColumns:"true", frozenColumns:[
        [
        {field:'ck', checkbox:check}
        ]
        ], columns:[
        [
        {field:'id', title:'id',width:30} ,
        {field:'title', title:'标题',width:100} ,
        {field:'content', title:'内容',width:300},
        {field:'attachment', title:'附件',width:150} ,
        {field:'date', title:'date',width:50}
        ]
        ]
        });
        }


        function doRemoveBulletin(){
        var rows = $('#tb_bulletin').datagrid('getSelections');
        if (rows || rows.length > 0) { //alert(rows.length);
        //for(var i=0;i<rows.length;++i)$.post("bulletin/remove",{"id":rows[i].id});//only for test
        postInQueue(0,rows);//i start with 0
        }

        alert("request has been send...");
        }

        function postInQueue(i,rows){
        if(i>=rows.length){
        doBulletinSearch(0,true);
        return;
        }

        $.post("bulletin/remove",{"id":rows[i].id},function(data){
        postInQueue(++i,rows);
        }); //此处必须设置idField:'id'才能多选
        }


        function doSendBulletin(){
        $.post('bulletin/sendMail',
        {"attachment":$('#bulletin_selectedAttachmentItems').val(),
        "title":$('#bulletin_mailTitle').val(),
        "content":$('#bulletin_mailEditor').val()
        },
        function(data){
        if(data.code == 'ok')
        {doBulletinSearch(0,true);
        $('#bulletin_dlg8').dialog('close');
        }else{
        alert(data.code)
        }
        }
        );
        }
        </script>


        <div id="bulletin_dlg8" class="easyui-dialog" title="发送邮件" data-options="
        iconCls: 'icon-save',buttons: '#bulletin_dlg_buttons8',modal:true,closed:true
        " style="width:600px;height:400px;padding:10px">
        <form>
        标题：<input style="width: 200px;" id="bulletin_mailTitle" type="text"/> <br/>
        <textarea style="width: 550px;height: 150px;" id="bulletin_mailEditor"></textarea> <br/>
        附件：<select id="bulletin_attachmentItems" ></select>&nbsp;&nbsp;
        数量：<input size="3" id="bulletin_attachmentItemNum" type="text" value="1" /> <br/>
        <input id="bulletin_addAttachmentItem" type="button" value="选中物品" />&nbsp;
        <input id="bulletin_gold_coin" type="button" value="达人币" />&nbsp;
        <input id="bulletin_game_coin" type="button" value="游戏币" />&nbsp;
        <input id="bulletin_farm_exp" type="button" value="农场经验" />&nbsp;
        <input id="bulletin_family_contribution" type="button" value="家族贡献" />&nbsp;
        <input id="bulletin_web_credit" type="button" value="网站积分" /><br/>
        <textarea style="width: 550px;height: 50px;" id="bulletin_selectedAttachmentItems"></textarea>
        </form>
        </div>
        <div id="bulletin_dlg_buttons8">
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:doSendBulletin()">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton"
        onclick="javascript:$('#bulletin_dlg8').dialog('close')">取消</a>
        </div><!-- dialog8 -->

        </body>
        </html>
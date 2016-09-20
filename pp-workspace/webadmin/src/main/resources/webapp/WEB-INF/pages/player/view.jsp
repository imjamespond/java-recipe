    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
        <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

        <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        <html xmlns="http://www.w3.org/1999/xhtml">
        <body>

        <script type="text/javascript">
        var uid;

        function doSearch(value,name){
        if(value == ""){
        alert("请输入玩家id");
        return;
        }
        var reg = /[^\d]+/;
        if(reg.test(value)) {
        alert("请输入数字");
        return;
        }

        uid = value;

        $('#status').datagrid({
        nowrap: false,
        url:'player/user/'+value+'.json',
        columns:[[
        {field:'key',title:'名称',width:100},
        {field:'val',title:'值',width:300,align:'right'}
        ]]
        });
        }


        $('#operation').datagrid({
        url:'u.json',
        columns:[[
        {field:'date',title:'日期',width:150},
        {field:'oper',title:'操作',width:300,align:'right'}
        ]]
        });

        $.get('player/loadItems/',function(data){
        for(var i=0;i<data.rows.length;i++){
        $("#view_items").append("<option value='"+data.rows[i].val+"'>"+data.rows[i].key+"</option>");
        $("#view_attachmentItems").append("<option value='"+data.rows[i].val+"'>"+data.rows[i].key+"</option>");
        }
        });

        $("#view_addItem").click(function(){
        var preText =
        $("#view_selectedItems").val($("#view_items").val()+":"+$("#view_itemNum").val()+","+$("#view_selectedItems").val());
        });

        $.get('bulletin/loadTypes',function(data){
        $("#view_addAttachmentItem").die('click').live("click",function(){
        appendAttachmentItems(data.TYPE_ITEM);
        });
        $("#view_gold_coin").die('click').live("click",function(){
        appendAttachment(data.TYPE_GAMECOIN);
        });

        $("#view_game_coin").die('click').live("click",function(){
        appendAttachment(data.TYPE_GAMECOIN);
        });

        $("#view_farm_exp").die('click').live("click",function(){
        appendAttachment(data.TYPE_FARMEXP);
        });

        $("#view_family_contribution").die('click').live("click",function(){
        appendAttachment(data.TYPE_FAMILY_CONTRIBUTION);
        });

        $("#view_web_credit").die('click').live("click",function(){
        appendAttachment(data.TYPE_CREDIT);
        });
        });

        function appendAttachmentItems(attachmentType){
        var str = $("#view_attachmentItems").val()+":"
        +$("#view_attachmentItemNum").val()+":"+attachmentType+","
        +$("#view_selectedAttachmentItems").val();
        $("#view_selectedAttachmentItems").val(str);
        }
        function appendAttachment(attachmentType){
        var str = "非物品:"
        +$("#view_attachmentItemNum").val()+":"+attachmentType+","
        +$("#view_selectedAttachmentItems").val();
        $("#view_selectedAttachmentItems").val(str);
        }

        function doAddItem(){
        $.get('player/addItems/u/'+uid+'/'+$('#view_selectedItems').val(),function(data){ if(data.code ==
        'ok'){$('#view_dlg5').dialog('close');doSearch(uid);}else{alert(data.code)}});
        }

        function doImport(){
        $.get('player/parseLog/',function(data){ if(data.code == 'ok'){alert("完成!")}else{alert(data.code)}});
        }
        function doFinishTask(){
        $.get('player/finishTask/'+uid+"/",function(data){ if(data.code == 'ok'){alert("完成!")}else{alert(data.code)}});
        }
        function doFinishAllTask(){
        $.get('player/finishAllTask/'+uid+"/",function(data){ if(data.code ==
        'ok'){alert("完成!")}else{alert(data.code)}});
        }
        function doVip(){
        $.get('player/chargeVip/u/'+uid+'/'+$('#vipTime').val(),function(data){ if(data.code ==
        'ok'){$('#view_dlg6').dialog('close');doSearch(uid);}else{alert(data.code)}});
        }
        function doFarmExpNum(){
        $.get('player/addFarmExp/u/'+uid+'/'+$('#expNum').val(),function(data){ if(data.code ==
        'ok'){$('#dlg7').dialog('close');doSearch(uid);}else{alert(data.code)}});
        }
        function doSetAssistant(){
        $.get('player/setAssistant/'+uid+'/',function(data){ if(data.code ==
        'ok'){alert("完成!");doSearch(uid);}else{alert(data.code)}});
        }

        function doSendMail(){
        $.post('player/sendMail/',{"uid":uid,"attachment":$('#view_selectedAttachmentItems').val(),"title":$('#view_mailTitle').val(),"content":$('#view_mailEditor').val()},function(data){
        if(data.code == 'ok'){$('#view_dlg8').dialog('close');}else{alert(data.code)}});
        }

        </script>

        <div style=" padding:10px; width:870px;">

        <div id="left" style="width:300px; float:left;">
        <div style="padding-bottom: 5px;">
        <!--<input id="memo" class='easyui-combobox' rows="10" style="width: 100px"/>-->
        <input id="search" class="easyui-searchbox" data-options="prompt:'输入用户uid',searcher:doSearch"
        style="width:180px"/>
        </div>

        <div style="padding-bottom: 5px;">
        封号状态:
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#view_dlg').dialog('open')">封号</a>
        </div>

        <div style="padding-bottom: 5px;">
        禁言操作:
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#view_dlg2').dialog('open')">禁言</a>
        </div>

        <div style="padding-bottom: 5px;">
        游戏币:
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#view_dlg3').dialog('open')">添加</a>
        </div>

        <div style="padding-bottom: 5px;">
        达人币:
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#view_dlg4').dialog('open')">添加</a>
        </div>

        <div style="padding-bottom: 5px;">
        添加道具:
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#view_dlg5').dialog('open')">添加</a>
        </div>
        <!--For test-->
        <%--<div style="padding-bottom: 5px;">--%>
        <%--导入log:--%>
        <%--<a href="javascript:void(0)" class="easyui-linkbutton" onclick="doImport()">test</a>--%>
        <%--</div>--%>
        <div style="padding-bottom: 5px;">
        跳过所有新手任务:
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="doFinishAllTask()">跳过所有新手任务</a>
        </div>
        <div style="padding-bottom: 5px;">
        完成卡住的新手任务:
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="doFinishTask()">完成卡住的新手任务</a>
        </div>
        <div style="padding-bottom: 5px;">
        设置vip:
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#view_dlg6').dialog('open')">设置vip</a>
        </div>
        <div style="padding-bottom: 5px;">
        设置明星助理:
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="doSetAssistant()">设置</a>
        </div>
        <div style="padding-bottom: 5px;">
        发送邮件:
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#view_dlg8').dialog('open')">添加</a>
        </div>
        <div style="padding-bottom: 5px;">
        添加农场经验:
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#dlg7').dialog('open')">添加</a>
        </div>
        <!--<table id="status"></table>-->

        </div><!-- left -->

        <div id="right" style="width:550px; float:left;">
        <table id="status"></table>
        <!--<table id="operation"></table>-->
        </div><!-- right -->

        <div id="view_dlg" class="easyui-dialog" title="封禁" data-options="
        iconCls: 'icon-save',buttons: '#view_dlg-buttons',modal:true,closed:true
        " style="width:400px;height:200px;padding:10px">
        <p>请选择时间</p>
        <form>
        <input type="radio" name="time" value="3" /> 3分钟
        <br />
        <input type="radio" name="time" value="10" /> 10分钟
        </form>
        </div>
        <div id="view_dlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton"
        onclick="javascript:$.get('player/freeze/u/'+uid+'/'+$('#view_dlg :input:checked').val(),function(data){
        if(data.code == 'ok'){$('#view_dlg').dialog('close')}else{alert(data.code)}})">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton"
        onclick="javascript:$('#view_dlg').dialog('close')">取消</a>
        </div><!-- dialog -->

        <div id="view_dlg2" class="easyui-dialog" title="禁言" data-options="
        iconCls: 'icon-save',buttons: '#view_dlg-buttons2',modal:true,closed:true
        " style="width:400px;height:200px;padding:10px">
        <p>请选择时间</p>
        <form>
        <input type="radio" name="time" value="3" /> 3分钟
        <br />
        <input type="radio" name="time" value="10" /> 10分钟
        </form>
        </div>
        <div id="view_dlg-buttons2">
        <a href="javascript:void(0)" class="easyui-linkbutton"
        onclick="javascript:$.get('player/speak/u/'+uid+'/'+$('#view_dlg2 :input:checked').val(),function(data){
        if(data.code == 'ok'){$('#view_dlg2').dialog('close')}else{alert(data.code)}})">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton"
        onclick="javascript:$('#view_dlg2').dialog('close')">取消</a>
        </div><!-- dialog -->

        <div id="view_dlg3" class="easyui-dialog" title="添加游戏币" data-options="
        iconCls: 'icon-save',buttons: '#view_dlg-buttons3',modal:true,closed:true
        " style="width:400px;height:200px;padding:10px">
        <form>
        添加<input type="text" name="time" value="10" />游戏币
        </form>
        </div>
        <div id="view_dlg-buttons3">
        <a href="javascript:void(0)" class="easyui-linkbutton"
        onclick="javascript:$.get('player/gameCoin/u/'+uid+'/'+$('#view_dlg3 :input').val(),function(data){ if(data.code
        == 'ok'){$('#view_dlg3').dialog('close');doSearch(uid);}else{alert(data.code)}})">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton"
        onclick="javascript:$('#view_dlg3').dialog('close')">取消</a>
        </div><!-- dialog3 -->

        <div id="view_dlg4" class="easyui-dialog" title="添加达人币" data-options="
        iconCls: 'icon-save',buttons: '#view_dlg-buttons4',modal:true,closed:true
        " style="width:400px;height:200px;padding:10px">
        <form>
        添加<input type="text" id="time" value="10" />达人币
        </form>
        </div>
        <div id="view_dlg-buttons4">
        <a href="javascript:void(0)" class="easyui-linkbutton"
        onclick="javascript:$.get('player/goldCoin/u/'+uid+'/'+$('#view_dlg4 :input').val(),function(data){ if(data.code
        == 'ok'){$('#view_dlg4').dialog('close');doSearch(uid);}else{alert(data.code)}})">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton"
        onclick="javascript:$('#view_dlg4').dialog('close')">取消</a>
        </div><!-- dialog4 -->

        <div id="view_dlg5" class="easyui-dialog" title="添加道具" data-options="
        iconCls: 'icon-save',buttons: '#view_dlg-buttons5',modal:true,closed:true
        " style="width:400px;height:200px;padding:10px">
        <form>
        <select id="view_items" ></select>_
        <input size="3" id="view_itemNum" type="text" value="1" />个
        <input id="view_addItem" type="button" value="选中"><br/>
        <textarea id="view_selectedItems"></textarea>
        </form>
        </div>
        <div id="view_dlg-buttons5">
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:doAddItem()">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton"
        onclick="javascript:$('#view_dlg5').dialog('close')">取消</a>
        </div><!-- dialog5 -->

        <div id="view_dlg6" class="easyui-dialog" title="增加Vip时间" data-options="
        iconCls: 'icon-save',buttons: '#view_dlg-buttons6',modal:true,closed:true
        " style="width:400px;height:200px;padding:10px">
        <form>
        增加Vip<input type="text" id="vipTime" value="1" />月
        </form>
        </div>
        <div id="view_dlg-buttons6">
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:doVip()">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton"
        onclick="javascript:$('#view_dlg6').dialog('close')">取消</a>
        </div><!-- dialog6 -->

        <div id="dlg7" class="easyui-dialog" title="增加农场经验" data-options="
        iconCls: 'icon-save',buttons: '#dlg-buttons7',modal:true,closed:true
        " style="width:400px;height:200px;padding:10px">
        <form>
        增加农场经验<input type="text" id="expNum" value="1" />
        </form>
        </div>
        <div id="dlg-buttons7">
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:doFarmExpNum()">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#dlg7').dialog('close')">取消</a>
        </div><!-- dialog7-->

        <div id="view_dlg8" class="easyui-dialog" title="发送邮件" data-options="
        iconCls: 'icon-save',buttons: '#view_dlg-buttons8',modal:true,closed:true
        " style="width:600px;height:400px;padding:10px">
        <form>
        标题：<input style="width: 200px;" id="view_mailTitle" type="text"/> <br/>
        <textarea style="width: 550px;height: 150px;" id="view_mailEditor"></textarea> <br/>
        附件：<select id="view_attachmentItems" ></select>&nbsp;&nbsp;
        数量：<input size="3" id="view_attachmentItemNum" type="text" value="1" /> <br/>
        <input id="view_addAttachmentItem" type="button" value="选中物品" />&nbsp;
        <input id="view_gold_coin" type="button" value="达人币" />&nbsp;
        <input id="view_game_coin" type="button" value="游戏币" />&nbsp;
        <input id="view_farm_exp" type="button" value="农场经验" />&nbsp;
        <input id="view_family_contribution" type="button" value="家族贡献" />&nbsp;
        <input id="view_web_credit" type="button" value="网站积分" /><br/>
        <textarea style="width: 550px;height: 50px;" id="view_selectedAttachmentItems"></textarea>
        </form>
        </div>
        <div id="view_dlg-buttons8">
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:doSendMail()">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton"
        onclick="javascript:$('#view_dlg8').dialog('close')">取消</a>
        </div><!-- dialog8 -->


        </div>
        </body>
        </html>
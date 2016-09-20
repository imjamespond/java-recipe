<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <div id="tbanalyse_goldStat">
        &nbsp;开始日期:
        <input id="analyse_goldStat_sdate" class="easyui-datebox" value="${sdate}"/> &nbsp;
        &nbsp;结束日期:
        <input id="analyse_goldStat_edate" class="easyui-datebox" value="${edate}"/> &nbsp;
        <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="javascript:search_goldStat();">查询</a>
    </div>
    <table id='analysegoldStat' class="easyui-datagrid" url="analyse/goldStat/list">
    </table>
    <script type="text/javascript">
        function search_goldStat() {
            var queryParams = {};
            if($("#analyse_goldStat_sdate").datebox('getValue')!=""&&$("#analyse_goldStat_edate").datebox('getValue')!=""){
                queryParams.sdate=$("#analyse_goldStat_sdate").datebox('getValue');
                queryParams.edate=$("#analyse_goldStat_edate").datebox('getValue');
            }
            $('#analysegoldStat').datagrid({
                height: "auto"
//                ,fit:true
                ,width:1000
                ,idField:'id'
                ,method:'GET'
                ,url:'analyse/goldStat/list'
                ,queryParams:queryParams
                ,columns:[[
                    {field:'date',title:'日期'}
                    ,{field:'st19',title:'成就系统奖励'}
                    ,{field:'st30',title:'连续登录奖励'}
                    ,{field:'st50',title:'农场开宝箱奖励'}
                    ,{field:'st',title:'消耗'}
                    ,{field:'st1',title:'农场作物催熟'}
                    ,{field:'st2',title:'农场开宝箱'}
                    ,{field:'st3',title:'农场装饰'}
                    ,{field:'st4',title:'订单快速货运'}
                    ,{field:'st5',title:'加工队列立即完成单个'}
                    ,{field:'st6',title:'加工队列加速'}
                    ,{field:'st7',title:'加工队列开通格子'}
                    ,{field:'st9',title:'时装购买'}
                    ,{field:'st10',title:'家族达人币捐献'}
                    ,{field:'st11',title:'赞音乐'}
                    ,{field:'st12',title:'大喇叭'}
                    ,{field:'st13',title:'房间扩建'}
                    ,{field:'st14',title:'房间购买达人币装饰'}
                    ,{field:'st15',title:'任务 完美领取'}
                    ,{field:'st16',title:'任务 立即完成'}
                    ,{field:'st17',title:'送明星精美礼物'}
                    ,{field:'st18',title:'农场 购买达人币作物'}
                    ,{field:'st20',title:'招财树增加次数'}
                    ,{field:'st21',title:'招财树 招财'}
                    ,{field:'st25',title:'抽奖'}
                    ,{field:'st35',title:'码头'}
                    ,{field:'st40',title:'摆摊,开启摊位'}
                    ,{field:'st44',title:'摆摊,开启'}
                    ,{field:'st43',title:'摆摊 助手'}
                    ,{field:'st45',title:'小游戏'}
                ]]
            });
        }


    </script>
    </body>
</html>
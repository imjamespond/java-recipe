<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ include file="common/taglib.jsp" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<div region="north" split="false" style="height:60px;overflow:hidden;">
    <%@ include file="common/admHeader.jsp" %>
</div>
<div region="west" split="true" title="粉丝达人" style="width:200px;OVERFLOW-Y: auto; OVERFLOW-X:hidden;">
    <ul id="mainTree" class="easyui-tree" animate="true" dnd="false">
        <sec:authorize ifAnyGranted="ROLE_GM,ROLE_ADMIN,ROLE_DIRECTOR,ROLE_MANAGER,ROLE_OPERATOR,ROLE_MAIN_DESIGNER,ROLE_DESIGNER,ROLE_NUMERICAL_DESIGNER">
        </sec:authorize>
            <li state="closed">
                <span>后台管理</span>
                <ul>
                    <li><span><a href="javascript:void(0)" onclick="showpage('password', '后台成员信息')">我的账户</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('', '管理后台成员')">账户管理</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('userActionModel', '管理员的系统操作记录')">操作记录</a></span>
                    </li>
                </ul>
            </li>


        <sec:authorize ifAnyGranted="ROLE_GM,ROLE_ADMIN,ROLE_DIRECTOR,ROLE_MAIN_DESIGNER,ROLE_DESIGNER,ROLE_NUMERICAL_DESIGNER">
            <li state="closed">
                <span>NPC编辑</span>
                <ul>
                    <li><span><a href="javascript:void(0)" onclick="showpage('qinMaFarmRule', '亲妈农场')">亲妈农场</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('qinMaRoomRule', '亲妈房间')">亲妈房间</a></span>
                    </li>
                </ul>
            </li>
        </sec:authorize>

        <sec:authorize ifAnyGranted="ROLE_GM,ROLE_ADMIN,ROLE_DIRECTOR,ROLE_MANAGER,ROLE_OPERATOR,ROLE_MAIN_DESIGNER,ROLE_NUMERICAL_DESIGNER">
            <li state="closed">
                <span>数据统计</span>
                <ul>
                    <li><span><a href="javascript:void(0)"
                                 onclick="showpage('playerLoginActionModel', '玩家登录统计')">玩家登录统计</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('playerLoginActionModel/2', '玩家退出统计')">玩家退出统计</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('playerRechargeActionModel', '金币充值统计')">金币充值统计</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)"
                                 onclick="showpage('playerMapActionModel', '进入地图统计')">进入地图统计</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('playerTaskActionModel', '完成任务奖励统计')">完成任务奖励统计</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('playerLotteryActionModel', '幸运翻牌统计')">幸运翻牌统计</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('playerPioneerActionModel', '音乐先锋榜领取统计')">音乐先锋榜领取统计</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)"
                                 onclick="showpage('playerActivityActionModel', '每日活跃度积分领取统计')">每日活跃度积分领取统计</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('playerHarvestActionModel', '农场种埴统计')">农场种埴统计</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('playerHarvestActionModel/2', '农场收获统计')">农场收获统计</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('playerHarvestActionModel/3', '协助收获统计')">协助收获统计</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)"
                                 onclick="showpage('playerFarmComplimentActionModel', '农场好评统计')">农场好评统计</a></span></li>
                    <li><span><a href="javascript:void(0)"
                                 onclick="showpage('playerOrderResetActionModel', '刷新农场订单统计')">刷新农场订单统计</a></span></li>
                    <li><span><a href="javascript:void(0)"
                                 onclick="showpage('playerBuyActionModel', '购买种子统计')">购买种子统计</a></span></li>
                    <li><span><a href="javascript:void(0)"
                                 onclick="showpage('playerSaleActionModel', '卖出作物统计')">卖出作物统计</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('playerDecorationActionModel', '房间装修统计')">房间装修统计</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('playerMoneyTreeActionModel', '摇钱树-祝福统计')">摇钱树-祝福统计</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('playerGivingActionModel', '赠送礼物统计')">赠送礼物统计</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)"
                                 onclick="showpage('playerRegisterModel', '玩家注册统计')">玩家注册统计</a></span></li>
                </ul>
            </li>
        </sec:authorize>

        <sec:authorize ifAnyGranted="ROLE_GM,ROLE_ADMIN,ROLE_DIRECTOR,ROLE_MANAGER,ROLE_OPERATOR,ROLE_MAIN_DESIGNER,ROLE_NUMERICAL_DESIGNER">
            <li state="closed">
                <span>报表分析</span>
                <ul>
                    <li><span><a href="javascript:void(0)"
                                 onclick="showpage('analyse/total', '总体数据分析')">总体数据分析</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('analyse/farm', '积分分析')">积分分析</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('analyse/goldStat', '达人币分析')">达人币分析</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)"
                                 onclick="showpage('analyse/operation', '推广运营数据')">推广运营数据</a></span></li>
                    <%--<li><span><a href="javascript:void(0)" onclick="showpage('analyse/room', '房间分析')">房间分析</a></span>--%>
                    <%--</li>--%>
                    <%--<li><span><a href="javascript:void(0)" onclick="showpage('analyse/lottery', '幸运翻牌分析')">幸运翻牌分析</a></span>--%>
                    </li>
                </ul>
            </li>
        </sec:authorize>

        <sec:authorize ifAnyGranted="ROLE_GM,ROLE_ADMIN,ROLE_DIRECTOR,ROLE_MANAGER,ROLE_OPERATOR,ROLE_MAIN_DESIGNER,ROLE_NUMERICAL_DESIGNER">
            <li state="closed">
                <span>玩家管理</span>
                <ul>
                    <li><span><a href="javascript:void(0)" onclick="showpage('player', '玩家管理')">玩家管理</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('message', '公告管理')">公告管理</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('gold', '登陆送达人币管理')">登陆送达人币管理</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('assistant', '明星助理审核')">明星助理审核</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('video', '视频直播管理')">视频直播管理</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('bulletin', '系统邮件')">系统邮件</a></span></li>
                <!-- <li><span><a href="javascript:void(0)" onclick="showpage('', '禁言封号')">封号/禁言</a></span></li>
                 <li><span><a href="javascript:void(0)" onclick="showpage('', '消费记录')">消费记录</a></span></li>
                 <li><span><a href="javascript:void(0)" onclick="showpage('player/money', '赠送/扣减达人币')">赠送/扣减达人币</a></span></li>-->
                </ul>
            </li>
        </sec:authorize>
        <sec:authorize ifAnyGranted="ROLE_GM,ROLE_ADMIN,ROLE_DIRECTOR,ROLE_MANAGER,ROLE_OPERATOR,ROLE_MAIN_DESIGNER,ROLE_NUMERICAL_DESIGNER">
            <li state="closed">
                <span>达人币VIP充值查询</span>
                <ul>
                    <li><span><a href="javascript:void(0)" onclick="showpage('rechage/goldList', '达人币充值查询')">达人币充值查询</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('rechage/vipList', 'VIP充值查询')">VIp充值查询</a></span></li>
                </ul>
            </li>
        </sec:authorize>

        <sec:authorize ifAnyGranted="ROLE_GM,ROLE_ADMIN,ROLE_DIRECTOR,ROLE_MAIN_DESIGNER,ROLE_DESIGNER,ROLE_NUMERICAL_DESIGNER">
            <li state="closed">
                <span>规则管理</span>
                <ul>
                    <li><span><a href="javascript:void(0)"
                                 onclick="showpage('/rule/refresh', '刷新服务器数据')">刷新服务器数据</a></span></li>
                    <li><span>----通用模块----</span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('playerRule', '玩家规则')">玩家规则</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('wordRule', '敏感词拦截规则')">敏感词拦截规则</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)"
                                 onclick="showpage('identityRule', '玩家身份规则')">玩家身份规则</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('taskRule', '任务规则')">任务规则</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('sceneRule', '场景规则')">场景规则</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('transferRule', '传送口规则')">传送口规则</a></span>
                    </li>

                    <li><span>----农场模块----</span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('qinMaFarmRule', '亲妈农场')">亲妈农场</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('farmLevelRule', '农场规则')">农场规则</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('farmOrderRule', '农场订单')">农场订单</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)"
                                 onclick="showpage('farmWareHouseRule', '农场仓库')">农场仓库</a></span></li>
                    <li><span><a href="javascript:void(0)"
                                 onclick="showpage('baseGiftRule', '农场礼物规则')">农场礼物规则</a></span></li>
                    <li><span><a href="javascript:void(0)"
                                 onclick="showpage('baseGiftRule', '农场商店礼物规则')">农场商店礼物规则</a></span></li>
                    <li><span><a href="javascript:void(0)"
                                 onclick="showpage('farmSeedRule', '农场种子规则')">农场种子规则</a></span></li>

                    <li><span>----房间模块----  </span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('qinMaRoomRule', '亲妈房间')">亲妈房间</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('roomItemRule', '房间物品')">房间物品</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('fashionItemRule', '装扮物品')">装扮物品</a></span>
                    </li>

                    <li><span>----家族模块----</span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('familyRule', '家族规则')">家族规则</a></span></li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('familyBuildingRule', '家族建筑规则')">家族建筑规则</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('moneyTreeRule', '摇钱树规则')">摇钱树规则</a></span>
                    </li>
                    <li><span><a href="javascript:void(0)" onclick="showpage('activeControlRule', '家族活动规则')">家族活动规则</a></span>
                    </li>
                </ul>
            </li>
        </sec:authorize>

    </ul>
</div>
<div region="center" style="overflow:hidden;">
    <div id="center-tabs" class="easyui-tabs" fit="true" border="false">
        <div title="Dashboard" style="padding:10px;">

            每隔 5 分钟统计一次</br> </br>

            当前在线人数:${cNum} </br>
            今日最高在线人数: ${cDay.maxNum} (${cDay.maxTime})</br>
            今日最低在线人数: ${cDay.minNum} (${cDay.minTime})</br>
            </br> </br>
            昨日最高在线人数: ${yDay.maxNum} (${yDay.maxTime})</br>
            昨日最低在线人数: ${yDay.minNum} (${yDay.minTime})</br>
        </div>
    </div>
</div>


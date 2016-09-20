<%@ page import="com.pengpeng.stargame.managed.ServerType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Servers-Stargame Server Manager</title>
	<%@ include file="common/assets.jsp" %>
</head>
<body>


	<div id="header">
		<h1><a href="/dashboard.do">Stargame</a></h1>
	</div>

	<div id="search">

	</div>
	<div id="user-nav" class="navbar navbar-inverse">

	</div>

	<div id="sidebar">
		<a href="#" class="visible-phone"><i class="icon icon-home"></i> Dashboard</a>
		<ul>
			<li><a href="/dashboard.do"><i class="icon icon-home"></i> <span>Dashboard</span></a></li>
			<li class="submenu open active">
				<a href="#"><i class="icon icon-th-list"></i> <span>Servers</span><span class="label">5</span></a>
				<ul>
					<li class="<s:if test='type.equals(@com.pengpeng.stargame.managed.ServerType@GAMESERVER)'>active</s:if>"><a href="servers.do?type=<%=ServerType.GAMESERVER%>"><span><%=ServerType.GAMESERVER%></span></a></li>
					<li class="<s:if test='type.equals(@com.pengpeng.stargame.managed.ServerType@LOGIN)'>active</s:if>"><a href="servers.do?type=<%=ServerType.LOGIN%>"><%=ServerType.LOGIN%></a></li>
					<li class="<s:if test='type.equals(@com.pengpeng.stargame.managed.ServerType@STATUS)'>active</s:if>"><a href="servers.do?type=<%=ServerType.STATUS%>"><%=ServerType.STATUS%></a></li>
					<li class="<s:if test='type.equals(@com.pengpeng.stargame.managed.ServerType@LOGIC)'>active</s:if>"><a href="servers.do?type=<%=ServerType.LOGIC%>"><%=ServerType.LOGIC%></a></li>
					<li class="<s:if test='type.equals(@com.pengpeng.stargame.managed.ServerType@MANAGER)'>active</s:if>"><a href="servers.do?type=<%=ServerType.MANAGER%>"><%=ServerType.MANAGER%></a></li>
				</ul>
			</li>

		</ul>

	</div>


	<div id="content">
		<div id="content-header">
			<h1><s:property value="type"/></h1>
		</div>
		<div id="breadcrumb">
			<a href="/dashboard.do" title="Go to Dashboard" class="tip-bottom"><i class="icon-home"></i>Dashboard</a>
			<a href="/servers.do?type=<s:property value='type'/>"><s:property value="type"/></a>
		</div>

		<div class="container-fluid">
			<div class="row-fluid">

				<table class="table table-striped  table-hover">
					<thead>
						<tr>
							<th>Server ID</th>
							<th>Host</th>
							<th>RMI</th>
							<th>TCP</th>
							<th>Build</th>
							<th>Status</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<s:iterator value="servers" status="st">
							<tr>
								<td>
									<s:property value="nodeInfo.id"/>
								</td>
								<td>
									<s:property value="nodeInfo.host"/>
								</td>
								<td>
									<s:property value="nodeInfo.port"/>
								</td>
								<td>
									<s:property value="nodeInfo.tcpPort"/>
								</td>
								<td>
									<s:property value="nodeInfo.buildTime"/>
								</td>
								<td>
									<s:if test='status.equals("stopped")'>
										<span class="label label-important"><s:property value="status"/></span>
									</s:if>
									<s:if test='status.equals("paused")'>
										<span class="label label-warning"><s:property value="status"/></span>
									</s:if>
									<s:if test='status.equals("onservice")'>
										<span class="label label-success"><s:property value="status"/></span>
									</s:if>
								</td>
								<td>
									<a href='javascript:detail("<s:property value='instanceId'/>")' class="btn btn-small">Manage</a>
								</td>
							</tr>
						</s:iterator>

					</tbody>
				</table>


			</div>

		</div>
	</div>

	<script type="text/javascript">
		function detail(instanceId){
			window.location = "/detail.do?id="+instanceId;
		}
	</script>
</body>
</html>
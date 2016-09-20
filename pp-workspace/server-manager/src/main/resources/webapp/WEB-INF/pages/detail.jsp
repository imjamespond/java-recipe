<%@ page import="com.pengpeng.stargame.managed.ServerType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Instance Detail-Stargame Server Manager</title>
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
					<li class="<s:if test='instance.nodeInfo.type.equals(@com.pengpeng.stargame.managed.ServerType@GAMESERVER)'>active</s:if>"><a href="servers.do?type=<%=ServerType.GAMESERVER%>"><span><%=ServerType.GAMESERVER%></span></a></li>
					<li class="<s:if test='instance.nodeInfo.type.equals(@com.pengpeng.stargame.managed.ServerType@LOGIN)'>active</s:if>"><a href="servers.do?type=<%=ServerType.LOGIN%>"><%=ServerType.LOGIN%></a></li>
					<li class="<s:if test='instance.nodeInfo.type.equals(@com.pengpeng.stargame.managed.ServerType@STATUS)'>active</s:if>"><a href="servers.do?type=<%=ServerType.STATUS%>"><%=ServerType.STATUS%></a></li>
					<li class="<s:if test='instance.nodeInfo.type.equals(@com.pengpeng.stargame.managed.ServerType@LOGIC)'>active</s:if>"><a href="servers.do?type=<%=ServerType.LOGIC%>"><%=ServerType.LOGIC%></a></li>
					<li class="<s:if test='instance.nodeInfo.type.equals(@com.pengpeng.stargame.managed.ServerType@MANAGER)'>active</s:if>"><a href="servers.do?type=<%=ServerType.MANAGER%>"><%=ServerType.MANAGER%></a></li>
				</ul>
			</li>

		</ul>

	</div>


	<div id="content">
		<div id="content-header">
			<h1><s:property value="instance.nodeInfo.id"/></h1>
		</div>
		<div id="breadcrumb">
			<a href="/dashboard.do" title="Go to Dashboard" class="tip-bottom"><i class="icon-home"></i> Dashboard</a>
			<a href="/servers.do?type=<s:property value='instance.nodeInfo.type'/>"><s:property value='instance.nodeInfo.type'/></a>
			<span><s:property value='instance.nodeInfo.id'/></span>
		</div>

		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span6">
					<h5>Basic Detail</h5>
					<table class="table table-striped  table-hover">
						<tbody>
							<tr>
								<td>Server Id</td>
								<td><s:property value="instance.nodeInfo.id"/></td>
							</tr>
							<tr>
								<td>Server Type</td>
								<td><s:property value="instance.nodeInfo.type"/></td>
							</tr>
							<tr>
								<td>Host</td>
								<td><s:property value="instance.nodeInfo.host"/></td>
							</tr>
							<tr>
								<td>RMI Port</td>
								<td><s:property value="instance.nodeInfo.port"/></td>
							</tr>
							<tr>
								<td>TCP Port</td>
								<td><s:property value="instance.nodeInfo.tcpPort"/></td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="span6">
					<h5>Runtime Detail</h5>
					<table class="table table-striped  table-hover">
						<tbody>
							<tr>
								<td>Status</td>
								<td>
									<s:if test='instance.status.equals("stopped")'>
										<span class="label label-important"><s:property value="instance.status"/></span>
									</s:if>
									<s:if test='instance.status.equals("paused")'>
										<span class="label label-warning"><s:property value="instance.status"/></span>
									</s:if>
									<s:if test='instance.status.equals("onservice")'>
										<span class="label label-success"><s:property value="instance.status"/></span>
									</s:if>
								</td>
							</tr>
							<tr>
								<td>Uptime</td>
								<td><s:property value="instance.upTime"/> mins</td>
							</tr>
							<tr>
								<td>Momory</td>
								<td>
									<s:property value="instance.nodeInfo.runtime.memoryUsed"/>/
									<s:property value="instance.nodeInfo.runtime.memoryTotal"/> M
								</td>
							</tr>
						</tbody>
					</table>
				</div>

				<div class="span12 text-center">
					<div class="btn-group">
						<s:if test='instance.status.equals("onservice")'>
							<button class="btn" onClick="manage('pause','<s:property value="instance.instanceId"/>')">Pause</button>
							<button class="btn btn-warning" onClick="manage('stop','<s:property value="instance.instanceId"/>')">Stop</button>
							<button class="btn btn-danger" onClick="manage('kill','<s:property value="instance.instanceId"/>')">Kill</button>
						</s:if>
						<s:if test='instance.status.equals("paused")'>
							<button class="btn" onClick="manage('resume','<s:property value="instance.instanceId"/>')">Resume</button>
							<button class="btn btn-warning" onClick="manage('stop','<s:property value="instance.instanceId"/>')">Stop</button>
							<button class="btn btn-danger" onClick="manage('kill','<s:property value="instance.instanceId"/>')">Kill</button>
						</s:if>

					</div>
				</div>
			</div>

		</div>
	</div>

	<script type="text/javascript">
		function manage(cmd,instanceId) {
			window.location = "/manage.do?cmd="+cmd+"&id=" + instanceId;
		}
	</script>
</body>
</html>
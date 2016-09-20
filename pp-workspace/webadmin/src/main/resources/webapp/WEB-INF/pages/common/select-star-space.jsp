<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ROOT" value="http://${header.host}${pageContext.request.contextPath}" scope="request" />
<c:set var="THIS" value="${ROOT}${pageContext.request.servletPath}" scope="request" />
//<pre>
/**
 * 本页面生成一段javascript，用于生成 明星空间列表 下拉框选项
 * @param tag 需要设置的下拉框名称
 * @param area 明星所在区域
 * @param selected 设置下拉选项后需要直接选中的选项值
 * @param keep 保留原下拉框中的多少项（一般可设1，默认为0）
 * 示范：
 * <select name="spaceId" id="spaceId"></select>
 * <script src="${THIS}?tag=spaceId&area=0"></script>
 */
var selectTag = document.getElementById("<%=request.getParameter("tag")%>");
selectTag.options.length = ${1*param.keep};

<c:forEach var="line" items="${RESULT_LIST}">
selectTag.options[selectTag.options.length] = new Option("${line.starName}(${line.id})","${line.id}");
</c:forEach>

<c:if test="${!empty param.selected}">
selectTag.value = "${param.selected}";
if(selectTag.selectedIndex < 0 && selectTag.length > 0) selectTag.selectedIndex = 0;
if(selectTag.onchange != null) selectTag.onchange();
</c:if>

<%out.flush();%>
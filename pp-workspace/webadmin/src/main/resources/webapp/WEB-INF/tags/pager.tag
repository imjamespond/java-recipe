<%
Object pager = request.getAttribute("pager");	
if(pager == null) return;

int pageTotal = (Integer)(pager.getClass().getMethod("getPageCount").invoke(pager));        
int total = (Integer)(pager.getClass().getMethod("getTotal").invoke(pager));
int pageNo = (Integer)(pager.getClass().getMethod("getPageNo").invoke(pager));
String params = (String)(pager.getClass().getMethod("getParams").invoke(pager));
String pageUrl = (String)(pager.getClass().getMethod("getPageUrl").invoke(pager));

String url = request.getContextPath(); 
String uri = request.getServletPath();
//System.out.println("url=" + url);
//System.out.println("uri=" + uri);
if (uri.indexOf(".jsp") != -1) {
     /**
     if(uri.indexOf("WEB-INF/pages/adm/") != -1){
         uri = uri.replace("WEB-INF/pages/adm/", "");
     }     
     uri = uri.replace(".jsp", ".do");
     **/

    if(pageUrl != null || pageUrl.length() > 0){
        uri = "/" + pageUrl;
    } else {
        uri = uri.replace(".jsp", ".do");
    }     
    
    if(params != null && params.length() > 0) params = params.replaceAll("&*(pageNo)=[^&]*","");
    url += uri + "?" + params;

    int pageNoEnd = pageNo < 4 ? 20 : pageNo + 17;
    pageNoEnd = pageNoEnd > pageTotal ? pageTotal : pageNoEnd;

    int pageNoBegin = pageNo - (19 - (pageNoEnd - pageNo));
    pageNoBegin = pageNoBegin < 1 ? 1 : pageNoBegin;
	
    StringBuilder sb = new StringBuilder();
    sb.append("共 <font color=red>").append(total).append("</font> 条 ");
    sb.append("第 <font color=red>").append(pageNo).append("</font> 页 / 共 ").append(pageTotal).append(" 页");
	
    if (pageNo > 1) {
        sb.append("<a class='nextLink' href='").append(url);
        sb.append("&pageNo=1' title='首页'>|&lt</a>");
        sb.append("&nbsp;<a class='nextLink' href='").append(url);
        sb.append("&pageNo=").append(pageNo - 1).append("' title='上页'>&lt</a>");
    }

    for (int i = pageNoBegin; i <= pageNoEnd; i ++) {
        if (i == pageNo) {
            sb.append("<span class='currentStep'>&nbsp;").append(pageNo).append("</span>");
        } else {
            sb.append("&nbsp;<a class='step' href='").append(url);
	    sb.append("&pageNo=").append(i).append("'>").append(i).append("</a>");
        }
    }

    if (pageNo < pageTotal) {
        sb.append("&nbsp;<a class='nextLink' href='").append(url);
        sb.append("&pageNo=").append(pageNo + 1).append("' title='下页'>&gt</a>");
        sb.append("&nbsp;<a class='nextLink' href='").append(url);
        sb.append("&pageNo="+pageTotal+"' title='尾页'>&gt|</a>");
    }

    out.print(new String(sb.toString().getBytes("iso-8859-1"), "UTF-8"));
}
%>

/**
 * easyUi插件的测试公用js,存放站点通用方法
 */

/**
 * 返回Tabs
 * @return {*}
 */
function getTabsObjId() {
    return $('#center-tabs');
}

/**
 * 通过title标示，打开选项的tab页。如果tab页已打开，则重新加载。
 * 如果使用$('#center-tabs').load(url)，也能达到效果，单但是无法对打开的tab设置属性。
 *
 * @param href     访问地址
 * @param title    打开的tab页标题
 */
function showpage(href, title) {
    var contents = getTabsObjId();
    //var content = '<iframe scrolling="no" frameborder="0"  src="" style="width:100%;height:100%;"></iframe>';

    //如果当前窗口被打开，先关闭当前窗口，实现刷新。方法待优化。
    //if (contents.tabs('exists', title)) {
    //    closeTab(title);
    //}

    if (contents.tabs('exists', title)) {
//        $.messager.alert('提示消息', '你已经打开一个窗口。请关闭当前窗口，继续操作。', 'info');
        contents.tabs('select', title);
        //refreshTab({tabTitle:title,url:href});
    } else {
        contents.tabs('add', {
            title:title,
            href:href,
            fit:true,
            data:"",
            //overflow:hidden,
            //padding:5,
            //content:content,
            closable:true
            /*tools:[{
             iconCls:'icon-mini-refresh',
             handler:function(){
             refreshTab({tabTitle:title,url:href});
             }
             }]*/
        });
    }

    //$('#center-tabs').load(url);
}

/**
 * 刷新tab
 *
 * example: {tabTitle:'tabTitle',url:'refreshUrl'}
 * 如果tabTitle为空，则默认刷新当前选中的tab
 * 如果url为空，则默认以原来的url进行reload
 */
function refreshTab(cfg) {
    var refresh_tab = cfg.tabTitle ? $('#center-tabs').tabs('getTab', cfg.tabTitle) : $('#center-tabs').tabs('getSelected');
    if (refresh_tab && refresh_tab.find('iframe').length > 0) {
        var _refresh_ifram = refresh_tab.find('iframe')[0];
        var refresh_url = cfg.url ? cfg.url : _refresh_ifram.src;
        //_refresh_ifram.src = refresh_url;

        _refresh_ifram.contentWindow.location.href = refresh_url;
    }
}

/**
 * 关闭tab
 *
 * @param tabTitle 需要关闭的tab标题
 */
function closeTab(tabTitle) {
    window.parent.$('#center-tabs').tabs('close', tabTitle);
}

/**
 * 删除所选记录
 *
 * @param dataTableId
 * @param requestURL
 * @param confirmMessage
 */
function deleteRows(dataTableId, requestURL, confirmMessage) {
    if (null == confirmMessage || typeof(confirmMessage) == "undefined" || "" == confirmMessage) {
        confirmMessage = "确定删除所选记录?";
    }

    var rows = $('#' + dataTableId).datagrid('getSelections');
    var num = rows.length;
    var ids = null;
    if (num < 1) {
        $.messager.alert('提示消息', '请选择你要删除的记录!', 'info');
    } else {
        $.messager.confirm('确认', confirmMessage, function (r) {
            if (r) {
                for (var i = 0; i < num; i++) {
                    if (null == ids || i == 0) {
                        ids = rows[i].id;
                    } else {
                        ids = ids + "," + rows[i].id;
                    }
                }
                $.post(requestURL, {"ids":ids}, function (message) {
                    if (null != message && "" != message) {
                        $.messager.alert('提示消息', message, 'info');
                        //flashTable(dataTableId);
                        //$('#' + dataTableId).datagrid('deleteRow', rows);
                        var indexs = rows.length;
                        for (var i = 0; i < indexs; i++) {
                            //$('#' + dataTableId).datagrid('deleteRow', rows[i]);
                            var row = rows[0];
                            if (row) {
                                var index = $('#' + dataTableId).datagrid('getRowIndex', row);
                                $('#' + dataTableId).datagrid('deleteRow', index);
                            }
                        }
                    } else {
                        $.messager.alert('提示消息', '删除失败！', 'warning');
                    }
                });
            }
        });
    }
}

function deleteRow(dataTableId, requestURL, index, id) {
    $.messager.confirm('确认', "确定删除记录?", function (r) {
        if (r) {
            $.post(requestURL, {"id":id}, function (message) {
                if (null != message && "" != message) {
                    $.messager.alert('提示消息', message, 'info');
                    //flashTable(dataTableId);
                    //$('#' + dataTableId).datagrid('deleteRow', rows);
                    //var indexs = rows.length;
                    $('#' + dataTableId).datagrid('deleteRow', index);
                } else {
                    $.messager.alert('提示消息', '删除失败！', 'warning');
                }
            });
        }
    });
}

/**
 * 刷新DataGrid列表(适用于Jquery Easy Ui中的dataGrid)
 * 注：建议采用此方法来刷新DataGrid列表数据(也即重新加载数据)，不建议直接使用语句
 * $('#dataTableId').datagrid('reload');来刷新列表数据，因为采用后者，如果日后
 * 在修改项目时，要在系统中的所有刷新处进行其他一些操作，那么你将要修改系统中所有涉及刷新
 * 的代码，这个工作量非常大，而且容易遗漏；但是如果使用本方法来刷新列表，那么对于这种修
 * 该需求将很容易做到，而去不会出错，不遗漏。
 *
 * @param dataTableId 将要刷新数据的DataGrid依赖的table列表id
 */
function flashTable(dataTableId) {
    $('#' + dataTableId).datagrid('reload');
}

/**
 * 取到dataGraid中选中项记录(单选)
 *
 * @param dataTableId    数据表格的ID
 */
function getSelected(dataTableId) {
    var selected = $('#' + dataTableId).datagrid('getSelected');
    return selected;
}

/**
 * 取到dataGraid中选中项记录(单选)
 *
 * @param dataTableId    数据表格的ID
 */
function getSelections(dataTableId) {
    var ids = [];
    var rows = $('#' + dataTableId).datagrid('getSelections');
    for (var i = 0; i < rows.length; i++) {
        ids.push(rows[i].id);
    }
    return ids;
}

/**
 * 计算当前窗体宽度的百分比
 *
 * @param percent
 */
function fixWidth(percent) {
    return document.body.clientWidth * percent; //这里你可以自己做调整
}

/**
 * 渲染默认搜索框，包括开始时间，结束时间，搜索列，搜索内容。
 * 如果默认搜索框无法满足需求，可另行组装。
 *
 * @param startDateId      开始时间框id
 * @param endDateId        结束时间框id
 * @param searchMenuId     搜索项菜单id
 * @param toolbarId        工具栏id
 */
function renderingToolbar(startDateId, endDateId, searchMenuId, gridId, toolbarId) {
    var datagrid = $("#" + gridId); // datagrid
    var toolbar = $(".datagrid-toolbar"); // toolbar
    var fields = datagrid.datagrid('getColumnFields');

    for (var i = 0; i < fields.length; i++) {
        var opts = datagrid.datagrid('getColumnOption', fields[i]);
        if (opts.title == undefined || opts.title == null || opts.title == "") continue;
        var muit = "<div name='" + fields[i] + "'>" + opts.title + "</div>";
        $('#' + searchMenuId).html($('#' + searchMenuId).html() + muit);
    }

    var searchToolbar = $("#" + toolbarId);
    toolbar.append(datagrid);
    toolbar.append(searchToolbar);

    $('#' + startDateId).datebox({
        required:false,
        width:100
    });
    $('#' + endDateId).datebox({
        required:false,
        width:100
    });
}


//**************************************************************
//以下是采用iframe方式生成的tab页，主要为了解决老版一些页面无法用新版方式实现的问题，包括生成tab页，刷新tab
//**************************************************************

/**
 * 增加iframe tab页
 *
 * @param title  tab页标题
 * @param href   tab页响应的链接
 * @param icon   tab页图标
 * @param iframeHeight   iframe高度百分比，为空时，默认100%。
 */
function addTabByFrame(href, title, iframeHeight) {
    if (iframeHeight == "" || iframeHeight == undefined || iframeHeight == null) {
        iframeHeight = "100%";
    }
    var tt = $('#center-tabs');
    if (tt.tabs('exists', title)) {//如果tab已经存在,则选中并刷新该tab
        tt.tabs('select', title);
        refreshIframeTab({tabTitle:title, url:href});
    } else {
        if (href) {
            var content = "<iframe scrolling='auto' frameborder='0' style='width: 100%; height: " + iframeHeight + "' src='" + href + "'></iframe> ";
        } else {
            var content = '未实现';
        }
        tt.tabs('add', {
            title:title,
            closable:true,
            content:content,
            overflowY:scroll,
            fit:true
            //iconCls:icon||'icon-default'
        });
    }
}

/**
 * 刷新iframe tab
 * @param cfg
 *example: {tabTitle:'tabTitle',url:'refreshUrl'}
 *如果tabTitle为空，则默认刷新当前选中的tab
 *如果url为空，则默认以原来的url进行reload
 */
function refreshIframeTab(cfg) {
    var refresh_tab = cfg.tabTitle ? $('#tabs').tabs('getTab', cfg.tabTitle) : $('#tabs').tabs('getSelected');
    if (refresh_tab && refresh_tab.find('iframe').length > 0) {
        var _refresh_ifram = refresh_tab.find('iframe')[0];
        var refresh_url = cfg.url ? cfg.url : _refresh_ifram.src;
        //_refresh_ifram.src = refresh_url;
        _refresh_ifram.contentWindow.location.href = refresh_url;
    }
}

/**
 * 通过title标示，打开选项的tab页。如果tab页已打开，则重新加载。
 * 如果使用$('#center-tabs').load(url)，也能达到效果，单但是无法对打开的tab设置属性。
 *
 * @param href     访问地址
 * @param title    打开的tab页标题
 */
function showpageByIframe(href, title, iframeHeight) {

    if (iframeHeight == "" || iframeHeight == undefined || iframeHeight == null) {
        iframeHeight = "100%";
    }
    var tt = window.top.$("#center-tabs", top.document.body);
    if (tt.tabs('exists', title)) {//如果tab已经存在,则选中并刷新该tab
        tt.tabs('select', title);
        refreshIframeTab({tabTitle:title, url:href});
    } else {
        if (href) {
            var content = "<iframe scrolling='auto' frameborder='0' style='width: 100%; height: " + iframeHeight + "' src='" + href + "'></iframe> ";
        } else {
            var content = '未实现';
        }
        tt.tabs('add', {
            title:title,
            closable:true,
            content:content,
            overflowY:scroll,
            fit:true
//iconCls:icon||'icon-default'
        });
    }
}

function formatterdate(val, row) {
    var date = new Date(val);
    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
}

function formatterdatetime(val, row) {
    var date = new Date(val);
    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
}

function formatterNumber(val, row) {
    if (null == val || "" == val || val == 0) {
        return 0
    }
    else {
        return  val;
    }
}

/**
 * 点击datagrid行时，禁止选中
 *
 */
function bindRowsEvent(datagrid) {
    var panel = $('#' + datagrid).datagrid('getPanel');
    var rows = panel.find('tr[datagrid-row-index]');
    rows.unbind('click').bind('click', function (e) {
        return false;
    });
    rows.find('div.datagrid-cell-check input[type=checkbox]').unbind().bind('click', function (e) {
        var index = $(this).parent().parent().parent().attr('datagrid-row-index');
        if ($(this).attr('checked')) {
            $('#' + datagrid).datagrid('selectRow', index);
        } else {
            $('#' + datagrid).datagrid('unselectRow', index);
        }
        e.stopPropagation();
    });
}

/**
 * 返回datagrid中指定的数据行参数对象。
 * 例如：行中有id,name,sex等属性，返回row后，可以通过row.id, row.name的方式取得对应属性值
 *
 * @param datagrid 列表id
 * @param index 行的索引号
 * @return 数据行对象
 */
function returnDatagridRow(datagrid, index) {
    var row = $('#' + datagrid + '').datagrid('getRows')[index];
    return row;
}

/**
 * @param requestURL 请求链接
 * @param index     记录索引
 * @param id       数据库记录的ID
 * @param tabId   dataGrid的TableID
 * @param opType 0:修改 1：删除
 */
function operator(requestURL, index, id, tabId, opType) {
    var ids = [];
    ids.push(id);
    if (opType == 0) {
        $.messager.confirm('确认', "确定修改记录?", function (r) {

            if (r) {
                $.post(requestURL, {"id":id, "ids":ids.toString()}, function (json) {
                    var msg = jsonToObject(json);
                    if (msg.status == pp.webresult.SUCCESS) {
                        $.messager.alert('温馨提示', msg.message, 'info');
                        $("#" + tabId).datagrid('reload');
                    } else {
                        $.messager.alert('温馨提示', '修改失败！', 'warning');
                    }
                });
            }
        });
    } else if (opType == 1) {
        $.messager.confirm('确认', "确定删除记录?", function (r) {
            if (r) {
                $.post(requestURL, {"id":id, "ids":ids.toString()}, function (json) {
                    var msg = jsonToObject(json);
                    if (msg.status == pp.webresult.SUCCESS) {
                        $.messager.alert('温馨提示', msg.message, 'info');
                        $("#" + tabId).datagrid('reload');
                    } else {
                        $.messager.alert('温馨提示', '删除失败！', 'warning');
                    }
                });
            }
        });
    }
}

//新开tab “编辑音乐”
function editSong(id) {
    closeTab('编辑音乐');
    showpage('/music/song-manage!editSong?listVO.songId=' + id , '编辑音乐');
}

//更改歌曲状态
function updateStatus(id,status){
    var requestURL = "/music/song-manage!updateStatus";
    $.post(requestURL, {"listVO.songId":id,"listVO.status":status}, function (msg) {
        var json = eval("(" + msg + ")");
        var status = json.status;
        if (status == 100) {
            $.messager.alert('Success', "恭喜，操作成功！", 'info');
            $('#manageSongList').datagrid('reload');
        } else if (status == 103) {
            $.messager.alert('Error', "操作失败，参数非法，联系技术！", 'info');
        } else{
            $.messager.alert('Error', "操作失败，请稍后再试！", 'info');
        }
    });
}

//更改歌曲审核状态
function updateApproved(id,approved){
    var requestURL = "/music/song-manage!updateApproved";
    $.post(requestURL, {"listVO.approved":approved,"listVO.songId":id}, function (msg) {
        var json = eval("(" + msg + ")");
        var status = json.status;
        if (status == 100) {
            $.messager.alert('Success', "恭喜，操作成功！", 'info');
            $('#approvedSongList').datagrid('reload');
        } else if (status == 103) {
            $.messager.alert('Error', "操作失败，参数非法，联系技术！", 'info');
        } else{
            $.messager.alert('Error', "操作失败，请稍后再试！", 'info');
        }
    });
}

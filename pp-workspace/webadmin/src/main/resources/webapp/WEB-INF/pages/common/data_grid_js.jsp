<script type="text/javascript">
    function formatterdate(val, row) {
        var date = new Date(val);
        return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' + date.getHours() + ":" + date.getMinutes();
    }

    function formatterNumber(val, row) {
        if (null == val || "" == val || val == 0) {
            return 0
        }
        else {
            return  val;
        }
    }
    function formatterStatus(val, row) {
        if (null == val || "" == val || val == 0) {
            return "举办中";
        }
        else if (val == 1) {
            return  "已结束";
        } else {
            return "已删除";
        }
    }
    function formatterRecordStatus(val, row) {
        if (null == val || "" == val || val == 0) {
            return "<font color='red'>待审</font>";
        }
        else if (val == 1) {
            return  "<font color='green'>已审</font>";
        } else {
            return "未知状态";
        }
    }
</script>
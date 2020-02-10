
var FtpManage = {
    id: "FtpManage",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
FtpManage.initColumn = function () {
    return [
			{field: 'selectItem', radio: true},
            {title: '工号', field: 'userid', visible: true, align: 'center', valign: 'middle'},
            {title: '主目录', field: 'homedirectory', visible: true, align: 'center', valign: 'middle'},
            {title: '启用', field: 'enableflag', visible: true, align: 'center', valign: 'middle',formatter:FtpManage.statusView},
            {title: '最大空闲时间(秒)', field: 'idletime', visible: true, align: 'center', valign: 'middle'},
            {title: '访问权限', field: 'permission', visible: true, align: 'center', valign: 'middle' },
            {title: '管理员权限', field: 'adminpermission', visible: true, align: 'center', valign: 'middle',formatter:FtpManage.adminpermissionView},
            {title: '最大的登录限制', field: 'maxloginnumber', visible: true, align: 'center', valign: 'middle'},
            {title: '同ip最大登录限制', field: 'maxloginperip', visible: true, align: 'center', valign: 'middle'},
            {title: '下载速率(byte)', field: 'downloadrate', visible: true, align: 'center', valign: 'middle'},
            {title: '上传速率(byte)', field: 'uploadrate', visible: true, align: 'center', valign: 'middle'},
            {title: '当前连接', field: 'currentLoginNumber', visible: true, align: 'center', valign: 'middle',formatter:FtpManage.currentLoginView},
            {title: '有效期', field: 'expires', visible: true, align: 'center', valign: 'middle'}
    ];
};

FtpManage.statusView = function(value, row, index){
	return value?"是":"否";
}
FtpManage.adminpermissionView = function(value, row, index){
	return value?"是":"否";
}

FtpManage.currentLoginView = function(value, row, index){
	if(value>0){
		return "使用中("+value+")";
	}else{
		return "离线";
	}
}


FtpManage.switcherView = function(path){
	var ajax = new $ax(Feng.ctxPath + "/switcherView", function (data) {
		if(data.code==0||data.code==2){
			location.reload();
		}
	}, function (data) {
		Feng.error("异常!");
	});
	ajax.set("view",path);
	ajax.start();
}
FtpManage.exit = function(){
	var ajax = new $ax(Feng.ctxPath + "/exit", function (data) {
		if(data.code==0||data.code==2){
			location.reload();
		}
	}, function (data) {
		Feng.error("异常!");
	});
	ajax.start();
}



FtpManage.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        FtpManage.seItem = selected[0];
        return true;
    }
};

FtpManage.openAdd = function () {
    var index = layer.open({
        type: 2,
        title: '新增用户',
        area: ['900px', '600px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/user/add'
    });
    this.layerIndex = index;
};

FtpManage.import = function () {
	var url=Feng.ctxPath + '/user/importView';
	parent.layer.open({
	    type:2,
	    shadeClose : true,
	    shade : 0.3,
	    title : '导入EXCEL',
	    skin: 'layui-layer-rim', //加上边框
	    area: ['300px', '240px'], //宽高
	    content: url
	});
};

FtpManage.export = function () {
	var url = Feng.ctxPath+"/user/export?";
	url+="userid="+$("#userid").val();
	url+="&enableflag="+$("#enableflag").val();
	url+="&onLine="+$("#onLine").val();
	url+="&adminpermission="+$("#adminpermission").val();
	window.location.href = Feng.ctxPath+url;
}

FtpManage.openDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户详情',
            area: ['900px', '600px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/user/modify?userid=' + FtpManage.seItem.userid
        });
        this.layerIndex = index;
    }
};

FtpManage.delete = function () {
    if (this.check()) {
    	if(confirm("确实要删除吗？")){
			var ajax = new $ax(Feng.ctxPath + "/user/delete", function (data) {
				if(data.code==0){
					Feng.success("删除成功!");
					FtpManage.table.refresh();
				}else if(data.code==2){
					location.reload();
				}else{
					Feng.error(data.msg);
				}
			}, function (data) {
				Feng.error("删除失败!");
			});
			ajax.set("userid",this.seItem.userid);
			ajax.start();
    	}
    }
};

FtpManage.search = function () {
	var queryData = {};
	queryData.userid=$("#userid").val();
	queryData.enableflag=$("#enableflag").val();
	queryData.onLine=$("#onLine").val();
	queryData.adminpermission=$("#adminpermission").val();
    FtpManage.table.refresh({query: queryData});
};
FtpManage.reset = function(){
	$("#userid").val("");
	$("#enableflag").val("");
	$("#onLine").val("");
	$("#adminpermission").val("");
	FtpManage.search();
};
FtpManage.onChangeStatus = function(){
	FtpManage.search();
};

$(function () {
    var defaultColunms = FtpManage.initColumn();
    var table = new BSTable(FtpManage.id, "/user/list", defaultColunms);
    table.setPaginationType("server");
    FtpManage.table = table.init();
});

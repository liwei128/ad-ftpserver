
var FtpLogManage = {
    id: "FtpLogTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
FtpLogManage.initColumn = function () {
    return [
            {title: '工号', field: 'userid', visible: true, align: 'center', valign: 'middle'},
            {title: 'ip', field: 'ip', visible: true, align: 'center', valign: 'middle'},
            {title: '操作', field: 'operation', visible: true, align: 'center', valign: 'middle'},
            {title: '文件路径', field: 'filepath', visible: true, align: 'center', valign: 'middle'},
            {title: '访问时间', field: 'accessTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

FtpLogManage.switcherView = function(path){
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
FtpLogManage.exit = function(){
	var ajax = new $ax(Feng.ctxPath + "/exit", function (data) {
		if(data.code==0||data.code==2){
			location.reload();
		}
	}, function (data) {
		Feng.error("异常!");
	});
	ajax.start();
}


FtpLogManage.export = function () {
	var userid = $("#userid").val();
	var ip = $("#ip").val();
	if((!userid)&&(!ip)){
		Feng.info("请先选择工号或者ip！");
		return ;
	}
	
	var url = Feng.ctxPath+"/log/export?userid="+userid+"&ip="+ip;
	window.location.href = Feng.ctxPath+url;
}



FtpLogManage.search = function () {
	var queryData = {};
	queryData.userid=$("#userid").val();
	queryData.ip=$("#ip").val();
	FtpLogManage.table.refresh({query: queryData});
};
FtpLogManage.reset = function(){
	$("#userid").val("");
	$("#ip").val("");
	FtpLogManage.search();
};

$(function () {
    var defaultColunms = FtpLogManage.initColumn();
    var table = new BSTable(FtpLogManage.id, "/log/list", defaultColunms);
    table.setPaginationType("server");
    FtpLogManage.table = table.init();
});

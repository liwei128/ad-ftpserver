
var FtpStatistics = {};



FtpStatistics.switcherView = function(path){
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
FtpStatistics.exit = function(){
	var ajax = new $ax(Feng.ctxPath + "/exit", function (data) {
		if(data.code==0||data.code==2){
			location.reload();
		}
	}, function (data) {
		Feng.error("异常!");
	});
	ajax.start();
}



FtpStatistics.refresh = function () {
	var ajax = new $ax(Feng.ctxPath + "/statistics", function (data) {
		if(data.code==0){
			$("#startTime").html(data.data.starttime);
			$("#totalUploadNumber").html(data.data.totaluploadnumber);
			$("#totalDownloadNumber").html(data.data.totaldownloadnumber);
			$("#totalDeleteNumber").html(data.data.totaldeletenumber);
			$("#totalUploadSize").html(Math.ceil(data.data.totaluploadsize/1024));
			$("#totalDownloadSize").html(Math.ceil(data.data.totaldownloadsize/1024));
			$("#totalDirectoryCreated").html(data.data.totaldirectorycreated);
			$("#totalDirectoryRemoved").html(data.data.totaldirectoryremoved);
			$("#totalConnectionNumber").html(data.data.totalconnectionnumber);
			$("#currentConnectionNumber").html(data.data.currentconnectionnumber);
			$("#totalLoginNumber").html(data.data.totalloginnumber);
			$("#totalFailedLoginNumber").html(data.data.totalfailedloginnumber);
			$("#currentLoginNumber").html(data.data.currentloginnumber);
		}else if(data.code==2){
			location.reload();
		}else{
			Feng.error("失败!");
		}
	}, function (data) {
		Feng.error("异常!");
	});
	ajax.start();
};


$(function () {
	FtpStatistics.refresh();
	setInterval(function() {
		FtpStatistics.refresh();
	},5000)
});

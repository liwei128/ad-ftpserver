/**
 * 初始化软件资产CMDB详情对话框
 */
var FtpUserInfoDlg = {
	ftpUserInfoData : {}
};

/**
 * 清除数据
 */
FtpUserInfoDlg.clearData = function() {
    this.ftpUserInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
FtpUserInfoDlg.set = function(key, val) {
    this.ftpUserInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
FtpUserInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
FtpUserInfoDlg.close = function() {
    parent.layer.close(window.parent.FtpManage.layerIndex);
}

/**
 * 收集数据
 */

FtpUserInfoDlg.collectData = function() {
    this
    .set('userid')
    .set('homedirectory')
    .set('enableflag')
    .set('idletime')
    .set('writepermission')
    .set('adminpermission')
    .set('maxloginnumber')
    .set('maxloginperip')
    .set('downloadrate')
    .set('uploadrate')
    .set('expires');
}

/**
 * 提交添加
 */
FtpUserInfoDlg.addSubmit = function() {
    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/user/doAdd", function(data){
    	if(data.code==0){
    		Feng.success("添加成功!");
            window.parent.FtpManage.table.refresh();
            FtpUserInfoDlg.close();
    	}else if(data.code==2){
			location.reload();
		}else{
    		Feng.error(data.msg);
    	}
    },function(data){
        Feng.error("添加失败!");
    });
    ajax.set(this.ftpUserInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
FtpUserInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/user/doModify", function(data){
    	if(data.code==0){
    		Feng.success("修改成功!");
            window.parent.FtpManage.table.refresh();
            FtpUserInfoDlg.close();
    	}else if(data.code==2){
			location.reload();
		}else{
			Feng.error(data.msg);
    	}
        
    },function(data){
        Feng.error("修改失败!");
    });
    ajax.set(this.ftpUserInfoData);
    ajax.start();
}

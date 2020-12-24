var CommonUtil={
	//同步获取数据
	getAjaxData:function (dataUrl,data){
		var rData;
		$.ajax({
			type : "POST",
			url : dataUrl,
			data:data,
			async : false,
			dataType : 'json',
			success : function(data) {
				rData = data;
			}
		});
		return rData;
	},
	//异步提交数据
	submitAjaxData:function(dataUrl,data,callback){
		$.ajax({
			type : "POST",
			url : dataUrl,
			data:data,
			async : true,
			dataType : 'json',
			success : function(data) {
				callback(data);
			}
		});	
	}
}

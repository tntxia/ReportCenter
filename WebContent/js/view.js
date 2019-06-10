$(function(){
	var url = window.location.search;
	var params = {};
	if(url && url.length){
		url = url.substring(1);
		var arr = url.split("&");
		
		for(var i=0;i<arr.length;i++){
			var s = arr[i];
			var p = s.split("=")[0];
			var v = s.split("=")[1];
			params[p] = v;
		}
	}
	
	var id = params.id;
	
	$.ajax({
		url: 'system!getSystemInfo.do'
	}).done(res=> {
		window.systemInfo = res;
		loadDetail(id);
	})
	
});

function loadDetail(id) {
	
	$.ajax({
		url:'report!detail.do',
		data:{
			id:id
		}
	}).done(function(data){
		$("#name").text(data.name);
		var statusShow = "未完成";
		if(data.status=="data"){
			statusShow = "正在获取数据"
		}else if(data.status=="file"){
			statusShow = "正在上传文件";
		}else if(data.status=="finish"){
			statusShow = "完成";
			$("#download-div").empty();
			if(data.file_id){
				let filecenter = window.systemInfo.filecenter;
				let host = filecenter.host;
				let port = filecenter.port;
				if(!port) {
					port = "80"
				}
				let contextPath = filecenter.contextPath;
				let downloadPath = "http://" + host + ":" + port + "/" + contextPath
				var a = $("<a>",{
					text:'点击下载',
					href: downloadPath + "/file!download.do?uuid="+data.file_id,
					"target":"_blank"
				});
				
				$("#download-div").append(a);
			}
			return;
		}else if(data.status=="exception") {
			statusShow = "异常";
		}
		$("#status").html(statusShow);
		
		setTimeout(function() {
			loadDetail(id)
		}, 1000);
	})
}
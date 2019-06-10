(function(name,module){
if(!window.modules){
window.modules=Object.create(null);
};
window.modules[name]=module();
})('template',function(){
var module=Object.create(null);
var exports = Object.create(null);
module.exports=exports;
exports.init = function(){
	
	let filecenter = window.systemInfo.filecenter;
	let host = filecenter.host;
	let port = filecenter.port;
	if(!port) {
		port = "80"
	}
	let contextPath = filecenter.contextPath;
	
	new Vue({
		el: '#template-app',
		data: {
		},
		mounted() {
			this.loadData();
		},
		methods: {
			loadData() {
				let me = this;
				$.ajax({
					url: "template!list.do"
				}).done(res=> {
					me.$refs["templateList"].setRows(res.rows);
				})
			},
			add() {
				router.goRoute("template_add");
			},
			del(row) {
				let me = this;
				$.ajax({
					url:'template!del.do',
					data:{
						id:row.id
					}
				}).done(function(data){
					if(data.success){
						alert("删除成功");
						me.loadData();
					}else{
						alert("删除失败")
					}
				}).fail(function(){
					alert("删除异常")
				})
			},
			exports(row) {
				let me = this;
				$.ajax({
					url:'template!export.do',
					data:{
						id:row.id
					}
				}).done(function(data){
					if(data.success){
						let downloadPath = "http://" + host + ":" + port + "/" + contextPath + "/file!download.do?uuid="+data.uuid
						window.open(downloadPath);
					}else{
						alert("删除失败")
					}
				}).fail(function(){
					alert("删除异常")
				})
			},
			toImport() {
				this.$refs["hiddenFileInput"].click();
			},
			uploadFile(e) {
				let file = event.target.files[0];
				console.log(e);
				let param = new FormData();     // 创建form对象
                param.append('file', file);     //对应后台接收图片名
                $.ajax({
                	type: "POST",
                	data: param,
                	url: 'template!templateImport.do',
                	contentType: false,
                	processData: false,
                }).success(function (data) {
	               if (data.status) {
	            	   console.log(data.url);
		           } else {
		               console.log(data.msg);
		           }
                }).error(function (data) {
                	alert(data);
                    console.log(data);
               });
			}
		}
	})
	
};
return module.exports;});
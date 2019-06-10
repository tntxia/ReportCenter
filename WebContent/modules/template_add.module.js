(function(name,module){
if(!window.modules){
window.modules=Object.create(null);
};
window.modules[name]=module();
})('template_add',function(){
var module=Object.create(null);
var exports = Object.create(null);
module.exports=exports;
exports.init = function(){
	
	var form = $("#form").buildform({
		actions:{
			add:function(){
				var params = this.getParamMap();
				$.ajax({
					url:'template!add.do',
					type:'post',
					data:params
				}).done(function(data){
					if(data.success){
						alert("操作成功");
						router.goRoute("template");
					}else{
						alert("操作失败");
					}
				}).fail(data=>{
					alert("操作异常");
				})
			}
		}
	});
	
};
return module.exports;});
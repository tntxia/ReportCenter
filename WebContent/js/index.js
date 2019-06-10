$(function(){
	
	
	
	$.ajax({
		url: 'system!getSystemInfo.do'
	}).done(res=> {
		window.systemInfo = res;
		console.log(res);
		initRoute();
	});
	
	function initRoute() {
		let target = $("#mainDiv");	
		// 绑定路由，设置路由变化影响的区域
		router.register({
			target:target,
			defaultModule:'template'
		});
	}
	
})
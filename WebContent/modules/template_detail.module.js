(function(name,module){
if(!window.modules){
window.modules=Object.create(null);
};
window.modules[name]=module();
})('template_detail',function(){
var module=Object.create(null);
var exports = Object.create(null);
module.exports=exports;
exports.init = function(){
	
	var id = router.getParam('id');
	
	new Vue({
		el: '#detail-container',
		data: {
			detail: {
				id: null,
				name: null,
				display_name: null,
				service_url: null
			}
		},
		mounted() {
			this.loadData();
		},
		methods: {
			loadData() {
				let me = this;
				$.ajax({
					url: 'template!detail.do',
					data: {
						id: id
					}
				}).done(res=> {
					me.detail = res;
					let cols = res.cols;
					me.$refs["colsTable"].setRows(cols);
				})
			},
			update() {
				let param = this.detail;
				$.ajax({
					url: 'template!update.do',
					data: param
				}).done(res=> {
					if (res.success) {
						alert("修改成功");
					}else {
						alert("修改失败");
					}
				}).fail(e=> {
					console.error(e);
				});
			},
			generateReport:function(){
				$.ajax({
					url:'template!generate.do',
					type:'post',
					data:{id}
				}).done(function(data){
					if(data.success){
						window.open("view.mvc?id="+data.uuid);
					}else{
						alert("操作失败");
					}
				}).fail(data=>{
					alert("操作异常");
				})
			},
			add() {
				this.dataset.rows.push({
					name: null,
					field: null
				})
			},
			save(row) {
				if (row.id) {
					$.ajax({
						url: 'template!updateCol.do',
						data: row
					}).then(res=> {
						window.location.reload();
					});
				} else {
					
					row.template_id = id;
					$.ajax({
						url:'template!addCol.do',
						type:'post',
						data:row
					}).done(function(data){
						if(data.success){
							window.location.reload();
						}else{
							alert("操作失败");
						}
					}).fail(function(){
						alert("操作异常");
					})
				}
			},
			exports() {
				$.ajax({
					url: 'template!export.do',
					data: {
						id: id
					}
				}).done(function(){
					
				})
			}
		}
	})
	
//	var grid = new BootstrapGrid({
//		target:$("#cols_table"),
//		url:"template!listCols.do",
//		editable:true,
//		paramMap:{
//			id:id
//		},
//		cols : [ {
//			label:'列名',
//			field:'name'
//		},{
//			label:'对应',
//			field:'field'
//		}]
//	});
//	grid.init();
	
//	$("#cols_toolbar").buildform({
//		actions:{
//			add:function(){
//				grid.addRow();
//			},
//			save:function(){
//				var rows = grid.getRows();
//				var data = rows[rows.length-1];
//				data.template_id = id;
//				$.ajax({
//					url:'template!addCol.do',
//					type:'post',
//					data:data
//				}).done(function(data){
//					if(data.success){
//						window.location.reload();
//					}else{
//						alert("操作失败");
//					}
//				}).fail(function(){
//					alert("操作异常");
//				})
//			}
//		}
//	})
	
	
};
return module.exports;});
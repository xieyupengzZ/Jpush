$(function(){
	index.main();
})
var index = {
	
	
	main : function(){
		index.initPage();
		index.inittabs();
	},
	
	inittabs : function(){
		//把生成的tab铺满 容器
		$('#tabs').tabs({
			fit : true,
			border : false,
		});
	},
	
	initPage : function(){
		$("#ultree").tree({
			onClick:function(node){
				index.openNewTab(node.id,node.text);
			}
		})
	},
	
	openNewTab : function(id,text){
		if ($("#tabs").tabs('exists', text)) {
			$('#tabs').tabs('select', text);
		}else if(id=='jpush'){
			$("#tabs").tabs('add',{
				title : text,
				closable : true,
				content : index.createFrame("jpushBulk.html"),
			});
		}
	},
	createFrame : function(url){
		var s = '<iframe scrolling="auto" frameborder="0"  src="' + url
		+ '" style="width:100%;height:100%;"></iframe>';
		return s;
	}
}
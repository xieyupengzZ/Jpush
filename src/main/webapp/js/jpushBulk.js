$(document).ready(function(){
	jpushBulk.main();
});

var jpushBulk = {
		
	main : function(){
		jpushBulk.init();
	},
	
	init : function(){
		$('#btnSearch').bind('click',jpushBulk.submitForm);//通过jquery的bind方式绑定点击函数，jpushBulk.submitForm加（）出错
		$('#infoWin').window({
			title:"消息窗口",
			width:200,
			height:100,
			inline:false,
			model:true,
			closable:false,
			resizable:false,
			draggable:false,
			collapsible:false,
			minimizable:false,
			maximizable:false,
			closed:true
		})
	},
	
	submitForm : function(){
		if(!$("input[type='checkbox']").is(':checked')){//细节：冒号
			$.messager.show({
				title:'温馨提示',
				timeout:5000,
				showType:'slide',
				msg:"<span style='color:red;'>请选择推送平台</span>"
			});
		}else{
					$('#form1').form('submit',{
					url:com.getRootPath()+'/submitForm.do',
					onSubmit : function(){
						if($(this).form('validate')==true){
							//$("#handin").attr("disabled","false");
							$("#btnSearch").linkbutton('disable');
							$('#btnSearch').unbind();//移除该元素的事件处理程序
							$('#infoWin').window("open");
						}
						return $(this).form('validate');
					},
				success : function(result){
					var data = eval('(' + result + ')');
					if (parseInt(data.info.code) != 0) {
						$('#infoWin').window("close");
						//$("#handin").removeAttr("disabled");
						$("#btnSearch").linkbutton('enable');
						$('#btnSearch').bind('click',jpushBulk.submitForm);
						$.messager.show({
						title:'温馨提示',
						timeout:2000,
						showType:'slide',
						msg:"<span style='color:red;'>"+data.info.mes+"</span>"
					});
					} else {
							$('#infoWin').window("close");
							//$("#handin").removeAttr("disabled");
							$("#btnSearch").linkbutton('enable');
							$('#btnSearch').bind('click',jpushBulk.submitForm);
							$.messager.show({
								title:'温馨提示',
								timeout:2000,
								showType:'slide',
								msg:"<span style='color:green;'>"+data.info.mes+"</span>"
							});
						}
					}
				})
			}
		},
		cleanFile : function(){
			var file = $("#file") ;
			file.after(file.clone().val(""));      
			file.remove();
		},
}
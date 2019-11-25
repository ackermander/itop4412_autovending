<%@page import="acmd.terminal.common.Administrator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="JS/jq.js"></script>
<style type="text/css">
.invi{
	display:none
}
img{
	height:200px;
}
</style>
<title>管理页面</title>
</head>
<body>
<%
	int conf = (int) session.getAttribute("conf");
	if (conf >= 0) {
%>
<input value='<%=conf%>' type='hidden' id='conf'> 配置数据库
<br>
<form>
	数据库地址: <input name="jdbcUrl" value="" id="url"
		placeholder='输入数据库ip地址和端口号' style='width: 200px'> 数据库用户: <input
		name="usr" id="usr"> 数据库密码: <input type="password" name="psw"
		id='psw'> <input type="button" value="确认" id='dbsub'>
		<!-- <input type="button" value='connection test' id='ct'> -->
</form>
<hr>
<script type="text/javascript">	
	var tmla = {};
	$('#dbsub').click(function() {
		$.ajax({
			url : 'dbconf',
			type : 'post',
			data : {
				url : $('#url').val(),
				usr : $('#usr').val(),
				psw : $('#psw').val(),
				version : 0,
				conf : $('#conf').val()
			},
			success : function(r) {
				var resp = JSON.parse(r);
				alert(resp.out);
				location.reload();
			}
		})
	})

	$('#ct').click(function() {
		location.reload();
		/*
		$.ajax({
			url : 'dbconf',
			type : 'post',
			data : {
				version : 1
			},
			success : function() {
				alert('ok');
			}
		})
		*/
	})
</script>
<%
	}
	if (conf > 0) {
%>
<!-- 创建新的管理员:
<br> 用户名:
<input> 密码:
<input>
<input type='button'>
<hr> -->
在线的终端:<br>
<div class='online-terminal'>已注册:<div class='reg'></div>未注册:<div class='unreg'></div></div>
<script type="text/javascript">
var uuidsdiv = $('.reg');
	$.ajax({
		url:'settings',
		type:'post',
		data:{'action':9},
		success:function(r){
			console.log(r);
			var unregs = r.unregs;
			var regs = r.uuids;
			var unregdiv = $('.unreg');
			for(c in unregs){
				var m = unregs[c];
				unregdiv.append(m);
				unregdiv.append('<br>');
			}
			for(c in regs){
				var nbr = regs[c].nbr;
				var uuid = regs[c].uuid;
				uuidsdiv.append('设备名称: ' + nbr + ' 设备uuid: ' + uuid)
				uuidsdiv.append('<br>')
			}
		}
	})
</script>
<hr>
配置终端:<br> 已有的终端:
<select id='tmns'><option value='-1'>请选择</option></select> 终端下的货物:
<select id='tml-good'></select>
<br> 
增加货物种类:
<button id='add-kind-btn'>增加种类</button>
修改货物种类:
<button id='alter-kind-btn'>修改货物种类</button>
增加货物:
<button id='add-good-btn'>增加货物</button>
修改货物:
<button id='alter-good-btn'>修改货物</button>
增加终端:
<button id='add-terminal-btn'>增加终端</button>
<!-- 修改终端:
<button id='cg-tml-btn'>修改终端</button> -->
<hr>
<div class='btn-evt'></div>
<div class='invi-alter-kind invi'>
	选择货物:<select class='all-good'></select>新名称:<input class='new-kind-name'>
	新价格:<input class='new-kind-price'>新描述:<input class='new-kind-desc'>
	新图片:<input class='new-kind-img' type='file'><input type="button" class='alter-btn' value='修改'>
	<div class='kind-img'></div>
</div>
<div class='alter-good invi'>
新价格:<input class='new-good-price'>存货:<input class='new-good-stock'>货物位置:行:<input class='new-x'>列:<input class='new-y'>
图片:<select class='pics'></select><input value='修改' type='button' class='alter-good'><!-- <input value='删除' type="button" class='drop-good-from-tml'> --><div class='view-pic'></div>
</div>
<script type="text/javascript">
	var evt = $('.btn-evt')
	var tmls = $("#tmns")
	var allgood = {};
	function getTerminalSelected(){
		return $('#tmns option:selected').val();
	}
	$('#add-kind-btn').click(function(){
		evt.html('');
		var h = "货物名称: <input class='kind-name'>货物价格:<input class='kind-price'>货物描述:<input class='kind-desc'>货物图片:<input type='file' class='kind-img'>";
		var btn = document.createElement('input');
		btn.type='button'
		btn.value='添加';
		btn.onclick = function(){
			var fd = new FormData();
			var name = $('.kind-name').val();
			var price = $('.kind-price').val();
			var desc = $('.kind-desc').val();
			var fo = $('.kind-img')[0].files[0];
			var result = true;
			var path = null;
			fd.append('file', fo);
			$.ajax({
				url:'upload',
				data: fd,
				type: "post",
				dataType: 'json',
	            cache: false,//上传文件无需缓存
	            processData: false,//用于对data参数进行序列化处理 这里必须false
	            contentType: false,
				success:function(r){
					console.log(r);
					if(r.result){
						console.log('interval');
						path = r.path;
						result = false;
						//console.log(name + price + desc + url);
						$.ajax({
							url:'settings',
							type:'post',
							data:{'action':3, 'name':name, 'price':price, 'desc':desc, 'url':path},
							success: function(resp){
								if(resp.result){
									alert('添加货物成功');
								}else{
									alert('添加货物失败')
								}
								evt.html('')
							}
						})
					}
				}
			})
			/*
			*/
		}
		evt.html(h);
		evt.append(btn);
	})
	$('#add-terminal-btn').click(function() {
		evt.html('');
		var h = "终端名称: <input class='terminal-nbr' name='nbr'>终端uuid: <input class='terminal-uuid' name='uuid'>";
		var btn = document.createElement('input');
		btn.value = '添加终端';
		btn.type = 'button';
		btn.onclick = function() {
			var nbr = $('.terminal-nbr').val();
			var uuid = $('.terminal-uuid').val();
			$.ajax({
				url : 'settings',
				type : 'post',
				data : {
					'action' : 1,
					'nbr' : nbr,
					'uuid' : uuid
				},
				success : function(resp) {
					console.log(resp);
					var result = resp.result;
					if (result) {
						var opt = document.createElement('option');
						opt.value = resp.terminal_id;
						opt.text = nbr;
						tmls.append(opt);
					}
					evt.html("");
				}
			})
		}
		evt.html(h);
		evt.append(btn);
	})
	var goods;
	$('#add-good-btn').click(function(){
		evt.html('');
		var tml = getTerminalSelected();
		if(tml == -1){
			alert('请选择终端操作')
			return;
		}
		
		var h = "货物:<select class='goods'></select> 货物价格: <input class='good-price' name='price'>存货: <input class='good-stock' name='stock'>货物位置:行:<input class='good-x' name='x'>列:<input class='good-y' name='y'>";
		var btn = document.createElement('input');
		btn.value = '添加货物';
		btn.type = 'hidden';
		btn.onclick = function(){
			var gd = $('.goods option:selected').val();
			var gdid = goods[gd].id;
			var urlid = goods[gd].urlId;
			var x = $('.good-x').val();
			var y = $('.good-y').val();
			var price = $('.good-price').val();
			var stock = $('.good-stock').val();
			$.ajax({
				url:'settings',
				type:'post',
				data:{'action':2, 'tml': tml, 'gdid':gdid, 'x': x, 'y': y, 'price': price, 'stock': stock, 'urlid':urlid},
				success: function(resp){
					if(resp.result){
						alert('终端货物添加成功')
					}else{
						alert('终端货物添加失败')
					}
					evt.html("");
				}
			})
		}
		evt.html(h);
		//查找到所有的货物, 并添加到select 中
		$.ajax({
			url:'settings',
			type:'post',
			data:{'action':4},
			success:function(r){
				btn.type='button';
				if(r.result){
					goods = r.goods;
					console.log(goods);
					for(i in r.goods){
						var good = goods[i];
						var opt = document.createElement('option');
						//val表示的是good在goods中的位置, 不是good 的id
						opt.value = i;
						opt.text = good.name;
						$('.goods').append(opt);
					}
				}
			}
		})
		evt.append(btn);
	})
	function gettmns() {
		$.ajax({
			url : 'settings',
			type : 'post',
			data : {
				'action' : 0
			},
			success : function(resp) {
				if(resp.result){
					tmla = resp.terminals;
					console.log('tmla: ' + JSON.stringify(tmla));
					for(n in resp.terminals){
						var t = resp.terminals[n]; 
						var opt = document.createElement('option');
						opt.value = t.terminalId;
						opt.text = t.terminalNumber;
						tmls.append(opt);
						
					}
				}
			}
		})
	}
	gettmns();
</script>
<script type="text/javascript" src='JS/admin.js'></script>
<%
	}
%>
</body>
</html>
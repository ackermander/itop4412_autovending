//alert('admin.js')
var alter_kind_html = $('.invi-alter-kind').html();
var tml_good = $('#tml-good');
var tml_goods;
tmls.change(function() {
	var value = this.value
	if (value != -1) {
		$.ajax({
			url : 'settings',
			type : 'post',
			data : {
				'action' : 5,
				'tmlid' : value
			},
			success : function(r) {
				if (r.result) {
					tml_goods = r.goods;
					tml_good.html('')
					console.log(tml_goods)
					for (i in tml_goods) {
						var good = tml_goods[i];
						var opt = document.createElement('option');
						opt.value = i;
						opt.text = good.name;
						tml_good.append(opt);
					}
				} else {

				}
			}
		})
	} else {
		tml_good.html('')
	}
})
tml_good.change(function() {

})
function allGood(select) {
	$.ajax({
		url : 'settings',
		type : 'post',
		data : {
			'action' : 4
		},
		success : function(r) {
			if (r.result) {
				goods = r.goods;
				console.log(goods);
				for (i in r.goods) {
					var good = goods[i];
					var opt = document.createElement('option');
					// val表示的是good在goods中的位置, 不是good 的id
					opt.value = i;
					opt.text = good.name;
					select.append(opt);
				}
				setKindImg(select);
			}
		}
	})
}
function setKindImg(select) {
	var ed = select.find('option:selected');
	var urlid = goods[ed.val()].urlId;
	var img_c = $('.kind-img')
	$.ajax({
		url : 'settings',
		type : 'post',
		data : {
			'action' : 6,
			'urlid' : urlid
		},
		success : function(r) {
			console.log(r)
			if (r.result) {
				var img = document.createElement('img');
				img.src = r.url;
				img_c.html('');
				img_c.append(img);
			}
		}
	})
}
$('#alter-kind-btn').click(
		function() {
			var inner = evt.html(alter_kind_html);
			var select = $('.all-good:first')
			var img = $('.kind-img')
			allGood(select);
			var new_name = $('.new-kind-name:first')
			var new_price = $('.new-kind-price:first')
			var new_desc = $('.new-kind-desc:first')
			var new_pic = $('.new-kind-img:first')
			var alter_btn = $('.alter-btn:first')
			var drop_btn = $('.alter-btn:first')
			select.change(function() {
				setKindImg(select);
			})
			alter_btn.click(function() {
				var selected = $('.all-good:first option:selected').val();
				var name = new_name.val() == '' ? goods[selected].name
						: new_name.val();
				var price = new_price.val() == '' ? goods[selected].price
						: new_price.val();
				var desc = new_desc.val() == '' ? goods[selected].desc
						: new_desc.val();
				var data = {
					'action' : 103,
					'goodid' : goods[selected].id,
					'name' : name,
					'price' : price,
					'desc' : desc
				}
				if (new_pic.val() != '') {
					var fd = new FormData();
					var file = new_pic[0].files[0]
					fd.append('file', file);
					$.ajax({
						url : 'upload',
						data : fd,
						type : "post",
						dataType : 'json',
						cache : false,// 上传文件无需缓存
						processData : false,// 用于对data参数进行序列化处理 这里必须false
						contentType : false,
						success : function(r) {
							if (r.result) {
								path = r.path;
								data.url = path;
								$.ajax({
									url : 'settings',
									type : 'post',
									data : data,
									success : function(resp) {
										if (resp.result) {
											alert('修改货物成功');
										} else {
											alert('修改货物失败')
										}
										evt.html('')
									}
								})
							}
						}
					})
				} else {
					$.ajax({
						url : 'settings',
						type : 'post',
						data : data,
						success : function(resp) {
							if (resp.result) {
								alert(resp.out);
							} else {
								alert(resp.out)
							}
							evt.html('')
						}
					})
				}
			})
		})
function good_pic_show() {

}
var pics;
function get_all_picture() {
	var pic_select = $('.pics:first');
	pic_select.change(function() {
		var view = $('.view-pic:first');
		var img = $('<img></img>')[0];
		img.src = pics[this.value].imgUrl;
		view.html(img);
	})
	$.ajax({
		type : 'post',
		url : 'settings',
		data : {
			'action' : 7
		},
		success : function(r) {
			if (r.result) {
				pics = r.pics;
				console.log(pics);
				for (attr in pics) {
					var pic = pics[attr]
					var opt = document.createElement('option');
					opt.value = attr;
					opt.text = pic.imgName
					pic_select.append(opt);
				}
				var cur_good = tml_goods[tml_good.val()];
				var opt_ed = pic_select.find('option[value=' + cur_good.urlId
						+ ']');
				opt_ed.attr('selected', true);
				var view = $('.view-pic:first');
				var img = $('<img></img>')[0];
				img.src = pics[opt_ed.val()]['imgUrl'];
				view.html(img);
			}
		}
	})
}
function drop_btn_function(good){
	alert(good)
}
var alter_good_html = $('.alter-good').html()
$('#alter-good-btn').click(function() {
	evt.html('');
	var tml = getTerminalSelected();
	if (tml == -1) {
		alert('请选择终端操作')
		return;
	}
	evt.html(alter_good_html)
	get_all_picture();
	var drop_btn = $('.drop-good-from-tml:first')
	var alter_btn = $('.alter-good:first')
	var new_price = $('.new-good-price:first')
	var new_stock = $('.new-good-stock:first')
	var new_x = $('.new-x:first')
	var new_y = $('.new-y:first')
	var select = tml_good.find('option:selected').val();
	var good = tml_goods[select]
	var new_good=good;
	var tmlid = getTerminalSelected();
	var gdid = good.id;
	var uuid = tmla[tmlid].terminalUUID;
	drop_btn.click(function(){
		var good_id = good.id;
		var tml_id = $('#tmns option:selected').val();
	})
	alter_btn.click(function() {
		var price = new_price.val() == '' ? good.price : new_price.val();
		var stock = new_stock.val() == '' ? good.stock : new_stock.val();
		var x = new_x.val() == '' ? good.x : new_x.val();
		var y = new_y.val() == '' ? good.y : new_y.val();
		new_good.price = price;
		new_good.stock = stock;
		new_good.x = x;
		new_good.y = y;
		tml_goods[select] = new_good;
		var pic_sel = $('.pics:first');
		var urlid = pics[pic_sel.find('option:selected').val()]['imgId'];
		$.ajax({
			url:'settings',
			type:'post',
			data:{
				'action':8,
				'x':x,
				'y':y,
				'tmlid':tmlid,
				'gdid':gdid,
				'urlid':urlid,
				'stock':stock,
				'price':price,
				'uuid':uuid
			},
			success:function(r){
				if(r.result){
					console.log('ok');
					evt.html('');
				}
			}
		})
	})
})










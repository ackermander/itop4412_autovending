//const protocol = 'http';
//const host = '://192.168.1.101';
//const port = ':8080';
var protocol = 'http';
var host = null;
var port = null;
var dvc = null;

function generatePost(para) {
	var url = protocol + host + port + para;
	return url;
}

function stopCommunicate() {
	communicateTime.clearInterval();
}

function dataPkg(data) {
	return JSON.stringify(data);
}

function getTimeMillisec() {
	return new Date().getTime();
}

var Checker = {
	timeChecker: function(res) {
		var now = getTimeMillisec();
		console.log(now);
		if (Math.abs(now - res) < 1000 * 60 * 1) {
			return true; //表示检测通过
		}
		return false; // 表示检测不通过
	},
	// 如果服务器发送过来的数据中有方法, 则数据无法通过.
	functionChecker: function(resp) {
		try {
			var JSONresp = JSON.parse(resp);
		} catch (e) {
			return true;
		}
		for (value in JSONresp) {
			if (typeof JSONresp[value] == 'function') {
				return false;
			}
		}
		return true;
	},
	functionChkJump: function(resp) {
		var bool = this.functionChecker(resp);
		if (!bool) {
			window.location.href = '/index.html';
		}
	}

}

function communicate(options) {
	var defSetting = {
		event: function(resp) {
			var isfunction = Checker.functionChecker(resp);
			console.log("resp: " + resp);
		},
		url: generatePost('/VendingServer/islive'),
		type: 'get',
		data: {
			"data": dataPkg({
				'version': 2,
				'request-type': 'live-request',
				'terminal-uuid': '',
				'uuid': dvc.uuid,
				'serial': dvc.serial,
				'model': dvc.platform,
				'time': getTimeMillisec()
			})
		},
		error: function(error) {
			console.log(error)
		}
	};
	var config = $.extend(defSetting, options);
	$.ajax({
		url: config.url,
		type: config.type,
		data: config.data,
		success: config.event
	})
}

function liveChkBeforeCommunicate(options) {
	communicate(options);
}

function alertLogTest() {
	console.log('test log');
}

/*通过获得设备唯一识别号来获得该设备的配置, 包括 */
/*货物种类, 货物价格, 余货, 服务器时间等         */

function log(text) {
	console.log(text);
}
var getGoodsRes = {
	getTerminalRes: function(kind) {
		Checker.functionChkJump(kind);
		if (kind == 'all') {
			var data = {};
			data.model = dvc.platform;
			data['request-type'] = 'get-resources';
			data.version = 1;
			data.serial = dvc.serial;
			data.uuid = dvc.uuid;
			data.action = 'getResAction';
			data.time = getTimeMillisec();
			$.ajax({
				url: generatePost('/VendingServer/terminal-setting'),
				type: 'post',
				data: {
					data: dataPkg(data)
				},
				success: function(resp) {
					var r = mainView.router
					r.back();
					getGoodsRes.setInnerHtml(resp);
				}
			});
		} else {
			console.log(kind);
		}
	},
	getTerminalResWrap: function(resp) {
		console.log(resp);
		Checker.functionChkJump(resp);
		var JSONresp = JSON.parse(resp);
		switch (JSONresp.version) {
			case 1:
				if (JSONresp.result == 'pass') {
					getGoodsRes.getTerminalRes('all');
				} else {
					// 失败软件无法打开, 写日志
				}
				break;
		}
	},
	setInnerHtml: function(resp) {
		var JSONresp = JSON.parse(resp);
		var goods = JSONresp.goods;
		$$(".page-content").html('');
		for (var i = 0; i < goods.length; i++) {
			if (goods[i].stock > 0) {
				var template = "<a href='about.html?good=" + JSON.stringify(goods[i]) + "'><div class='card'><div class='card-content card-content-padding'>" +
					"<div><img class='center sliding goodresimg' src='" + generatePost(goods[i].imgUrl) + "'></div>" +
					"<div id='desc'>" + goods[i].name + "</div><div id='desc'>" + goods[i].describe + "</div><div id='desc'>¥ " + goods[i].price + "</div></div></div></a>";
				$$(".page .page-content").append(template);
			}
		}
		return goods;
	}
}
var ctrl4412 = null;
var idof = null;
var connCtrlSer = {
	"socket": null,
	"url": null,
	"protocol": "ws",
	'page': null,
	'good': null,
	'svrstatu': true,
	connect: function() {
		connCtrlSer.url = connCtrlSer.protocol + host + port + '/VendingServer/webcontroller/' + dvc.uuid;
		this.socket = new WebSocket(connCtrlSer.url);
		this.socket.onopen = connCtrlSer.onopen;
		this.socket.onmessage = this.onmessage;
		this.socket.onerror = this.onerror;
		this.socket.onclose = this.onclose;
		return this;
	},
	onopen: function(event) {
		if (idof != null) {
			clearInterval(idof);
			idof = null;
		}
	},
	onclose: function(event) {
		connCtrlSer.svrupding()
		if (idof == null) {
			idof = setInterval(function() {
				connCtrlSer.connect();
				console.log('reconnecting...' + idof)
			}, 1000)

		}
		if (device.platform.indexOf('Android') != -1) {}
	},
	onerror: function(event) {
		connCtrlSer.svrupding()
		if (idof == null) {
			idof = setInterval(function() {
				connCtrlSer.connect();
				console.log('error reconnecting...' + idof)
			}, 1000)
		}
		if (device.platform.indexOf('Android') != -1) {}
	},
	//处理服务器发来的消息
	/*
	{'version':5,'action':'deal','x':1,'y':1}
	*/
	onmessage: function(event) {
		console.log(event.data);
		var data = JSON.parse(event.data);
		switch (data.version) {
			case 5:
				{
					if (data.action == 'deal') {
						var good = connCtrlSer.good;
						console.log('stock: ' + good.stock);
						good.stock = good.stock - 1;
						console.log('stock: ' + good.stock);
						connCtrlSer.good = good;
						connCtrlSer.deal(good);
					}
					break;
				}
			case 6:
				{
					if (data.result) {
						communicate({
							type: 'get',
							event: getGoodsRes.getTerminalResWrap
						});
					} else {
						console.log(data);
						connCtrlSer.svrreg();
					}

					break;
				}
			case 7:
				{

					communicate({
						type: 'get',
						event: getGoodsRes.getTerminalResWrap
					});
					break;
				}

		}
	},
	send: function(text) {
		this.socket.send(text);
	},
	led: function(num, en) {
		ctrl4412.led([num, en], function() {}, function() {});
	},
	serial: function(data) {
		console.log('serial: ' + JSON.stringify(data));
		if (device.platform.indexOf('Android') != -1) {
			ctrl4412.serial([data.y], function() {}, function() {});
		}
	},
	deal: function(data) {
		this.serial(data);
		this.update(data);
	},
	update: function(good) {
		console.log(JSON.stringify(good))
		aboutPage.contentFill(good);
	},
	svrupding: function() {
		var r = mainView.router
		r.load('.about.html')
		$(".page-content").html("");
		var template = "<div class='card'>" +
			"<div class='card-content card-content-padding'>" +
			"<div class='desc'>服务器正在维护请稍后...</div>" +
			"</div>" +
			"</div>";
		$(".page-content").html(template);
	},
	svrupded: function() {
		$(".page-content").html("");
		communicate({
			type: 'get',
			event: getGoodsRes.getTerminalResWrap
		});
	},
	svrreg: function() {
		$(".page-content").html("");
		var template = "<div class='card'>" +
			"<div class='card-content card-content-padding'>" +
			"<div class='desc'>本机还未注册.</div>" +
			"<div class='desc'>uuid: " + dvc.uuid + "</div>" +
			"</div>" +
			"</div>";
		$(".page-content").html(template);
	}
}

var aboutPage = {
	paymentReqUrl: "/VendingServer/get-payment-code",
	contentFill: function(data) {
		var good = data;
		$(".page-content").html("");
		var template = "<div class='card'><div class='card-content card-content-padding'>" +
			"<div><img class='center sliding goodresimg' src='" + generatePost(good.imgUrl) + "'></div>" +
			"<div class='desc'>" + good.name + "</div>" +
			"<div class='desc'>" + good.describe + "</div>" +
			"<div class='desc'>¥ " + good.price + "</div>" +
			"<div class='desc'>剩余 <font class='stock'>" + good.stock + "</font></div>" +
			"<div class='desc'>扫码支付</div>" +
			"<div class='desc codeqr' id=''></div>" +
			"</div></div>";
		$(".page-content").append(template);
		this.getPaymentUrl(good);
	},
	stockUpdate: function(stock) {
		$(".stock").text(stock);
	},
	getPaymentUrl: function(good) {
		communicate({
			url: generatePost(aboutPage.paymentReqUrl),
			data: {
				data: dataPkg({
					'good': good,
					'time': getTimeMillisec(),
					'version': 3,
					'action': 'get-payment-code',
					'uuid': dvc.uuid,
					'model': dvc.platform
				})
			},
			type: 'post',
			event: function(resp) {
				var JSONresp = JSON.parse(resp);
				if (JSONresp.error == "error") {
					window.location.href = '/index.html';
				} else {
					var query = "qr=" + JSONresp.qr +
						"&ex-time=" + JSONresp.create_time +
						"&uuid=" + dvc.uuid +
						"&position=" + JSONresp.position;
					var code = $(".codeqr").qrcode({
						width: 150,
						height: 150,
						correctLevel: 0,
						text: generatePost("/VendingServer/pay?" + query)
					});
				}
			}
		})
	}
}

function firstConfView(f) {
	var container = $("<div class='card'><div class='card-content card-content-padding'></div></div>");
	var _host = $("<div>ip: <input name='ip'/></div>");
	var _port = $("<div>port: <input name='port'/></div>")
	var btn = $("<div><input type='button' value='确认' name='ok'/></div>")
	var ok = btn.find('input')
	ok.click(function() {
		var hostVal = _host.find('input').val();
		var portVal = _port.find('input').val();
		host = '://'+hostVal;
		port = ':'+portVal;
		$(".page-content").html('');
		connCtrlSer.connect();
		var url = 'http://' + hostVal + ':' + portVal;
		var dataObj = new Blob([url], {
			type: 'text/plain'
		});
		writeFile(f, dataObj)
	})
	container.append(_host)
	container.append(_port)
	container.append(btn)
	$(".page-content").html(container);
	console.log('config')
}

function readFile(fileEntry) {
	fileEntry.file(function(file) {
		var reader = new FileReader();
		reader.onloadend = function() {
			console.log("Successful file read: " + this.result);
			if (this.result == '') {
				firstConfView(fileEntry);
			} else {
				var a = document.createElement('a');
				a.href = this.result;
				host = '://'+a.hostname;
				port = ':'+a.port;	
				connCtrlSer.connect();
			}
		};
		reader.readAsText(file);
	}, function() {});
}

function writeFile(fileEntry, dataObj) {
	fileEntry.createWriter(function(fileWriter) {
		fileWriter.onwriteend = function() {
			console.log("Successful file write...");
			readFile(fileEntry);
		};
		fileWriter.onerror = function(e) {
			console.log("Failed file write: " + e.toString());
		};
		if (!dataObj) {
			dataObj = new Blob(['some file data'], {
				type: 'text/plain'
			});
		}
		fileWriter.write(dataObj);
	});
}

function createFile(dirEntry, fileName, isAppend) {
	dirEntry.getFile(fileName, {
		create: true,
		exclusive: false
	}, function(fileEntry) {
		console.log(fileName)
		writeFile(fileEntry, null, isAppend);
	}, function() {});
}
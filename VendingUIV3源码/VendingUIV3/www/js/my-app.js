// Initialize app
var myApp = new Framework7({
	on: {
		pageAfterIn: function(e, page) {
			myApp.alert('in')
		}
	},
	routes: [{
		name: 'about',
		path: '/about/',
		url: './about.html'
	}],
	swipeBackPage:false
});

// If we need to use custom DOM library, let's save it to $$ variable:
var $$ = Dom7;
var mainView = myApp.addView('.view-main', {
	dynamicNavbar: true
});
window.addEventListener('filePluginIsReady', function() {
	console.log('File plugin is ready');
}, false);
$$(document).on('deviceready', function() {
	console.log("Device is ready!");
	console.log(cordova.file)
	dvc = device;
	//dvc.uuid = '61cf96698f5f198a';
	if (device.platform.indexOf('Android') != -1) {
		ctrl4412 = cordova.plugins['cordova-plugin-testpl'];
	}
	//var con = connCtrlSer.connect();
	window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, function(fs) {
		console.log(fs);
		fs.root.getFile("hostconfig", {
			create: true,
			exclusive: false
		}, function(fileEntry) {
			console.log(fileEntry);
			readFile(fileEntry);
		}, function(){});

	}, function(){});
});

// Now we need to run the code that will be executed only for About page.

// Option 1. Using page callback for page (for "about" page in this case) (recommended way):
myApp.onPageInit("index", function(page) {
	// Do something here for "about" page
	console.log('12312341234')
})

// Option 2. Using one 'pageInit' event handler for all pages:
$$(document).on('pageInit', function(e) {
	// Get page data from event data
	var page = e.detail.page;
	if (page.name == 'about') {
		connCtrlSer.page = page;
		connCtrlSer.good = JSON.parse(page.query.good);
		aboutPage.contentFill(connCtrlSer.good);
	} else if (page.name === 'index') {

	}
})

$$(document).on('', function(e) {
	myApp.alert('Here comes About page');
})
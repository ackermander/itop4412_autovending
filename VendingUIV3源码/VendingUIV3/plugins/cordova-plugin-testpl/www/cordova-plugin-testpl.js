var exec = require('cordova/exec');
var acmdTestplug = {
	test: function(arg0, success, error) {
		exec(success, error, 'TestPlugin', 'testaction', [arg0]);
	},
	led: function(arg0, success, error) {
		exec(success, error, 'TestPlugin', 'led', arg0);
	},
	serial: function(arg0, success, error) {
		exec(success, error, 'TestPlugin', 'serial', arg0);
	}
}
module.exports = acmdTestplug;
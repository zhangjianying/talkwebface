var exec = require('cordova/exec');

exports.startFace = function (arg0, success, error) {
	if (!arg0) {
        arg0 = {};
    }
    exec(success, error, 'TalkwebFacePrinter', 'startFace', [arg0]);
};

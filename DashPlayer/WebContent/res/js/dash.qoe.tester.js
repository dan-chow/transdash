var events = dashjs.MediaPlayer.events;

var videoTag = {};
var buttonTag = {};
var player = {};

var videoSource = "http://114.212.84.179:8080/video/result.mpd";

var trueUrl = "http://114.212.84.179:8080/video/{0}";
var proxyUrl = "http://114.212.85.243:8080/DashProxy/proxy?url={0}&trans={1}";
var controlUrl = "http://114.212.85.243:8080/DashProxy/control";

var trans = true;
var testName = "testWithTrans";

function init() {
	(function(open) {
		XMLHttpRequest.prototype.open = function() {
			if(arguments[0] == 'GET'){
				var file = trueUrl.format(arguments[1].substring(arguments[1].lastIndexOf('/') + 1));
				arguments[1] = proxyUrl.format(encodeURIComponent(file), trans);
			}
			open.apply(this, arguments);
		};
	})(XMLHttpRequest.prototype.open);

	if (!String.prototype.format) {
		String.prototype.format = function() {
			var args = arguments;
			return this.replace(/{(\d+)}/g, function(match, number) { 
				return typeof args[number] != 'undefined'
					? args[number]
					: match
				;
			});
		};
	}
}

function startTest() {
	document.getElementById("buttonTag").disabled = true;
	
	startTestPost();
	playAndRecord(1);
}

function startTestPost(){
	var data = {};
	data["testName"] = testName;
	data["op"] = "start";
	var xhr = new XMLHttpRequest();
	xhr.open("POST", controlUrl);
	xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	xhr.send(JSON.stringify(data));
}

function endTestPost(){
	var data = {};
	data["testName"] = testName;
	data["op"] = "end";
	var xhr = new XMLHttpRequest();
	xhr.open("POST", controlUrl);
	xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	xhr.send(JSON.stringify(data));
}

function playAndRecord(count){
	if(count > 10) {
		endTestPost();
		return;
	}

	player = dashjs.MediaPlayer().create();
	player.getDebug().setLogToBrowserConsole(false);
	player.initialize();
	player.setAutoPlay(false);
	player.attachView(document.getElementById("videoTag"));

	var stalls = [];
	var time = performance.now();

	var first = true;
	player.on(events['CAN_PLAY'], function(){
		console.log("CAN_PLAY");

		if(first) {
			var startupDelay = performance.now() - time;
			console.log("startupDelay:" + startupDelay);
			first = false;
		} else {
			stalls.push(performance.now() - time);
		}
	});

	player.on(events['BUFFER_EMPTY'], function(){
		console.log("BUFFER_EMPTY");

		time = performance.now();
	});

	player.on(events['PLAYBACK_ENDED'], function(){
		console.log("PLAYBACK_ENDED");

		var videoQualities = player.getMetricsFor('video').HttpList
						.filter(http => http.type == "MediaSegment")
						.map(http => http.url)
						.map(url => url.replace(/^.*[\\\/]/, ''))
						.map(file => file.split('-')[0])
						.map(Number);
		console.log("videoQualities:" + videoQualities);

		var avgVideoQuality =  mean(videoQualities);
		console.log("avgVideoQuality:" + avgVideoQuality);

		var qualityVariations = videoQualities.slice(1)
							.map((num, idx) => Math.abs(num - videoQualities[idx]));
		console.log("qualityVariations:" + qualityVariations);

		var avgQualityVariations = mean(qualityVariations);
		console.log("avgQualityVariations:" + avgQualityVariations);

		var stallFrequency = stalls.length;
		console.log("stallFrequency:" + stallFrequency);

		var avgStallDuration = 0;
		if(stalls.legth > 0){
			avgStallDuration = mean(stalls);
		}
		console.log("avgStallDuration:" + avgStallDuration);

		uploadStatsPost(videoQualities, stalls);

		player.reset();

		playAndRecord(count + 1);
	});

	player.attachSource(videoSource);
	player.play();
}

function uploadStatsPost(qualities, stalls){
	var data = {};
	data["testName"] = testName;
	data["op"] = "upload";
	data["statistic"] = {};
	data["statistic"]["qualities"] = qualities;
	data["statistic"]["stalls"] = stalls;
	var xhr = new XMLHttpRequest();
	xhr.open("POST", controlUrl.format(testName, 'upload'));
	xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	xhr.send(JSON.stringify(data));
}

function mean(arr) {
	return sum(arr) / arr.length;
}

function sum(arr) {
	return arr.reduce((a,b) => a+b, 0);
}
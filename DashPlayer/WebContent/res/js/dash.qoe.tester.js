var events = dashjs.MediaPlayer.events;

var videoTag = {};
var buttonTag = {};

var player = {};

var videoSource = "http://114.212.84.179:8080/video/result.mpd";
var isAutoPlay = false;

var proxyUrl='http://114.212.85.243:8080/DashProxy/proxy?url='
function init() {

	(function(open) {
		XMLHttpRequest.prototype.open = function() {
			console.log(arguments);
			arguments[1] = proxyUrl + encodeURIComponent(arguments[1]);
			console.log(arguments);
			open.apply(this, arguments);
		};
	})(XMLHttpRequest.prototype.open);


	videoTag = document.getElementById("videoTag");
	buttonTag = document.getElementById("buttonTag");

	player = dashjs.MediaPlayer().create();
	player.getDebug().setLogToBrowserConsole(false);
	player.initialize();
	player.setAutoPlay(isAutoPlay);
	player.attachView(videoTag);
}

function start() {
	buttonTag.disabled = true;

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
	});

	player.attachSource(videoSource);
	player.play();
}

function mean(arr) {
	return sum(arr) / arr.length;
}

function sum(arr) {
	return arr.reduce((a,b) => a+b, 0);
}
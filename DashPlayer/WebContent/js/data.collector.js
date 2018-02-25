var player = {};
var controlBtn = {};

function init(){
	controlBtn = document.getElementById("controlBtn");

	var videoSource = "http://114.212.84.179:8080/video/result.mpd";
	var videoTag = document.querySelector("video");
	var isAutoPlay = false;

	player = dashjs.MediaPlayer().create();
	player.getDebug().setLogToBrowserConsole(false);
	player.initialize(videoTag, videoSource, isAutoPlay);

	console.log("Ready");
}

function start(){
	console.log("Start");
	player.play();
	controlBtn.innerHTML = "Pause";
	controlBtn.onclick = pause;
}

function pause(){
	console.log("Pause");
	player.pause();
	controlBtn.innerHTML = "Continue";
	controlBtn.onclick = start;
}
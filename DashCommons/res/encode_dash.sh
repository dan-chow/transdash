#!/bin/bash

if [ $# -lt 2 ] || ! [ -f $1 ]; then
    echo 'Usage: $0 input output'
    exit 1
fi


input=$1
output=$2

resolution=([1]="426x180" [2]="640x272" [3]="854x362" [4]="1280x544" [5]="1920x816")
bitrate=([1]="400k" [2]="800k" [3]="1200k" [4]="2400k" [5]="4800k")


file_264="$(uuid).264"
x264 --log-level error $input --output $file_264 --fps 24 --preset slow \
	--bitrate 2400 --vbv-maxrate 4800 --vbv-bufsize 9600 \
	--min-keyint 48 --keyint 48 --scenecut 0 --no-scenecut --pass 1
rm x264_2pass.log
rm x264_2pass.log.mbtree


file_mp4="$(uuid).mp4"
MP4Box -add $file_264 -fps 24 $file_mp4
rm $file_264


for i in {1..5}; 
do
	ffmpeg -loglevel error -i $file_mp4 -x264opts 'keyint=48:min-keyint=48:no-scenecut' \
		-s "${resolution[$i]}" -b:v "${bitrate[$i]}" -b:a 128k "${resolution[$i]}.mp4"
done

rm $file_mp4


MP4Box -dash 4000 -frag 4000 -rap -out 'result.mpd' -bs-switching no \
	-segment-name '$RepresentationID$-$Number$$Init=init$' \
	"${resolution[1]}.mp4" "${resolution[2]}.mp4" "${resolution[3]}.mp4" \
	"${resolution[4]}.mp4" "${resolution[5]}.mp4"

for i in {1..5};
do
	rm "${resolution[$i]}.mp4"
done


mv *-init.mp4 "${output}/"
mv *.m4s "${output}/"
mv *.mpd "${output}/"

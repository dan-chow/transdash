#!/bin/bash

if [ $# -lt 3 ] ; then
    echo 'Usage: $0 init input output'
    exit 1
fi

init=$1
input=$2
output=$3

resolution=([1]="426x180" [2]="640x272" [3]="854x362" [4]="1280x544" [5]="1920x816")
bitrate=([1]="400k" [2]="800k" [3]="1200k" [4]="2400k" [5]="4800k")

mergedMp4="$(uuid).mp4"
cat $init > $mergedMp4
cat $input >> $mergedMp4


tmpx264="$(uuid).264"
x264 $mergedMp4 --output $tmpx264 --fps 24 --preset slow \
  --bitrate 2400 --vbv-maxrate 4800 --vbv-bufsize 9600 \
  --min-keyint 48 --keyint 48 --scenecut 0 --no-scenecut --pass 1
# rm $mergedMp4
# rm x264_*


validMp4="$(uuid).mp4"
MP4Box -noprog -quiet -add $tmpx264 -fps 24 $validMp4
# rm $tmpx264


transMp4="$(uuid).mp4"
idx=$(echo $output | grep -Eo '[0-9]*-' | grep -Eo '[0-9]*')


ffmpeg -loglevel panic -i $validMp4 -x264opts 'keyint=48:min-keyint=48:no-scenecut' \
	-s ${resolution[$idx]} -b:v ${bitrate[$idx]} -b:a 128k $transMp4
# rm $validMp4


MP4Box -noprog -quiet -dash 2000 -frag 2000 -rap -out 'result.mpd' -bs-switching no \
	-segment-name '$Init=init$' $transMp4

# rm $transMp4
# rm init.mp4
# rm result.mpd
# mv 1.m4s $output

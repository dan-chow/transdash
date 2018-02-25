#!/bin/bash

if [ $# -lt 1 ] || ! [ -f $1 ]; then
    echo 'Usage: $0 input'
    exit 1
fi


input=$1
file_264='tmp.264'
file_mp4='tmp.mp4'
file_prefix='sample'


x264 $input --output $file_264 --fps 24 --preset slow --bitrate 2400 \
    --vbv-maxrate 4800 --vbv-bufsize 9600 --min-keyint 48 --keyint 48 \
    --scenecut 0 --no-scenecut --pass 1


MP4Box -add $file_264 -fps 24 $file_mp4


ffmpeg -i $file_mp4 -x264opts 'keyint=48:min-keyint=48:no-scenecut' \
    -s 1920x816 -b:v 4800k -b:a 128k "${file_prefix}_1920_816.mp4"

ffmpeg -i $file_mp4 -x264opts 'keyint=48:min-keyint=48:no-scenecut' \
    -s 1280x522 -b:v 2400k -b:a 128k "${file_prefix}_1280_544.mp4"

ffmpeg -i $file_mp4 -x264opts 'keyint=48:min-keyint=48:no-scenecut' \
    -s 854x362 -b:v 1200k -b:a 128k "${file_prefix}_854_362.mp4"

ffmpeg -i $file_mp4 -x264opts 'keyint=48:min-keyint=48:no-scenecut' \
    -s 640x272 -b:v 800k -b:a 128k "${file_prefix}_640_272.mp4"

ffmpeg -i $file_mp4 -x264opts 'keyint=48:min-keyint=48:no-scenecut' \
    -s 426x180 -b:v 400k -b:a 128k "${file_prefix}_426_180.mp4"


MP4Box -dash 4000 -frag 4000 -rap -out 'result.mpd' -segment-name '$RepresentationID$-$Number$' \
    "${file_prefix}_1920_816.mp4#video" "${file_prefix}_1280_544.mp4#video" \
    "${file_prefix}_854_362.mp4#video"  "${file_prefix}_640_272.mp4#video" \
    "${file_prefix}_426_180.mp4#video"


result_folder=$(uuid)
mkdir $result_folder
mv *.mp4 "${result_folder}/"
mv *.m4s "${result_folder}/"
mv *.mpd "${result_folder}/"
rm *.log
rm *.log.mbtree
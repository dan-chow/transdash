#! /bin/bash

curl -d '{"testName":"$1", "op":"start"}' -H "Content-Type: application/json" -X POST http://114.212.81.29:8080/proxy/control
curl -d '{"testName":"$1", "op":"preparecache"}' -H "Content-Type: application/json" -X POST http://114.212.81.29:8080/DashProxy/control

#! /bin/bash

curl -d '{"testName":"testTrans", "op":"start"}' -H "Content-Type: application/json" -X POST http://114.212.81.29:8080/DashProxy/control
curl -d '{"testName":"testTrans", "op":"precache"}' -H "Content-Type: application/json" -X POST http://114.212.81.29:8080/DashProxy/control

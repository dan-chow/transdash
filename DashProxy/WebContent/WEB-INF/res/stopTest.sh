#! /bin/bash

curl -d '{"testName":"$1", "op":"stop"}' -H "Content-Type: application/json" -X POST http://114.212.81.29:8080/DashProxy/control

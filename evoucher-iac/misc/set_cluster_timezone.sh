#!/bin/bash

helm repo add k8tz https://k8tz.github.io/k8tz/
helm install k8tz k8tz/k8tz --set timezone=Asia/Ho_Chi_Minh

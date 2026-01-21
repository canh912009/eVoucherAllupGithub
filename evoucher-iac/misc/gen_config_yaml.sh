#!/bin/bash

kubectl create configmap push-agent-properties --namespace production --from-file=configs/push-agent/application.properties --dry-run=client -o yaml > config-map-push-agent-properties.yaml

kubectl create configmap sms-callback-receive-properties --namespace production --from-file=configs/sms-callback-receive/application.properties --dry-run=client -o yaml > config-map-sms-callback-receive-properties.yaml

kubectl create configmap zalo-callback-receive-properties --namespace production --from-file=configs/zalo-callback-receive/application.properties --dry-run=client -o yaml > config-map-zalo-callback-receive-properties.yaml

kubectl create configmap evoucher-viewer-properties --namespace production --from-file=configs/evoucher-viewer/application.properties --dry-run=client -o yaml > config-map-evoucher-viewer-properties.yaml

kubectl create configmap pos-api-properties --namespace production --from-file=configs/pos-api/application.yaml --dry-run=client -o yaml > config-map-pos-api-properties.yaml

kubectl create configmap frontend-ui-properties --namespace production --from-file=configs/frontend-ui/SystemConfig.js --dry-run=client -o yaml > config-map-frontend-ui-properties.yaml

kubectl create configmap admin-api-properties --namespace production --from-file=configs/admin-api/application.yaml --dry-run=client -o yaml > config-map-admin-api-properties.yaml

kubectl create configmap evoucher-service-be-properties --namespace production --from-file=configs/evoucher-service-be/application.yaml --dry-run=client -o yaml > config-map-evoucher-service-be-properties.yaml

kubectl create configmap publish-service-properties --namespace production --from-file=configs/publish-service/application.yaml --dry-run=client -o yaml > config-map-publish-service-properties.yaml

kubectl create configmap otp-service-properties --namespace production --from-file=configs/otp-service/application.properties --dry-run=client -o yaml > config-map-otp-service-properties.yaml

kubectl create configmap evoucher-api-properties --namespace production --from-file=configs/evoucher-api/application.properties --dry-run=client -o yaml > config-map-evoucher-api-properties.yaml

kubectl create configmap evoucher-service-fe-properties --namespace production --from-file=configs/evoucher-service-fe/application.properties --dry-run=client -o yaml > config-map-evoucher-service-fe-properties.yaml

kubectl create configmap vnpt-epay-dummy-properties --namespace production --from-file=configs/vnpt-epay-dummy/application.yaml --dry-run=client -o yaml > config-map-vnpt-epay-dummy-properties.yaml

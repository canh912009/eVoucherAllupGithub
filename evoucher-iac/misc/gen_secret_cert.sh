#!/bin/bash

kubectl create secret tls admin.urlybud.aqua.gift --namespace production --key ssl-cert/admin.urlybud.aqua.gift/privkey1.pem --cert ssl-cert/admin.urlybud.aqua.gift/fullchain1.pem --dry-run=client -o yaml  > secret-tls-admin-urlybud-aqua-gift.yaml

kubectl create secret tls ev.aqua.gift --namespace production --key ssl-cert/ev.aqua.gift/privkey1.pem --cert ssl-cert/ev.aqua.gift/fullchain1.pem --dry-run=client -o yaml  > secret-tls-ev-aqua-gift.yaml

kubectl create secret tls smsreport.aqua.gift --namespace production --key ssl-cert/smsreport.aqua.gift/privkey1.pem --cert ssl-cert/smsreport.aqua.gift/fullchain1.pem --dry-run=client -o yaml  > secret-tls-smsreport-aqua-gift.yaml

kubectl create secret tls zaloreport.aqua.gift --namespace production --key ssl-cert/zaloreport.aqua.gift/privkey1.pem --cert ssl-cert/zaloreport.aqua.gift/fullchain1.pem --dry-run=client -o yaml  > secret-tls-zaloreport-aqua-gift.yaml

kubectl create secret tls ev.webpos.aqua.gift --namespace production --key ssl-cert/ev.webpos.aqua.gift/privkey1.pem --cert ssl-cert/ev.webpos.aqua.gift/fullchain1.pem --dry-run=client -o yaml  > secret-tls-ev-webpos-aqua-gift.yaml

kubectl create secret tls ev.ui.aqua.gift --namespace production --key ssl-cert/ev.ui.aqua.gift/privkey1.pem --cert ssl-cert/ev.ui.aqua.gift/fullchain1.pem --dry-run=client -o yaml  > secret-tls-ev-ui-aqua-gift.yaml

kubectl create secret tls ev.pos.aqua.gift --namespace production --key ssl-cert/ev.pos.aqua.gift/privkey1.pem --cert ssl-cert/ev.pos.aqua.gift/fullchain1.pem --dry-run=client -o yaml  > secret-tls-ev-pos-aqua-gift.yaml

kubectl create secret tls ev.img.aqua.gift --namespace production --key ssl-cert/ev.img.aqua.gift/privkey1.pem --cert ssl-cert/ev.img.aqua.gift/fullchain1.pem --dry-run=client -o yaml  > secret-tls-ev-img-aqua-gift.yaml

kubectl create secret tls ev.viewer.aqua.gift --namespace production --key ssl-cert/ev.viewer.aqua.gift/privkey1.pem --cert ssl-cert/ev.viewer.aqua.gift/fullchain1.pem --dry-run=client -o yaml  > secret-tls-ev-viewer-aqua-gift.yaml

kubectl create secret tls be.aqua.gift --namespace production --key ssl-cert/be.aqua.gift/privkey1.pem --cert ssl-cert/be.aqua.gift/fullchain1.pem --dry-run=client -o yaml  > secret-tls-be-aqua-gift.yaml
# wildcard certs
kubectl create secret tls aqua.gift --namespace production --key ssl-cert/aqua.gift/unencrypted-key.pem --cert ssl-cert/aqua.gift/cert.pem --dry-run=client -o yaml > secret-tls-aqua-gift.yaml
# new b2b certs


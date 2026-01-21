#!/bin/bash

mvn clean package -Dmaven.test.skip=true -U
docker compose up
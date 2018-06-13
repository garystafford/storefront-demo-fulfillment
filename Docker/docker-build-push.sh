#!/usr/bin/env bash

./gradlew clean build
docker build -f Docker/Dockerfile --no-cache -t garystafford/storefront-fulfillment:latest .
docker push garystafford/storefront-fulfillment:latest

# docker run --name storefront-fulfillment -d garystafford/storefront-fulfillment:latest
#!/usr/bin/env bash

./gradlew clean build
docker build -f Docker/Dockerfile --no-cache -t garystafford/storefront-fulfillment:gke-2.0.0 .
docker push garystafford/storefront-fulfillment:gke-2.0.0

# docker run --name storefront-fulfillment -d garystafford/storefront-fulfillment:gke-2.0.0
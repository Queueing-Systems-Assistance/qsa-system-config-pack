#!/bin/bash

#### Init
cd "${TRAVIS_BUILD_DIR}" || exit

#### Docker Login
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin || exit

#### Docker Build Image
sudo docker build \
  -f ./support/docker/Dockerfile \
  -t docker.pkg.github.com/queueing-systems-assistance/qsa-system-config-pack/qsa-system-config-pack:latest . || exit
sudo docker push docker.pkg.github.com/queueing-systems-assistance/qsa-system-config-pack/qsa-system-config-pack:latest || exit

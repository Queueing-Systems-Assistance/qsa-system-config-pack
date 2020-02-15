#!/bin/bash

#### Init
cd "${TRAVIS_BUILD_DIR}" || exit

#### Build
printf "Build System Config Pack"
./gradlew clean build || exit
echo "[OK]"
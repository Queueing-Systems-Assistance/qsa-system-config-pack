# QSA System Config Pack [![Build Status](https://travis-ci.com/Queueing-Systems-Assistance/qsa-system-config-pack.svg?branch=master)](https://travis-ci.com/Queueing-Systems-Assistance/qsa-system-config-pack)

**QSA System Config Pack** contains the system resources. Each system has a 'base' directory and inside that directory you can find all the system configurations. For each system attribute there is a config file and inside the config file you can find the value for that attribute. 

For those who want to help develop the application:
- [Git workflow](https://github.com/Queueing-Systems-Assistance/qsa-application/docs/git-workflow.md)
- Build the config pack:
  - Use `gradle clean build` to build the project
  - If you want to deploy it into your local repository, issue `gradle clean install`

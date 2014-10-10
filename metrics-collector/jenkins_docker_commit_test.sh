#!/bin/bash

# Build the Docker Image (webapp) from the Dockerfile
docker build -t ${JOB_NAME}-commit ${WORKSPACE}/metrics-collector

# Spawn a Docker container from the Image <webapp> and run the short_test_suite
docker run --name ${JOB_NAME} ${JOB_NAME}-commit sh -c '/var/lib/tomcat7/bin/startup.sh && sleep 20 && /usr/bin/ruby /project/dockertests/short_test_suite.rb'

# Remove the Docker container after test
docker rm ${JOB_NAME}

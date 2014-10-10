#!/bin/bash

# Build the Docker Image (webapp) from the Dockerfile
echo '[script] Build Docker Image for Commit test started...'
docker build -t ${JOB_NAME}-commit ${WORKSPACE}/metrics-collector
echo '[script] Build Docker Image for Commit test completed'

# Spawn a Docker container from the Image <webapp> and run the short_test_suite
echo '[script] Spawn Docker container and run Commit test started...'
docker run --name ${JOB_NAME} ${JOB_NAME}-commit sh -c '/opt/tomcat7/bin/startup.sh && sleep 20 && /usr/bin/ruby /project/dockertests/short_test_suite.rb'
echo '[script] Spawn Docker container and run Commit test completed'

# Remove the Docker container after test
echo '[script] Clean up Docker container started...'
docker rm ${JOB_NAME}
echo '[script] Clean up Docker container completed'

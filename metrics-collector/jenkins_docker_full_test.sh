#!/bin/bash

# Build the Docker Image for Periodic (full) test from the Dockerfile
echo '[script] Build Docker Image for Periodic test started...'
docker build -t ${JOB_NAME} ${WORKSPACE}/metrics-collector
echo '[script] Build Docker Image for Periodic test completed'

# Spawn a Docker container from the Image and run the Full test suite
echo '[script] Spawn Docker container and run All tests started...'
docker run --name ${JOB_NAME} ${JOB_NAME} sh -c '/opt/tomcat7/bin/startup.sh && sleep 20 && /usr/bin/ruby /project/dockertests/short_test_suite.rb ; /usr/bin/ruby /project/dockertests/long_test_suite.rb'
echo '[script] Spawn Docker container and run All tests completed'

# Remove the Docker container after test
echo '[script] Clean up Docker container started...'
docker rm ${JOB_NAME}
echo '[script] Clean up Docker container completed'


#!/bin/bash

# Persisted Docker Image name with the following format: <registry_location>/<jenkins_job_name>:<jenkins_build_number>
# <registry_location> can be: Docker public image registry, or private Docker registry (<registry_host>:<registry_port>)
PERSISTED_IMAGE_NAME="54.153.156.88:5000/${JOB_NAME}:${BUILD_NUMBER}"

# Tag the current periodic build image with the build number
echo '[script] Tag the image with the periodic build number started...'
docker tag ${JOB_NAME} ${PERSISTED_IMAGE_NAME}
echo '[script] Tag the image with the periodic build number completed'

# Push the image to Docker Registry
echo '[script] Push the image to Docker Registry started...'
docker push ${PERSISTED_IMAGE_NAME}
echo '[script] Push the image to Docker Registry completed'


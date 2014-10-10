#!/bin/bash

# Tag the current periodic build image with the build number
echo '[script] Tag the image with the periodic build number started...'
docker tag ${JOB_NAME} ${PERSISTED_IMAGE_NAME}
echo '[script] Tag the image with the periodic build number completed'

# Push the image to Docker Registry
echo '[script] Push the image to Docker Registry started...'
docker push ${PERSISTED_IMAGE_NAME}
echo '[script] Push the image to Docker Registry completed'


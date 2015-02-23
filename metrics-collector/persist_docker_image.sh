# Build Docker Image with version ${BUILD_NUMBER} from Dockerfile in current directory
docker build -t abtran/${JOB_NAME}:${BUILD_NUMBER}

# Push Docker Image to repository
docker push abtran/${JOB_NAME}:${BUILD_NUMBER}

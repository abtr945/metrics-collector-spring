# Set the base image to Ubuntu
FROM ubuntu

# File Author / Maintainer
MAINTAINER An Binh Tran

# Update the repository sources list
RUN apt-get update

# Install required system libraries (Java, Tomcat, Ruby)
RUN apt-get install -y openjdk-7-jdk tomcat7 ruby1.9.3

# Mount the current project workspace to /project in the Docker container
ADD . /project



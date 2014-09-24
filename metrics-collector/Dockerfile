# Set the base image to Ubuntu
FROM ubuntu

# File Author / Maintainer
MAINTAINER An Binh Tran

# Update the repository sources list
RUN apt-get update

# Install required system libraries (Java, Tomcat, Ruby)
RUN apt-get install -y curl openjdk-7-jdk tomcat7 ruby1.9.3

# Mount the current project workspace to /project in the Docker container
ADD . /project

# Stop the Tomcat7 service
RUN service tomcat7 stop

# Copy the .WAR artifact to tomcat deploy
RUN cp /project/target/metrics*.war /var/lib/tomcat7/webapps

# Rename the .WAR file
RUN mv /var/lib/tomcat7/webapps/metrics*.war /var/lib/tomcat7/webapps/metrics.war

# Restart Tomcat7 to deploy the webapp
RUN service tomcat7 start


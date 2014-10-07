# Set the base image to Ubuntu
FROM ubuntu

# File Author / Maintainer
MAINTAINER An Binh Tran

# Update the repository sources list
RUN apt-get update

# Install required system libraries (wget, curl, Java, Tomcat, Ruby)
RUN apt-get install -y wget curl openjdk-7-jdk ruby1.9.3
RUN wget -O /tmp/tomcat.tar.gz http://mirror.olnevhost.net/pub/apache/tomcat/tomcat-7/v7.0.55/bin/apache-tomcat-7.0.55.tar.gz
RUN tar -xzf /tmp/tomcat.tar.gz -C /var/lib
RUN mv /var/lib/apache-tomcat-7.0.55 /var/lib/tomcat7
RUN rm /tmp/tomcat.tar.gz

# Mount the current project workspace to /project in the Docker container
ADD . /project

# Rename the .WAR artifact
RUN cp /project/target/metrics*.war /tmp/metrics.war

# Deploy the webapp to Tomcat
RUN mv /tmp/metrics.war /var/lib/tomcat7/webapps/metrics.war

# Start the Tomcat container (only run when no overrided CMD is supplied during DOCKER RUN)
CMD /var/lib/tomcat7/bin/startup.sh

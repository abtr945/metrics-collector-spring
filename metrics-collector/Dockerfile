# Set the base image to Ubuntu
FROM ubuntu

# File Author / Maintainer
MAINTAINER An Binh Tran

# Update the repository sources list
RUN apt-get update

# Install required system libraries (wget, curl, Java, Tomcat, Ruby, supervisor)
RUN apt-get install -y wget curl openjdk-7-jdk ruby1.9.3
RUN wget -O /tmp/tomcat.tar.gz http://mirror.olnevhost.net/pub/apache/tomcat/tomcat-7/v7.0.55/bin/apache-tomcat-7.0.55.tar.gz
RUN tar -xzf /tmp/tomcat.tar.gz -C /var/lib
RUN mv /var/lib/apache-tomcat-7.0.55 /var/lib/tomcat7
RUN rm /tmp/tomcat.tar.gz
RUN apt-get install -y supervisor

# Mount the current project workspace to /project in the Docker container
ADD . /project

# Rename the .WAR artifact
RUN cp /project/target/metrics*.war /tmp/metrics.war

# Deploy the webapp to Tomcat
RUN mv /tmp/metrics.war /var/lib/tomcat7/webapps/metrics.war

# Expose the Tomcat port 8080
EXPOSE 8080

# Configure supervisor to start Tomcat container
RUN mkdir -p /var/log/supervisor
ADD supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# Start the Tomcat container via supervisor (only run when no overrided CMD is supplied during DOCKER RUN)
CMD supervisord -c /etc/supervisor/conf.d/supervisord.conf

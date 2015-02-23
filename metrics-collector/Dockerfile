# Set the base image to Ubuntu
FROM ubuntu:latest

# Install required libraries and Servlet container (Java, Tomcat)
RUN apt-get -q -y update && apt-get install -y openjdk-7-jre=7u65-2.5.2-4 tomcat7=7.0.55-1

RUN apt-get -q -y update
RUN apt-get install -y openjdk-7-jre tomcat7

# Mount the current project workspace to /project in the Docker container
ADD . /project

# Deploy application artifact to Tomcat webapps directory
RUN cp /project/target/metrics-collector.war /var/lib/tomcat7/webapps/metrics-collector.war

# Expose the Tomcat port 8080 of Docker container to host machine
EXPOSE 8080

# Start the Tomcat servlet container
CMD service tomcat7 start && tail -f /var/lib/tomcat7/logs/catalina.out

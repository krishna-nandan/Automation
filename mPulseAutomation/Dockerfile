# latest image from UBUNTU installed in the machine
FROM ubuntu:latest

MAINTAINER Krishna Nandan <krishnanandan@live.in>

# Update ubuntu system
RUN apt-get update

# Install java version on ubuntu-selenium image
RUN apt-get install -y openjdk-8-jdk
RUN apt-get install -y openjdk-8-jre

# Install phantomjs
RUN apt-get install -y phantomjs

# Install maven on ubuntu-selenium image
RUN apt-get install -y maven

# Install git on ubuntu-selenium image
RUN apt-get install -y git

# Get the repository onto the local system
RUN git clone https://github.com/krishna-nandan/Automation.git

WORKDIR "/Automation/mPulseAutomation"

# Run the maven command to execute all the tests
RUN mvn clean package -Dmaven.test.skip

ENTRYPOINT ["/bin/bash"]

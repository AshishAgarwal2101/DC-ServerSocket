FROM ubuntu:latest
WORKDIR usr/data
RUN apt update
RUN apt install -y openjdk-8-jdk

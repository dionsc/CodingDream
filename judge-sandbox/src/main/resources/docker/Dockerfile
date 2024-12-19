# 指定基础镜像
FROM ubuntu:16.04


COPY judge-sandbox-1.0-SNAPSHOT.jar  /app/

RUN  apt update
RUN  apt install openjdk-8-jdk
RUN  apt install build-essential gdb
RUN  apt install -y g++
RUN  apt clean
RUN  rm -rf /var/lib/apt/lists/*

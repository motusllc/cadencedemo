FROM motus/javabase:master-latest

ADD  build/docker/setenv.sh /usr/local/bin/
ADD  target/*.jar /run.jar
RUN  chmod +x /usr/local/bin/setenv.sh

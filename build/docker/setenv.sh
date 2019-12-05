#!/bin/bash

# Set defaults for log4j if they're not set
if [[ -z "$LOG4J_LEVEL" ]]; then
    LOG4J_LEVEL=INFO
fi

SPRING_PROFILES=default
if [[ -n "$LOGENTRIES_TAG" ]]; then
    CONTAINER=`hostname`
    SPRING_PROFILES=${SPRING_PROFILES},logentries-logging
    JAVA_OPTS="${JAVA_OPTS} -Dlogentries.token=${LOGENTRIES_TAG} -Dlogentries.host=${HOST} -Dlogentries.container=${CONTAINER} -Dlogentries.level=${LOG4J_LEVEL}"
fi

if [[ -z "$CRSINC_DATABASE_USERNAME" ]]; then
    CRSINC_DATABASE_USERNAME=crsinc
fi
if [[ -z "$CRSINC_DATABASE_PASSWORD" ]]; then
    CRSINC_DATABASE_PASSWORD=crsinc
fi

if [[ -z "$ACTIVEMQ_BROKER_URL" ]]; then
    ACTIVEMQ_BROKER_URL="tcp://${ACTIVEMQ_1_PORT_61616_TCP_ADDR}:${ACTIVEMQ_1_PORT_61616_TCP_PORT}"
fi
if [[ -z "$ACTIVEMQ_USER" ]]; then
    ACTIVEMQ_USER=appuser
fi
if [[ -z "$ACTIVEMQ_PASSWORD" ]]; then
    ACTIVEMQ_PASSWORD=motusmqapp
fi


export SPRING_ACTIVEMQ_BROKER_URL="${ACTIVEMQ_BROKER_URL}"
export SPRING_ACTIVEMQ_USER="${ACTIVEMQ_USER}"
export SPRING_ACTIVEMQ_PASSWORD="${ACTIVEMQ_PASSWORD}"

export SPRING_DATASOURCE_URL="jdbc:postgresql://${DB_1_PORT_5432_TCP_ADDR}:${DB_1_PORT_5432_TCP_PORT}/crsinc?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"
export SPRING_DATASOURCE_USERNAME=${CRSINC_DATABASE_USERNAME}
export SPRING_DATASOURCE_PASSWORD=${CRSINC_DATABASE_PASSWORD}

JAVA_OPTS="-Dspring.profiles.active=${SPRING_PROFILES} ${JAVA_OPTS}"

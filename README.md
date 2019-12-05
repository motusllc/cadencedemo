# Welcome to the Cadence Demo!  

To run the demo, please make sure you have the following:
* Docker
* Java
* Maven

## Compiling
To compile the test application, run
```$xslt
mvn clean package
```

## Cadence Server
To run the cadence server, execute the following from the project root: 
```$xslt
docker-compose up
```
This will start the cadence server, cassandra, cadence web interface, and statsd. 

If this is the first time you're running, you need to create a Cadence Domain by running: 
```$xslt
docker run --network=host --rm ubercadence/cli:master --do test-domain domain register -rd 1
```

## Running the demo
To run the various parts of the demo, start each of these in their own terminal window.  

Workflow Process:
```$xslt
 mvn exec:java -Pworkflow
```

Activity Process:
```$xslt
 mvn exec:java -Pactivity
```

Once those have started with no errors, you can start the client: 
```$xslt
 mvn exec:java -Prunner
```

This should start the process, and you should see your workflow executing. 

## Web UI
To see the web UI, go to http://localhost:8088 and choose the domain you created above (test-domain)
### Prerequisites:
1) Java 8 (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
2) Maven (https://maven.apache.org/)
3) MongoDB (https://www.mongodb.com/)

### Building Project
mvn clean install package

### Running Application

1) Make sure you have MongoDB running on localhost on 27017 port 
and database with name "creativity" exits

2) java -jar target/datamanager-1.0-SNAPSHOT.jar

FROM openjdk:8-jre

ADD ./@project.build.finalName@.jar /root/@project.build.finalName@.jar
ENTRYPOINT ["/usr/bin/java", "-jar", "@project.build.finalName@.jar"]

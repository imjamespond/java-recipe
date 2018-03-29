mv ../../AuthServices/dist/client/authService-client.jar ./libs
mv ../../underware/dist/lib/DP-common-0.1.jar ./libs
mvn install:install-file -Dfile=./libs/authService-client.jar -DgroupId=com.metasoft -DartifactId=authService-client -Dversion=0.1 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=./libs/DP-common-0.1.jar -DgroupId=com.metasoft -DartifactId=DP-common -Dversion=0.1 -Dpackaging=jar -DgeneratePom=true

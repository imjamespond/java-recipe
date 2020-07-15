DOCKER_HOST=unix:///var/run/docker.sock mvn clean install docker:build  
DOCKER_HOST=tcp://192.168.0.254:2375 mvn clean install docker:build -X  
docker tag mavendemo 192.168.0.193:59999/mvndemo  
docker login  192.168.0.193:59999 -u abc -p abc  
docker push 192.168.0.193:59999/mvndemo  
docker logout 192.168.0.193:59999  
docker system prune  
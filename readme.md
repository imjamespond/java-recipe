scp target/test-docker-1.0-SNAPSHOT.jar root@192.168.0.193:/tmp
docker tag mavendemo 192.168.0.193:59999/mvndemo  
docker login  192.168.0.193:59999 -u abc -p abc  
docker push 192.168.0.193:59999/mvndemo  
docker logout 192.168.0.193:59999  
docker system prune  
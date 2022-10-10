docker -H tcp://192.168.1.208:2375 rm -f zydb_platform .
docker -H tcp://192.168.1.208:2375 rmi badousoft/zydb_platform
docker -H tcp://192.168.1.208:2375 build -t badousoft/zydb_platform .
docker -H tcp://192.168.1.208:2375 run -d --name zydb_platform --add-host=badouchain.test.com:192.168.1.241 -p 9003:9003/tcp -v /home/server/docker_mapping/zydb:/usr/local/tomcat/attach badousoft/zydb_platform .

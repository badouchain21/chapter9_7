docker -H tcp://192.168.1.208:2375 rm -f zydb_front .
docker -H tcp://192.168.1.208:2375 rmi badousoft/zydb_front
docker -H tcp://192.168.1.208:2375 build -t badousoft/zydb_front .
docker -H tcp://192.168.1.208:2375 run -d --name zydb_front -p 9001:8080/tcp badousoft/zydb_front

date >> /home/logs/log_restart_gateway.log &&
mvn clean package  &&
cd /home/edenhub/edenhub_sys/core/edenhub_gateway/target &&  
java -Djava.security.egd=file:/dev/./urandom -jar edenhub-gateway-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod &  exit 0

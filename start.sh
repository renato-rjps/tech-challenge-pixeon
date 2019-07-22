#!/bin/bash

DEPLOY_IP=$1

if [ -z $1 ]; then 
#	echo >&2 "É necessário informar o ip local da maquina"; exit 1; 
	DEPLOY_IP=192.168.99.100
fi;

"Gerando o build dos projeto"
mvn clean install -DskipTests

echo "Parando os containers"
docker stop techchallengepixeon_pixeon-postgres_1 techchallengepixeon_pixeon-exam-api_1 techchallengepixeon_pixeon-gateway_1 techchallengepixeon_pixeon-discovery_1 techchallengepixeon_pixeon-exam-api-2_1 || true
sleep 10

echo "Removendo os containers"
docker rm techchallengepixeon_pixeon-postgres_1 techchallengepixeon_pixeon-exam-api_1 techchallengepixeon_pixeon-gateway_1  techchallengepixeon_pixeon-discovery_1 techchallengepixeon_pixeon-exam-api-2_1 || true
sleep 10

echo "Revovendo as images"
docker rmi pixeon-exam-api:latest pixeon-gateway:latest pixeon-discovery:latest || true
sleep 10
#docker-compose rm || true

echo "Criando a Base de Dados"
docker-compose up -d pixeon-postgres
sleep 40

echo "Criando o Service Discovery"
docker-compose up -d pixeon-discovery
sleep 40

echo "Iniciando as aplicações"
docker-compose up -d pixeon-gateway pixeon-exam-api pixeon-exam-api-2
sleep 90
echo "###################################"
echo "Aplicações iniciadas com sucesso"
echo "###################################"
sleep 20

exit 0
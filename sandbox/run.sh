# Criar as docker networks
docker network create adm_videos_services || true
docker network create elastic || true

# Criar as pastas com permissões
sudo chown root app/filebeat/filebeat.docker.yml
mkdir -p .docker
chmod 777 .docker
mkdir -p .docker/es01
mkdir 777 .docker/es01
mkdir -p .docker/keycloak
chmod 777 .docker/keycloak
mkdir -p .docker/filebeat
mkdir 777 .docker/filebeat

docker compose -f app/docker-compose.yml up -d
docker compose -f elk/docker-compose.yml up -d
docker compose -f services/docker-compose.yml up -d

echo "Inicializando os containers..."
sleep 20
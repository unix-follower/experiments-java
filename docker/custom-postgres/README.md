# Build image
```bash
docker build -t custom-postgres:18.7 .
```
# Run
```bash
docker network create \
  --driver=bridge \
  --subnet=192.168.2.0/24 \
  --gateway=192.168.2.254 \
  local-bridge

docker-compose up -d
nc -vz 127.0.0.1 5432
```
# Cheat sheet
```bash
docker exec -it experiments_postgres /bin/bash
docker-compose logs > log.log
docker-compose down -v
```
# Install software (psql and other)
## On macOS
```bash
brew install libpq
brew link --force libpq
```
## On Ubuntu
```bash
apt search postgresql-client
sudo apt install -y postgresql-client-common postgresql-client
```

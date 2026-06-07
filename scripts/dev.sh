#!/bin/bash

DOCKER_COMPOSE_NAME="todo-list"
DB_CONTAINER_NAME="db"
DB_CONTAINER="${DOCKER_COMPOSE_NAME}-${DB_CONTAINER_NAME}"


set -e

echo "Starting Database..."
docker compose -f docker/docker-compose.yml up -d 

echo "Waiting for database..."

until [ "$(docker inspect --format='{{.State.Health.Status}}' todo-list-db-1)" = "healthy" ]; do
  sleep 1
done

echo "Database is ready"

sleep 5

echo "Database ready"

echo "Starting Backend"
cd backend
./mvnw spring-boot:run &
BACK_PID=$!

cd ..

echo "Starting Frontend"
cd frontend

if [ ! -d node_modules ]; then
  npm install
fi

npm run dev &
FRONT_PID=$!

cd ..

cleanup() {
  echo ""
  echo "Stopping services..."

  kill $BACK_PID 2>/dev/null || true
  kill $FRONT_PID 2>/dev/null || true

  docker compose -f docker/docker-compose.yml down
}

trap cleanup EXIT INT TERM

echo ""
echo "Backend : http://localhost:8080"
echo "Frontend: http://localhost:5173"
echo "Database: localhost:5432"
echo ""

wait
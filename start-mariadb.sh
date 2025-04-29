
#!/bin/bash

# Container configuration
CONTAINER_NAME="birdbox-mariadb"
DB_ROOT_PASSWORD="rootpw"
DB_NAME="birdbox"
DB_USER="birdbox"
DB_PASSWORD="birdbox"
DB_PORT="3306"

# Stop and remove existing container if it exists
docker stop $CONTAINER_NAME 2>/dev/null
docker rm $CONTAINER_NAME 2>/dev/null

# Start new MariaDB container
docker run --name $CONTAINER_NAME \
    -e MYSQL_ROOT_PASSWORD=$DB_ROOT_PASSWORD \
    -e MYSQL_DATABASE=$DB_NAME \
    -e MYSQL_USER=$DB_USER \
    -e MYSQL_PASSWORD=$DB_PASSWORD \
    -p $DB_PORT:3306 \
    -d mariadb:lts

echo "MariaDB container started. Waiting for database to initialize..."
sleep 5

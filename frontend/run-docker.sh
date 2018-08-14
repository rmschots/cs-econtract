#!/bin/bash

ng build

yes | cp -rf db.js docker-build/db.js
yes | cp -TRv dist/ docker-build/dist/

# build the container
docker build -t cipalschaubroeck/angular-poc:latest ./docker-build
echo 'angular-poc image has been build.';

echo 'starting a container with the angular-poc image on port 9000';
docker run --name angular-poc --rm -p 9000:80 -it cipalschaubroeck/angular-poc:latest

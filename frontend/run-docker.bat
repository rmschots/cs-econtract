CALL ng build

copy db.js docker-build/db.js
xcopy /e /i "./dist" "./docker-build/dist"

# build the container
docker build -t cipalschaubroeck/example-app:latest ./docker-build
echo 'example-app image has been build.';

echo 'starting a container with the example-app image on port 9000';
docker run --name example-app --rm -p 9000:80 -it cipalschaubroeck/example-app:latest

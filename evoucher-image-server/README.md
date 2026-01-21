# Evoucher File Api

Evoucher File Api: File/Image Upload/Download

## Description
- Use: rename file `.env.sample` to `.env` and edit configure yourself
- Docs: [http://localhost:4000/docs](http://localhost:4000/docs)
- Private folder: for upload/download documents
- * Request: [http://localhost:4000/api/attachments](http://localhost:4000/api/attachments) => Response ("path": "/documents/[filename]")
- Public folder: for upload/show images
- * Request: [http://localhost:4000/api/images](http://localhost:4000/api/images) => Response ("path": "/static/images/[filename]")
- * Public link: [http://localhost:4000/static/images/[filename]](http://localhost:4000/static/images/[filename])
- Logs: check logs in folder `logs`

## Installation

```bash
$ yarn install
```

## Running the app

```bash
# development
$ yarn run start

# watch mode
$ yarn run start:dev

# production mode
$ yarn run start:prod
```

## Test

```bash
# unit tests
$ yarn run test

# e2e tests
$ yarn run test:e2e

# test coverage
$ yarn run test:cov
```

## Build

```bash
# build
$ yarn build
```

## Docker Build
To build Docker image, use `Dockerfile`.

Build image
```bash
docker build -t [name]:[tag] .                        # Example: docker build -t evoucher-file:v1.0.0 .
```

## Run Container
```bash
## Production
docker run --name [container_name] -d -p [public_port]:[port] [image]:[tag]                 # docker run --name ev_file -d -p 4000:4000 evoucher-file:v1.0.0

## Mount folder
docker run --name [container_name] -d -p [public_port]:[port] -v [host_path]:[container_path] [image]:[tag]
Ex: docker run --name ev_file -d -p 4000:4000 -v /home/account/E-Voucher/uploads/public:/app/uploads/public -v /home/account/E-Voucher/uploads/private:/app/uploads/private evoucher-file:v1.0.0

## Other configure
docker run -d ...                                                                           # Detached mode, meaning it runs in the background
docker run --rm ...                                                                         # Automatically remove it after it exits
```

## Docker Compose (If not use command docker run ...)
Opt for `docker-compose.yml` to streamline managing multi-container Docker applications, avoiding the verbosity of long docker run commands.
```bash
## Start
docker compose up                                                                           # Run in the background
docker compose -d up

## Stop
docker compose down
```

## Logs
Access container is running and check folder `logs`
```bash
# Check container is running
docker ps                           # docker ps -a

# Show log
docker logs -f [container_id]

# Access a container
docker exec -it [container_id] sh
```
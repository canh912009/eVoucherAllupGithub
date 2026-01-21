# Aqua E-Voucher Admin CMS UI
Aqua E-Voucher Admin CMS UI Project

## Install dependencies
```bash
yarn
```

Development on Localhost
```bash
yarn dev   # http://localhost:3000
```

Build
```bash
yarn build
```

Start
```bash
yarn start
```

## Docker Build
To build Docker image, use `Dockerfile` and `entrypoint.sh`.

Build image
```bash
docker build -t [name]:[tag] .                        # Example: docker build -t evoucher:v1 .
docker build -f Dockerfile_other -t [name]:[tag] .    # Build with other file Dockerfile
```

### Important Note 
If you're using Docker to build in a Unix environment, ensure that the `entrypoint.sh` file is saved with UNIX line endings to prevent Docker run errors like `exec ./entrypoint.sh: no such file or directory`.
Make sure to replace LF line endings with CRLF.

1. Open the file entrypoint.sh in Notepad++.
2. Navigate to Edit => EOL Conversion => Unix (LF) to ensure correct line endings.
3. Do not use Windows (CR LF).

Alternatively, you can run the following command before building Docker:

```bash
sed -i -e 's/\r$//' entrypoint.sh
```

For more details, refer to this [Stack Overflow post](https://stackoverflow.com/questions/14219092/bash-script-bin-bashm-bad-interpreter-no-such-file-or-directory).


## Run Container
```bash
## Production
docker run --name [container_name] -d -p [public_port]:[port] [image]:[tag]                  # docker run --name ev_prod -d -p 3000:3000 evoucher:v1.0.0

## Other configure
docker run -d ...                                                                           # Detached mode, meaning it runs in the background
docker run --rm ...                                                                         # Automatically remove it after it exits

## Other environment
docker run --name [container_name] --env-file [file_env] -d -p [public_port]:[port] [image]:[tag]

## Example
docker run --name ev_v1.0.0_prod -d -p 3000:3000 evoucher:v1.0.0
docker run --name ev_v1.0.0_stag --env-file .env.staging -d -p 3001:3000 evoucher:v1.0.0
```

## Logs
```bash
# Check container is running
docker ps                           # docker ps -a

# Show log
docker logs -f [container_id]

# Access a container
docker exec -it [container_id] sh
```
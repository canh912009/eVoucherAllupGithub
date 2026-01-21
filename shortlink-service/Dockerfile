FROM --platform=linux/amd64 ubuntu:latest
RUN mkdir -p /root/urlybud/urlybud
COPY . /root/urlybud/urlybud/
WORKDIR /root/urlybud/urlybud
EXPOSE 80 2000
CMD ["sh", "-c", "./urlybud3"]

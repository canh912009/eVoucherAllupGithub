192.168.105.87 Ubuntu 20.04.6 LTS

aqua / aqua

sudo su

aqua

git clone https://git.altimedia.vn/evoucher/shortlink-service.git

pipeline-at / Alti@1234

cd shortlink-service

docker build -t registry.git.altimedia.vn/evoucher/shortlink-service:master_`` `date +%Y%m%d` `` .

docker login registry.git.altimedia.vn

docker push registry.git.altimedia.vn/evoucher/shortlink-service:master_`` `date +%Y%m%d` ``
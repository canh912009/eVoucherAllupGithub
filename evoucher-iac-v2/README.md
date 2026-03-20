# eVoucher IAC v2 - Kubernetes Manifests

Thư mục chứa Kubernetes manifests cho hệ thống eVoucher, được tổ chức theo mô hình base/overlay với comment giải thích chi tiết bằng tiếng Việt.

## Cấu trúc thư mục

```
evoucher-iac-v2/
├── README.md                                          # File hướng dẫn này
├── base/                                              # Manifests dùng chung (không phụ thuộc môi trường)
│   ├── monitoring/                                    # Công cụ giám sát hệ thống
│   │   ├── namespace.yaml                             # Namespace "monitoring"
│   │   ├── prometheus/                                # Thu thập và lưu trữ metrics
│   │   │   ├── prometheus-role.yaml                   # RBAC cho Prometheus
│   │   │   ├── prometheus-configmap.yaml              # Cấu hình scrape targets
│   │   │   ├── prometheus-deployment.yaml             # Deployment Prometheus server
│   │   │   ├── prometheus-service.yaml                # Service ClusterIP port 9090
│   │   │   └── prometheus-node-exporter.yaml          # DaemonSet + Service node metrics
│   │   ├── grafana/                                   # Dashboard visualization
│   │   │   ├── grafana-deployment.yaml                # Deployment Grafana server
│   │   │   ├── grafana-service.yaml                   # Service ClusterIP port 3000
│   │   │   └── grafana-datasource-configmap.yaml      # Auto-provisioning Prometheus datasource
│   │   └── fluentd/                                   # Thu thập và chuyển tiếp log
│   │       ├── fluentd-rbac.yaml                      # ServiceAccount + ClusterRole + Binding
│   │       ├── fluentd-configmap.yaml                 # Cấu hình pipeline log
│   │       └── fluentd-ds.yaml                        # DaemonSet thu thập log mỗi node
│   ├── ingress-nginx/                                 # NGINX Ingress Controller
│   │   └── ingress-nginx-deployment.yaml              # Full deployment (RBAC, Service, DaemonSet, Webhook)
│   └── services/                                      # Application services
│       ├── cms-ui/                                    # Giao diện quản trị CMS
│       │   ├── deployment.yaml                        # Deployment CMS UI
│       │   └── service.yaml                           # Service ClusterIP port 80
│       ├── admin-api/                                 # Backend API cho CMS
│       │   ├── deployment.yaml                        # Deployment Spring Boot (port 8080)
│       │   └── service.yaml                           # Service ClusterIP 80 → 8080
│       └── evoucher-service-be/                       # Core voucher processing engine
│           ├── deployment.yaml                        # Deployment Spring Boot (port 8083)
│           └── service.yaml                           # Service ClusterIP 80 → 8083
└── (overlays/)                                        # TODO: Tạo overlays cho dev/prod
    ├── (dev/)                                         # Namespace: dev, domain: *.aqua.altimedia.vn
    └── (prod/)                                        # Namespace: production, domain: *.aqua.gift, b2b.aquavoucher.com
```

## Cách deploy

### Bước 1: Apply base resources (monitoring + ingress)

```bash
# Tạo namespace monitoring
kubectl apply -f base/monitoring/namespace.yaml

# Deploy Prometheus (RBAC → ConfigMap → Deployment → Service → Node Exporter)
kubectl apply -f base/monitoring/prometheus/

# Deploy Grafana (Datasource ConfigMap → Deployment → Service)
kubectl apply -f base/monitoring/grafana/

# Deploy Fluentd (RBAC → ConfigMap → DaemonSet)
kubectl apply -f base/monitoring/fluentd/

# Deploy NGINX Ingress Controller
kubectl apply -f base/ingress-nginx/
```

### Bước 2: Apply application services

```bash
# Deploy CMS UI
kubectl apply -f base/services/cms-ui/

# Deploy Admin API
kubectl apply -f base/services/admin-api/

# Deploy eVoucher Service BE
kubectl apply -f base/services/evoucher-service-be/
```

### Bước 3: Apply overlay theo môi trường (khi có overlays)

```bash
# Dev environment
kubectl apply -k overlays/dev/

# Production environment
kubectl apply -k overlays/prod/
```

## Danh sách services và ports

| Service               | Container Port | Service Port | Service Name                            | Mô tả                          |
|-----------------------|----------------|--------------|-----------------------------------------|---------------------------------|
| CMS UI                | 80             | 80           | admin-cms-ui-cluster-ip-service         | Giao diện quản trị CMS         |
| Admin API             | 8080           | 80           | admin-api-cluster-ip-service            | Backend API cho CMS             |
| eVoucher Service BE   | 8083           | 80           | evoucher-service-be-cluster-ip-service  | Core voucher processing         |
| Prometheus            | 9090           | 9090         | prometheus-service                      | Metrics server                  |
| Grafana               | 3000           | 3000         | grafana-cluster-ip-service              | Dashboard visualization         |
| Node Exporter         | 9100           | 9100         | node-exporter                           | Node system metrics             |
| Ingress Controller    | 80/443         | 80/443       | ingress-nginx-controller                | HTTP/HTTPS traffic routing      |

## Monitoring Stack

### Prometheus
- **UI**: Truy cập qua Ingress hoặc port-forward: `kubectl port-forward svc/prometheus-service -n monitoring 9090:9090`
- **Retention**: Dữ liệu metrics được giữ 35 ngày
- **Targets**: Node Exporter (cluster + external), API Server, kubelet, kube-state-metrics, cAdvisor

### Grafana
- **UI**: Truy cập qua Ingress hoặc port-forward: `kubectl port-forward svc/grafana-cluster-ip-service -n monitoring 3000:3000`
- **Default login**: admin/admin (nên đổi ngay sau khi deploy)
- **Datasource**: Prometheus đã được auto-provisioning

### Fluentd
- **Output**: Elasticsearch tại `192.168.23.104:19200`
- **Index pattern**: `fluentd-YYYY.MM.DD` (dùng trong Kibana)

## Domains theo môi trường

| Môi trường | Namespace    | Domain                                    |
|------------|-------------|-------------------------------------------|
| Dev        | dev         | `*.aqua.altimedia.vn`                     |
| Production | production  | `*.aqua.gift`, `b2b.aquavoucher.com`      |

## Lưu ý quan trọng

1. **Storage**: Prometheus và Grafana hiện dùng `emptyDir` — dữ liệu sẽ mất khi pod restart. Cho production, nên thay bằng PersistentVolumeClaim (PVC).
2. **Secrets**: Grafana admin password đang hardcode — nên dùng Kubernetes Secret cho production.
3. **Image tags**: Các service đang dùng `latest` tag — nên dùng specific version tag cho production.
4. **Resources**: Đã thêm resource requests/limits cho tất cả containers. Điều chỉnh theo workload thực tế.

# SpringKube
Spring WebFlux + Reactive Mongo + Kubernetes + Windows 11

# Complete Guide to Deploying the Project

## Prerequisites

Ensure you have the following tools installed:

- **Java 21 (JDK 21)**
- **Docker Desktop**
- **Minikube**
- **kubectl**

---

## Table of Contents

1. [Step 1: Start Minikube](#step-1-start-minikube)
2. [Step 2: Configure Docker to Use Minikube's Docker Daemon](#step-2-configure-docker-to-use-minikubes-docker-daemon)
3. [Step 3: Build the Docker Image Inside Minikube](#step-3-build-the-docker-image-inside-minikube)
4. [Step 4: Apply Kubernetes Manifests](#step-4-apply-kubernetes-manifests)
   - [4.1 Apply ConfigMap for NGINX Proxy](#41-apply-configmap-for-nginx-proxy)
   - [4.2 Apply Deployment and Service for NGINX Proxy](#42-apply-deployment-and-service-for-nginx-proxy)
   - [4.3 Apply Deployment and Service for MongoDB](#43-apply-deployment-and-service-for-mongodb)
   - [4.4 Apply Deployment and Services for the Application](#44-apply-deployment-and-services-for-the-application)
   - [4.5 Apply Ingress Resource (if using)](#45-apply-ingress-resource-if-using)
5. [Step 5: Check the Status of Pods and Services](#step-5-check-the-status-of-pods-and-services)
   - [5.1 Check Pods](#51-check-pods)
   - [5.2 Check Services](#52-check-services)
6. [Step 6: Set Up Port Forwarding to Access the Application](#step-6-set-up-port-forwarding-to-access-the-application)
7. [Step 7: Interact with the Application](#step-7-interact-with-the-application)
   - [7.1 Sending a GET Request](#71-sending-a-get-request)
   - [7.2 Sending a POST Request](#72-sending-a-post-request)
8. [Additional Commands](#additional-commands)
   - [Check Pod Logs](#check-pod-logs)
   - [Describe a Pod for Detailed Information](#describe-a-pod-for-detailed-information)
   - [Delete Kubernetes Resources](#delete-kubernetes-resources)
9. [Completion](#completion)
   - [Switch Back to the Local Docker Daemon](#switch-back-to-the-local-docker-daemon)
   - [Stop Minikube](#stop-minikube)
---

## Step 1: Start Minikube

Open PowerShell **as Administrator** and start Minikube:

```powershell
minikube start
```

## Step 2: Configure Docker to Use Minikube's Docker Daemon
In the same PowerShell window, run:

```powershell
minikube docker-env --shell powershell | Invoke-Expression
```
This command configures Docker to use the Docker daemon inside Minikube.

## Step 3: Build the Docker Image Inside Minikube
Navigate to the root directory of your project (where your Dockerfile is located) and run:

```powershell
docker build -t demo-app:latest .
```

## Step 4: Apply Kubernetes Manifests
### 4.1 Apply ConfigMap for NGINX Proxy

kubectl apply -f k8s/proxy-configmap.yaml

### 4.2 Apply Deployment and Service for NGINX Proxy

```powershell
kubectl apply -f k8s/proxy-deployment.yaml
```

### 4.3 Apply Deployment and Service for MongoDB

```powershell
kubectl apply -f k8s/mongodb-deployment.yaml
```

### 4.4 Apply Deployment and Services for the Application

```powershell
kubectl apply -f k8s/app-deployment.yaml
```

### 4.5 Apply Ingress Resource (if using)

```powershell
kubectl apply -f k8s/app-ingress.yaml
```

Note: If you are using a separate NGINX proxy and not configuring Ingress, you can skip this step.

## Step 5: Check the Status of Pods and Services
### 5.1 Check Pods

```powershell
kubectl get pods
```

### 5.2 Check Services 

```powershell
kubectl get services
```

Ensure that the following services are running:

```
method-proxy-service
demo-app-reader-service
demo-app-writer-service
mongodb-service
```

## Step 6: Set Up Port Forwarding to Access the Application
To access your application from Postman or a browser, run:

```powershell
kubectl port-forward service/method-proxy-service 8080:80
```

Now your application is accessible at http://localhost:8080.

Note: Leave this PowerShell window open while you are working with the application.

## Step 7: Interact with the Application
### 7.1 Sending a GET Request

You can use a browser or Postman to send a GET request:
URL: http://localhost:8080/products

###7.2 Sending a POST Request

Use Postman or another tool to send a POST request:
* URL: http://localhost:8080/products
* Headers: Content-Type: application/json
* Body: 
  ```json
  {
  "name": "Product from Postman",
  "price": 50.0
  }
  ```

## Additional Commands
### Check Pod Logs
If you need to check the application logs:

```powershell
kubectl logs <pod-name>
```
### Describe a Pod for Detailed Information

```powershell
kubectl describe pod <pod-name>
```

### Delete Kubernetes Resources
If you need to delete all resources and start over:

```powershell
kubectl delete -f k8s/proxy-configmap.yaml
kubectl delete -f k8s/proxy-deployment.yaml
kubectl delete -f k8s/mongodb-deployment.yaml
kubectl delete -f k8s/app-deployment.yaml
kubectl delete -f k8s/app-ingress.yaml
```

## Completion
### Switch Back to the Local Docker Daemon
After finishing with Minikube, you can switch Docker back to the local daemon:

```powershell
minikube docker-env --shell powershell --unset | Invoke-Expression
```

### Stop Minikube

```powershell
minikube stop
```

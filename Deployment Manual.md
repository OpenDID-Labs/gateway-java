### Gateway Deployment Guide

- ## **Introduction**

  This document is a guide to install, configure and run the `opendid-gateway-java`. 

  > As a clear demonstration, all commands in this document are run with root permission. These commands can also be run under normal user permissions, please set the file storage and configure the parameters properly.

  ## 1. Environmental requirements

  ### 1.1 **Hardware Requirements**

  It is recommended to build the `opendid-gateway-java` on Ubuntu Server (Version 22.04 LTS or higher) with the following requirements:

  #### **Minimum Requirements:**

  - 2 CPU
  - Memory: 4GB
  - Disk: 25GB SSD
  - Bandwidth: 20Mbps

  #### **Recommended Requirements:**

  - 4 CPU
  - Memory: 8GB
  - Disk: 50GB SSD
  - Bandwidth: 20Mbps

  ### 1.2 **Prerequisites**

  | Software       | Version  |
  | -------------- | -------- |
  | MySQL          | 8.0+     |
  | Nacos          | 2.4.3    |
  | OpenssL        | 3.0.2+   |
  | Docker-ce      | 20.10.0+ |
  | Docker-compose | 1.25.5+  |

  # 2. **Download and Configuration**

1. Download [gateway-java.zip](https://github.com/OpenDID-Labs/gateway-java/releases/) file.

```
gateway-java/
├── 1-init.sh
├── 2-install-docker.sh
├── 3-install-mysql.sh
├── 4-install_nacos.sh
├── 5-install-web2gateway.sh
├── conf
│   ├── containerd.service
│   ├── docker.service
│   ├── docker.socket
│   ├── my.cnf
│   ├── web2gateway_nacos_config.zip
│   └── vnmapping.yaml
├── install.conf
├── packages
│   ├── docker
│   │   ├── containerd
│   │   ├── containerd-shim-runc-v2
│   │   ├── ctr
│   │   ├── docker
│   │   ├── docker-init
│   │   ├── docker-proxy
│   │   ├── dockerd
│   │   └── runc
│   ├── docker-24.0.7.tar.gz
│   ├── docker-compose-linux-x86_64
│   └── gateway-java.jar
└── sql
    └── standalone-nacos-mysql.sql
```



2. **Configure Settings**

   Edit the install.conf configuration file, configure the basic installation directory, MySQL and nacos connection information.

```bash
# ========================================================================================
# Web2Gateway Service Configuration File
# ----------------------------------------------------------------------------------------
# Description: This configuration file is for Web2Gateway service basic settings
# 1. If using installation scripts to deploy MySQL and Nacos, these values will be used for service initialization
# 2. If using existing MySQL and Nacos services, please fill in the actual connection information
# ========================================================================================

# Base installation path for Web2Gateway service
# Used to specify the installation directory for service components
INSTALL_BASE_PATH=/data

# ----------------------------------------------------------------------------------------
# MySQL Configuration
# Description: MySQL database connection settings
# - Using installation script: These values will be used for MySQL initialization
# - Using existing MySQL: Fill in your existing MySQL server connection details
# ----------------------------------------------------------------------------------------
MYSQL_HOST="127.0.0.1"                      # Enter the MySQL server address
MYSQL_PORT="3306"                           # Enter the MySQL server port
MYSQL_USER=                                 # Enter the username for MySQL
MYSQL_PASSWORD=                             # Enter the password for MySQL

# ----------------------------------------------------------------------------------------
# Nacos Configuration
# Description: Nacos service connection settings
# - Using installation script: These values will be used for Nacos initialization
# - Using existing Nacos: Fill in your existing Nacos server connection details
# ----------------------------------------------------------------------------------------
NACOS_HOST="127.0.0.1"                      # Enter the Nacos server address
NACOS_PORT="8848"                           # Enter the Nacos service port
NACOS_USER=                                 # Enter the username for Nacos
NACOS_PASSWORD=                             # Enter the password for Nacos
NACOS_NAMESPACE=                            # Enter the Nacos namespace name
NACOS_NAMESPACE_ID=                         # Enter the Nacos namespace ID
```



3. **Execute Installation Scripts**

   Run the following scripts in order:

```bash
# System initialization
bash 1-init.sh

# Docker installation
bash 2-install-docker.sh

# MySQL installation (Optional)
bash 3-install-mysql.sh

# Nacos installation (Optional)
bash 4-install_nacos.sh

# Web2Gateway service installation
bash 5-install-web2gateway.sh
```



#### Verification

After installation, verify the service's status. **When the service is first started, you can obtain the corresponding JWT Token by checking the field containing Root Token in the web2gateway service logs.**


```
#docker ps
CONTAINER ID   IMAGE                       COMMAND                  CREATED          STATUS          PORTS                                                                                  NAMES
32e7ab55863a   openjdk:17                  "sh /data/web2gatewa…"   13 minutes ago   Up 13 minutes                                                                                          web2gateway
ecacb0630e9f   nacos/nacos-server:v2.4.3   "sh bin/docker-start…"   17 minutes ago   Up 17 minutes   0.0.0.0:8848->8848/tcp, :::8848->8848/tcp, 0.0.0.0:9848->9848/tcp, :::9848->9848/tcp   nacos
73347e29da79   mysql:8.0.36                "docker-entrypoint.s…"   25 minutes ago   Up 25 minutes   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp, 33060/tcp                                   mysql
```


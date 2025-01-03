## **Introduction**

This document is a guide to install, configure and run the `opendid-gateway-java`. 

> As a clear demonstration, all commands in this document are run with root permission. These commands can also be run under normal user permissions, please set the file storage and configure the parameters properly.

## 1. Environmental requirements

### 1.1 **Hardware Requirements**

It is recommended to build the `opendid-gateway-java` on Linux Server with the following requirements:

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

| Software | Version |
| -------- | ------- |
| Java     | 17      |
| MySQL    | 8.0+    |
| nacos    | 2.4.3   |

## 2. **Download and Configuration**

### **2.1** **Installing** **MySQL** **and** **Nacos**

Install MySQL 8.0+ and Nacos 2.4.3, and create a new client gateway library in the MySQL database:

```SQL
CREATE DATABASE client_gateway DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
```

### **2.2 Downloading Configuration Files**

Download `[gateway-java-1.0.0.zip](https://github.com/OpenDID-Labs/gateway-java/releases/download/v1.0.0/gateway-java-1.0.0.zip "gateway-java-1.0.0.zip")` file, which contains `web2gateway_nacos_config.zip`, `gateway-java.jar`, `lib.zip` and `vnmapping.yaml` files.

### 2.3 **Configuration**

Log in to the Nacos management page, create a new namespace named web2gateway, and import the `web2gateway_nacos_config.zip` file included in `gateway-java-1.0.zip`.

Modify the `common.yaml` configuration file, Fill in the public and private keys of secp256k1 and Ed25519 algorithms.

```YAML
service-key:
  privatekey: "0x"    #Enter the private key of secp256k1 algorithm after the 0x prefix

wallet:
  privatekey: "0x"    #Enter the ed25519 algorithm private key corresponding to your APTOS mainnet wallet address after the 0x prefix
```

Generate Service-Key public and private keys based on secp256k1 algorithm:

```Bash
openssl ecparam -name secp256k1 -genkey -noout -out private_key.pem
openssl ec -in private_key.pem -pubout -out public_key.pem
openssl ec -in private_key.pem -text -noout | tr -d ':'
```

Modify the parameters for linking MySQL libraries in `jdbcdruid.yaml`:

```YAML
mysql:
  master:
    url: jdbc:mysql://xxx.xxx.xxx.xxx:3306/client_gateway?characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
    username:     #Enter the username for MySQL
    password:     #Enter the password for MySQL
```

## 3. **Starting the Service**

### 3.1 Requirements

Make sure Java 17 or later version has been installed in your system.

### 3.2 **Starting by Package**

Put `gateway-java.jar`, `lib.zip` and `vnmapping.yaml` files included in `gateway-java-1.0.0.zip` into the same directory and run the command below:

```Bash
#step1:
unzip lib.zip

#step2:
nohup java \
-XX:MetaspaceSize=256m \
-XX:MaxMetaspaceSize=256m \
-Dlogging.file.path \           #Enter the log storage path
-Dspring.cloud.nacos.discovery.server-addr= \       #Enter the host's IP address and port for Nacos
-Dspring.cloud.nacos.discovery.username= \          #Enter the username for Nacos
-Dspring.cloud.nacos.discovery.password= \          #Enter the password for Nacos
-Dspring.cloud.nacos.discovery.namespace= \         #Enter the ID of the Nacos's namespace
-Dvn-mapping-file-path=vnmapping.yaml \             #Enter the absolute path of the vnmapping.yaml file
-jar -Xms4000m -Xmx4000m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./oomdumpfile/heap.hprof gateway-java.jar \
 > nohup.out  2>&1 &
```

You can also check the process from the log:

```Plain
tail -f nohup.out
```

To stop the service in `nohup` mode, please refer to the below command:

```Bash
# Check the PID of the web2gateway service
ps -ef | grep java

# Stop the service, change "PID" to the correct number
kill -9 PID
```

# Gateway Deployment Guide

---

## Introduction

This document is a guide to install, configure and run the `opendid-gateway-java`.

> As a clear demonstration, all commands in this document are run with root permission. These commands can also be run under normal user permissions, please set the file storage and configure the parameters properly.

---

## 1. Environmental Requirements

### 1.1 Hardware Requirements

It is recommended to build the `opendid-gateway-java` on Ubuntu Server (Version 22.04 LTS or higher) with the following requirements:

#### Minimum Requirements:

* 2 CPU
* Memory: 4GB
* Disk: 25GB SSD
* Bandwidth: 20Mbps

#### Recommended Requirements:

* 4 CPU
* Memory: 8GB
* Disk: 50GB SSD
* Bandwidth: 20Mbps

### 1.2 Prerequisites

| Software       | Version  |
| :------------- | :------- |
| MySQL          | 8.0+     |
| Nacos          | 2.4.3    |
| OpenssL        | 3.0.2+   |
| Docker-ce      | 20.10.0+ |
| Docker-compose | 1.25.5+  |

---

## 2. Download and Configuration

1. **Download the latest version of the [gateway-java.zip](https://github.com/OpenDID-Labs/gateway-java/releases/) file and upload it to the server. Then, navigate to the directory on the server where the `gateway-java.zip` file is located and unzip it. This will create a folder named `gateway-java` in the current directory.**

   ```bash
   unzip gateway-java.zip
   ```

   After decompression, you'll see the following directory structure:

   ```
   gateway-java/
   ├── 1-init.sh
   ├── 2-install-docker.sh
   ├── 3-install-mysql.sh
   ├── 4-install_nacos.sh
   ├── 5-install-web2gateway.sh
   ├── conf
   │   ├── containerd.service
   │   ├── docker.service
   │   ├── docker.socket
   │   ├── my.cnf
   │   ├── web2gateway_nacos_config.zip
   │   └── vnmapping.yaml
   ├── install.conf
   ├── packages
   │   ├── docker-24.0.7.tar.gz
   │   ├── docker-compose-linux-x86_64
   │   ├── jasypt-1.9.3.jar
   │   └── gateway-java.jar
   └── sql
       └── standalone-nacos-mysql.sql
   ```

2. **Configure Settings**

   Now, you need to navigate into the `gateway-java` directory that you just unzipped. All installation scripts and configuration files are located here.

   ```bash
   cd gateway-java
   ```

   This is the most important step! You need to edit the `install.conf` file, which contains the base installation path, MySQL connection details, Nacos service settings, and Jasypt encryption parameters. Please read the comments carefully and fill in the information according to your actual situation.

   ```bash
   # ========================================================================================
   # Web2Gateway Service Configuration File
   # ----------------------------------------------------------------------------------------
   # Description: This configuration file is used for the core settings of the Web2Gateway service.
   # 1. If you use our installation scripts to deploy MySQL and Nacos services, these configurations will be used for their deployment.
   # 2. If you already have running MySQL and Nacos services, please fill in the actual connection information for the servers where they are located.
   # ========================================================================================
   
   # Base installation path for Web2Gateway service
   # Used to specify the installation directory for all Web2Gateway services and components. The default is /data, but you can also customize the installation path.
   INSTALL_BASE_PATH=/data
   
   # ----------------------------------------------------------------------------------------
   # MySQL Configuration
   # Description: MySQL database connection settings
   # - If there is no current MySQL service and you need to use this script to deploy the MySQL service: please fill in the IP address of the local server. The port number and password can remain at their default settings or be customized.
   # - If you are using an existing MySQL service: please fill in the IP address, port number, username, and password of the existing MySQL server.
   # ----------------------------------------------------------------------------------------
   MYSQL_HOST="127.0.0.1"               # Please enter the IP address of the local server or the existing MySQL server's IP address within the quotes.
   MYSQL_PORT="3306"                    # Please enter the MySQL service port number within the quotes. Keep the default if not necessary.
   MYSQL_USER="root"                    # Please enter the MySQL username within the quotes, e.g., root.
   MYSQL_PASSWORD="your_mysql_Password" # Please enter the MySQL password within the quotes. It can remain default or be customized.
   
   # ----------------------------------------------------------------------------------------
   # Nacos Configuration
   # Description: Nacos service connection settings
   # - If there is no current Nacos service and you need to use this script to deploy the Nacos service: please fill in the IP address of the local server. The port number and password can remain at their default settings or be customized.
   # - If you are using an existing Nacos service: please fill in the IP address, port number, username, password, and NAMESPACE ID of the existing Nacos server.
   # ----------------------------------------------------------------------------------------
   NACOS_HOST="127.0.0.1"               # Please enter the IP address of the local server or the existing Nacos server's IP address within the quotes.
   NACOS_PORT="8848"                    # Please enter the Nacos service port number within the quotes. Keep the default if not necessary.
   NACOS_USER="nacos"                   # Please enter the Nacos username within the quotes.
   NACOS_PASSWORD="nacos"               # For first-time Nacos deployment, keep "nacos". Change after initial setup via Nacos UI if desired.
   NACOS_NAMESPACE="web2gateway_prod"   # Please enter the Nacos namespace name within the quotes. It can remain default or be customized.
   NACOS_NAMESPACE_ID="web2gateway-id"  # Please enter the Nacos namespace ID within the quotes. It can remain default or be customized.
   
   # ----------------------------------------------------------------------------------------
   # Jasypt Configuration
   # Description: Jasypt encryption settings for sensitive configurations. This password is used by the installation scripts
   # to encrypt sensitive values (like database passwords and private keys) before storing them in Nacos.
   # It is also used by the Web2Gateway application at runtime to decrypt these values.
   # Ensure this password is strong and kept confidential.
   # ----------------------------------------------------------------------------------------
   JASYPT_PASSWORD="your_jasypt_encryption_password" # Jasypt encryption password
   ```

3. **Execute Installation Scripts**

   Now, we'll run the installation scripts in sequence. Before executing the following commands, ensure you're still in the `gateway-java` directory.

   ```bash
   pwd
   # Should display /path/to/gateway-java
   ```

   Run the following commands line by line in the `gateway-java` directory to start deploying the relevant components and services:

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

4. **Verification**

After installation, verify the service's status. **When the service is first started, you can obtain the corresponding JWT Token by checking the field containing Root Token in the web2gateway service logs.**

```bash
docker ps
# Execute the docker ps command to view the current service status
CONTAINER ID   IMAGE                       COMMAND                  CREATED        STATUS        PORTS                                                                                  NAMES
551cf84b8a05   nacos/nacos-server:v2.4.3   "sh bin/docker-start…"   2 hours ago    Up 2 hours    0.0.0.0:8848->8848/tcp, :::8848->8848/tcp, 0.0.0.0:9848->9848/tcp, :::9848->9848/tcp   nacos
899108816a0c   openjdk:17                  "sh /data/web2gatewa…"   19 hours ago   Up 19 hours                                                                                          web2gateway
5677be8768ac   mysql:8.0.36                "docker-entrypoint.s…"   19 hours ago   Up 19 hours   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp, 33060/tcp                                   mysql

# View web2gateway logs
docker logs web2gateway

# Retrieve JWT Token from the logs
docker logs -f web2gateway | grep "Root Token"
```

### Jasypt Encryption and Decryption for Nacos Configurations
-----

This section provides instructions on how to manually encrypt and decrypt sensitive configuration values (like database passwords or private keys) that are stored in Nacos, using the Jasypt tool. This is crucial if you need to modify encrypted values directly or troubleshoot configurations.

The `4-install_nacos.sh` script utilizes a Jasypt JAR file located within the `gateway-java` directory to perform encryption.

#### Understanding Jasypt Encryption in Nacos

Nacos stores certain sensitive configurations in an encrypted format (e.g., `ENC(encrypted_value)`). This encryption relies on a secret password, which is configured in your `install.conf` as `JASYPT_PASSWORD`. This same password is used by the Web2Gateway application at runtime to decrypt these values.

#### How to Encrypt a String

To encrypt a new sensitive string (e.g., a password), you'll use the `JasyptPBEStringEncryptionCLI` tool, which is executed via a temporary Docker container to ensure a consistent environment.

1.  Locate the Jasypt JAR:
    The Jasypt JAR file (`jasypt-1.9.3.jar` or similar version) is typically found in the `packages` directory within your `gateway-java` directory. For example: `~/gateway-java/packages/jasypt-1.9.3.jar`.

2.  Get Your Jasypt Password:
    Retrieve the `JASYPT_PASSWORD` from your `install.conf` file. This is the encryption password used by the system.

3.  Execute the Encryption Command:
    Use the following Docker command. Important: Replace `YOUR_STRING_TO_ENCRYPT` with the actual value you want to encrypt, and manually replace `YOUR_JASYPT_PASSWORD` with the exact Jasypt password from your `install.conf`.

    ```bash
    # First, navigate to the `gateway-java` directory
    cd ~/gateway-java # or your actual deployment path, e.g., /data/gateway-java

    # Execute the encryption command
    docker run --rm \
      -v "$(pwd)/packages/jasypt-1.9.3.jar:/tmp/jasypt-1.9.3.jar" \
      openjdk:17 \
      java -cp "/tmp/jasypt-1.9.3.jar" org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI \
      input="YOUR_STRING_TO_ENCRYPT" password="YOUR_JASYPT_PASSWORD" \
      algorithm="PBEWITHHMACSHA512ANDAES_256" \
      ivGeneratorClassName="org.jasypt.iv.RandomIvGenerator" \
      saltGeneratorClassName="org.jasypt.salt.RandomSaltGenerator" \
      keyObtentionIterations="1000"
    ```

    Example:
    If your `JASYPT_PASSWORD` is `myStrongJasyptPass` and you want to encrypt `myNewMySQLPass`:

    ```bash
    docker run --rm \
      -v "$(pwd)/packages/jasypt-1.9.3.jar:/tmp/jasypt-1.9.3.jar" \
      openjdk:17 \
      java -cp "/tmp/jasypt-1.9.3.jar" org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI \
      input="myNewMySQLPass" password="myStrongJasyptPass" \
      algorithm="PBEWITHHMACSHA512ANDAES_256" \
      ivGeneratorClassName="org.jasypt.iv.RandomIvGenerator" \
      saltGeneratorClassName="org.jasypt.salt.RandomSaltGenerator" \
      keyObtentionIterations="1000"
    ```

    The output will contain the encrypted string, which will appear on the line immediately following ----OUTPUT----------------------. Extract this value.

4.  Update Nacos Configuration:
    Once you have the encrypted string, update the corresponding configuration in Nacos (via the Nacos UI or API) by prefixing it with `ENC()`.
    For example, if the encrypted output is `AbCdEfG123...`, you would set the Nacos configuration value to `ENC(AbCdEfG123...)`.

#### How to Decrypt an Encrypted String

To decrypt an existing encrypted value from Nacos (e.g., `ENC(AbCdEfG123...)`), you can use a similar process.

1.  Extract Encrypted Value:
    Take the part inside `ENC()` from your Nacos configuration. For example, if the Nacos config is `ENC(AbCdEfG123...)`, the encrypted value is `AbCdEfG123...`.

2.  Get Your Jasypt Password:
    Again, retrieve the `JASYPT_PASSWORD` from your `install.conf`. This must be the exact same password used for encryption.

3.  Execute the Decryption Command:
    Use the following Docker command. Important: Replace `YOUR_ENCRYPTED_STRING` with the encrypted value you extracted, and manually replace `YOUR_JASYPT_PASSWORD` with the exact Jasypt password from your `install.conf`.

    ```bash
    # First, navigate to the `gateway-java` directory
    cd ~/gateway-java # or your actual deployment path, e.g., /data/gateway-java

    # Execute the decryption command
    docker run --rm \
    -v "$(pwd)/packages/jasypt-1.9.3.jar:/tmp/jasypt-1.9.3.jar" \
    openjdk:17 \
    java -Djasypt.debug=true -cp "/tmp/jasypt-1.9.3.jar" org.jasypt.intf.cli.JasyptPBEStringDecryptionCLI \
    input="YOUR_ENCRYPTED_STRING" password="YOUR_JASYPT_PASSWORD" \
    algorithm="PBEWITHHMACSHA512ANDAES_256" \
    ivGeneratorClassName="org.jasypt.iv.RandomIvGenerator" \
    saltGeneratorClassName="org.jasypt.salt.RandomSaltGenerator" \
    keyObtentionIterations="1000"
    ```

    The output will contain the decrypted (original) string.

# Prerequisites for the workshop

## Software on your machine

### Bash terminal

* Linux or Mac - just terminal
* Windows - one of:
  * GitBash
  * Windows Subsystem for Linux (WSL)
  
### Git

* Linux: install using the package manager from your Linux distribution
* Mac: install using Brew
* Windows - GitBash (WSL - same as on Linux)

```shell script
git --version
```

### JDK 8 or later

* OpenJDK of the latest version is preferred
* Make sure you have `javac` in your $PATH

```shell script
javac -version
```

### Maven 3.6.0 or later

* Make sure you have `mvn` in your $PATH

```shell script
mvn --version
```

### IntelliJ IDEA

* Download for your OS
* Enable Scala plugin

## Configuration on the Hadoop cluster edge node

Edge node - the node that has the access to YARN, HDFS and other Hadoop cluster resources.
Typically, you submit a Spark or Flink application from an edge node.

### Spark

Make sure you have `spark2-submit` in your $PATH

```shell script
spark2-submit --version
```

### Hadoop configuration

Make sure the `HADOOP_CONF_DIR` environment variable
points to the directory with Hadoop configuration files:
```shell script
echo $HADOOP_CONF_DIR
```
The output should look like `/etc/hadoop/conf`.
Consult with the administrator of your Hadoop cluster for the correct path.
Declare `HADOOP_CONF_DIR` in your `.bashrc` if you don't have it yet:

```shell script
export HADOOP_CONF_DIR="/etc/hadoop/conf"
```

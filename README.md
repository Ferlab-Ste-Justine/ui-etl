Univers Informationnel ETL
=========================

This project contains code to run etl pipeline for Univers informartionnel project.

For now, it includes only a hl7 parser.  This parser takes as input argument :
 - a path which contains some hl7 text files : this path can be any path supported by hadoop (local FS, HDFS, S3, ..) 
 - a path where to write output files
 
 Output files format is avro for now, but it could change later for another format (delta)
 The parser also saved metadata into Hive metastore. For now it will produce only one table :
 - `ui.adt`  
 
# Requirement

 - JDK > 1.8 installed
 - sbt > 1.0 installed
 - Docker and docker-compose installed (for hive and minio)
 
 # How to build
 
 ```sbt assembly```
 
That will produce a fat jar file (a java archive that will contains code of the project, but also code for runtime dependencies).
This fat jar is located at `target/scala-2.12/ui-etl.jar`

# How to run it

1) Install Hive and MINIO
Univers informationnel will use Hive as a metastore, and an object store compatible with S3 API. 
Although this is not necessary, it's recommended to install both locally: 
- Clone this project : https://github.com/arempter/hive-metastore-docker
- Open a terminal, and start containers : `docker-compose up`
- You can access to minio interface with a browser : [http://localhost:9000](http://localhost:9000)
- Create 2 new buckets, 1 for spark, the other  one for univers informationnel (univers-informationel)

2) Install spark
Download [Spark](https://mirror.its.dal.ca/apache/spark/spark-3.0.1/spark-3.0.1-bin-hadoop3.2.tgz) (>3.0.0 with hadoop 3.2) and unarchive it
Configure spark to use Hive as a metastore and minio as an object store : 
- Edit file `conf/spark-defaults.conf`, add these lines :
```
spark.hadoop.hive.metastore.uris    thrift://localhost:9083
spark.sql.warehouse.dir s3a://spark/wharehouse
spark.hadoop.mapreduce.outputcommitter.factory.scheme.s3a       org.apache.hadoop.fs.s3a.commit.S3ACommitterFactory
spark.hadoop.fs.s3a.committer.name      directory
spark.hadoop.fs.s3a.committer.staging.tmp.path  /tmp/spark_staging
spark.hadoop.fs.s3a.buffer.dir  /tmp/spark_local_buf
spark.hadoop.fs.s3a.committer.staging.conflict-mode     fail
spark.hadoop.fs.s3a.impl        org.apache.hadoop.fs.s3a.S3AFileSystem
spark.hadoop.fs.s3a.access.key  accesskey
spark.hadoop.fs.s3a.secret.key  secretkey
spark.hadoop.fs.s3a.endpoint    http://localhost:9000
spark.hadoop.fs.s3a.connection.ssl.enabled      false
spark.hadoop.fs.s3a.path.style.access   true
spark.jars.packages org.apache.spark:spark-avro_2.12:3.0.1,org.apache.hadoop:hadoop-aws:3.2.0,com.amazonaws:aws-java-sdk-bundle:1.11.375
spark.sql.catalogImplementation hive
```

3) Test the setup 
Test your setup starting spark-sql tool : 
- Open a terminal and in `SPARK_HOME` directory run `bin/spark-sql`
- Create a new database for univers informationnel : `create database ui`
- Use ui database : `use ui`
- Create test table in `ui` database :  
```
CREATE TABLE test USING csv LOCATION 's3a://univers-informationel/db/test' AS (SELECT 1 as id union SELECT 2 as id);
```
- Verify test table `select * from test;` or `describe table test;`
- You can also verify than files in minio have been created under `ui/db/test` directory
 
4) Running application :
- Copy some hl7 into MINIO (using the UI) under directory `univers-informationel/hl7`
- Open a terminal and in `SPARK_HOME` directory, run :
```
bin/spark-submit --class bio.ferlab.ui.etl.ParseHL7 ~/workspace/ui-etl/target/scala-2.12/ui-etl.jar s3a://univers-informationel/hl7 s3a://univers-informationel/db/
```
Dont forget to change the path `~/workspace/ui-etl/target/scala-2.12/ui-etl.jar`

5) Verify data :
- Open a terminal and in `SPARK_HOME` directory run `bin/spark-sql`
- Use ui database : `use ui`
- Verify ADT table `select * from adt`
- You can also verify than files in minio have been created under `ui/adt` directory

 
   

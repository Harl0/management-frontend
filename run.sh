#!/bin/sh
aws s3 --region eu-west-2 cp s3://basappconfigbucket/$ENVIRONMENT/$MSNAME.conf .
java -Xms128m -Xmx128m -XX:+UseG1GC -Dhttp.port=8080 -Dhttp.address=0.0.0.0 -Dconfig.file=$MSNAME.conf -jar /opt/$MSNAME.jar

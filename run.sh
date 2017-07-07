#!/bin/sh
wget https://s3.eu-west-2.amazonaws.com/basappconfigbucket/$ENVIRONMENT/$MSNAME.conf
java -Dhttp.port=8080 -Dconfig.file=$MSNAME.conf -jar /opt/$MSNAME.jar

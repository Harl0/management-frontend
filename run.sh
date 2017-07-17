#!/bin/sh
aws s3 --region eu-west-2 cp s3://basappconfigbucket/$ENVIRONMENT/$MSNAME.conf .
java $JAVA_ARGS -jar /opt/$MSNAME.jar

#!/bin/sh

lein ring uberwar package.war
scp package.war root@fettemama.org:/var/lib/tomcat6/webapps/ROOT.war
rm package.war

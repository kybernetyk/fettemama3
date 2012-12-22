#!/bin/sh

lein ring uberwar package.war
scp target/package.war root@fettemama.org:/var/lib/tomcat6/webapps/ROOT.war
rm target/package.war

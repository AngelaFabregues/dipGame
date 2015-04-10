#!/bin/sh
  BASEDIR=`dirname $0`
  cd $BASEDIR
  exec java -jar gameManager-1.0.jar & > /dev/null 2>&1

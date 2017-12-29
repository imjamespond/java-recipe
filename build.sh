#!/bin/bash

GRADLE_HOME=/Users/zyy/Documents/tools/gradle-4.2
export PATH=$PATH:$GRADLE_HOME/bin
export GRADLE_USER_HOME=/Users/zyy/Documents/workspace/gradle-home

if [ "$1" = "gradle" ]; then
  PARAMS=${2:-'build'}
  gradle $PARAMS
elif [ "$1" = "install" ]; then
  exit
fi
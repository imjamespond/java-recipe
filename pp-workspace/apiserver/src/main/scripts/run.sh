#!/bin/sh

class="com.pengpeng.stargame.ApiServer"

# ----------------------------------------------------------------------------
#                   DONOT EDIT WITHOUT AUTHORIZED !!!
#                          (honghong.chen)
# ----------------------------------------------------------------------------

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

PRGDIR=`dirname "$PRG"`
BASEDIR=`cd "$PRGDIR/.." >/dev/null; pwd`


if [ -z "$JAVA_HOME" ] ; then
        echo "ERROR: JAVA_HOME is not set !!!"
        exit 1
fi

# If a specific java binary isn't specified search for the standard 'java' binary
if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD=`which java`
  fi
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly." 1>&2
  echo "  We cannot execute $JAVACMD" 1>&2
  exit 1
fi

if [ -z "$REPO" ]
then
  REPO="$BASEDIR"/lib
fi

CLASSPATH=$CLASSPATH_PREFIX:"$BASEDIR"/classes:"$REPO"/*


#--------------------------------------------------------
# Parse options
#--------------------------------------------------------

OPTS=`getopt -o h -l help -l id: -l rmi: -l conf: -l home: -l jargs: -- "$@"`;

if [ $? != 0 ]
then
    exit 1
fi

eval set -- "$OPTS"

while true ; do
    case "$1" in
        -h|--help)
                        exit 0
                ;;
                --conf)
                        conf=$2
                        shift 2;
                ;;
        --home)
                        home=$2
                        shift 2;
                ;;
                --rmi)
                        rmi=$2
                        shift 2;
                ;;
                --id)
                        id=$2
                        shift 2;
                ;;
                --jargs)
                        jargs=$2
                        shift 2;
                ;;
        --)
                        shift;
                        break;
                ;;
    esac
done

if [ -z "$jargs" ] ; then
        jargs="-Xms128m -Xmx512m -Xss256m"
fi

if [ -z $conf ] || [ -z $rmi ] || [ -z $home ] ; then
        echo "Can not start server, required options: --conf, --home, --rmi"
        echo "Failed!"
        exit 1
fi

logfile="${home}/stdout"
pidfile="${home}/${id}.pid"
nohup $JAVACMD $JAVA_OPTS ${jargs} -Dserver.properties=${conf} -Dserver.port=${rmi} -Dserver.home="${home}" \
  -classpath "$CLASSPATH" \
  -Dapp.name="run" \
  -Dapp.pid="$$" \
  -Dapp.repo="$REPO" \
  -Dapp.home="$BASEDIR" \
  -Dbasedir="$BASEDIR" \
  ${class} \
  "$@" > $logfile 2>&1 &

#save PID to file
pid=$!
cat /dev/null > $pidfile
echo ${pid} >> $pidfile
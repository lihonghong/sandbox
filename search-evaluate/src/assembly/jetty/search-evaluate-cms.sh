#!/bin/bash
start() {
WAR=${project.build.finalName}.${packaging}
RUNLOG=${log.home}/${service.name}/jetty.log
REQUESTLOG=${log.home}/${service.name}/access.yyyy_mm_dd.log
JAVA_CMD=java
WEBDIR=${project.build.finalName}

PROFILE=${spring.profile}
echo $PROFILE
if [ $PROFILE == "online" ]; then
    export JAVA_HOME="/usr/java/jdk1.7.0_25"
    export CLASSPATH=".:/usr/java/jdk1.7.0_25/jre/lib:/usr/java/jdk1.7.0_25/lib:/usr/java/jdk1.7.0_25/lib/tools.jar:/usr/java/jdk1.7.0_25/lib/dt.jar"
    JAVA_CMD=$JAVA_HOME/bin/java
fi

rm -rf $WEBDIR
unzip $WAR -d $WEBDIR

CMD="nohup $JAVA_CMD
    -Djsse.enableSNIExtension=false
    -Djava.security.krb5.conf=/etc/krb5-hadoop.conf
    -Dhadoop.property.hadoop.security.authentication=kerberos
    -Dhadoop.property.hadoop.client.keytab.file=/etc/h_sns.keytab
    -Dhadoop.property.hadoop.client.kerberos.principal=h_sns@XIAOMI.HADOOP
    -jar jetty-runner.jar
    --config jetty.xml
    --log $REQUESTLOG
    $WEBDIR
    >> $RUNLOG
    2>&1
    &
"
echo $CMD
eval $CMD
echo $! > currentpid
}

stop() {
# Getting the PID of the process
PID=`ps -ef | grep -P 'java.*search-evaluate' | grep -v grep | awk '{print $2}'`

# Number of seconds to wait before using "kill -9"
WAIT_SECONDS=10

echo 'process id, ' $PID

# Counter to keep count of how many seconds have passed
count=0

while kill $PID > /dev/null
do
    # Wait for one second
    sleep 1
    # Increment the second counter
    ((count++))

    # Has the process been killed? If so, exit the loop.
    if ! ps -p $PID > /dev/null ; then
        break
    fi

    # Have we exceeded $WAIT_SECONDS? If so, kill the process with "kill -9"
    # and exit the loop
    if [ $count -gt $WAIT_SECONDS ]; then
        kill -9 $PID
        break
    fi
done
echo "done."
}

restart(){
    stop
    start
}

# See how we were called.
case "$1" in
  start)
    start
    ;;
  stop)
    stop
    ;;
  restart)
    restart
    ;;
  *)
    echo $"Usage: $0 {start|stop|restart}"
    exit 2
esac

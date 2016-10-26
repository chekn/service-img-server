#!/bin/bash
act=$1
redis_path=/usr/local/redis/redis-3.0.7
redis_pid=`ps aux | grep [r]edis-server | awk '{print $2}'`

if [ "$act" == "start" ]
then
        if [ "$redis_pid" == "" ]
        then
                ${redis_path}/src/redis-server ${redis_path}/redis.conf &
                echo "do start finish"
        else
                echo "has start, pid: "$redis_pid
        fi
elif [ "$act" == "stop" ]
then
        if [ "$redis_pid" != "" ]
        then
                echo "do your stop, pid: "$redis_pid
                kill -9 $redis_pid
        else
                echo "has stop"
        fi
elif [ "$act" == "state" ]
then
        if [ "$redis_pid" == "" ]
        then
                echo "is not start"
        else
                echo "running , pid: "$redis_pid
        fi
else
        echo "don't know 's param"
fi

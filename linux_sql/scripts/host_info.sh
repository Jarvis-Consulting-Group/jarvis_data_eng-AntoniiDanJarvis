#!/bin/bash

export PGHOST=$1;
export PGPORT=$2;
export PGUSER=$3;
export PGPASSWORD=$4;
export PGDATABASE=host_agent;

hostname=\'$(hostname -f)\'
cpu_number=$(lscpu | grep "^CPU(s):" | awk '{print$2}')
cpu_architecture=\'$(lscpu | grep "^Architecture" | awk '{print$2}')\'
cpu_model=\'$(lscpu | grep "^Model name" | awk '{print$3" "$4" "$5}')\'
cpu_mhz=$(lscpu | grep "^CPU MHz" | awk '{print$3}')
l2_cache=$(lscpu | grep "^L2 cache" | awk '{print substr($3, 1, length($3)-1)}')
timestamp=\'$(date "+%Y-%m-%d %T")\'
total_mem=$(grep MemTotal /proc/meminfo | awk '{print$2}')

query="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, timestamp, total_mem)
                  VALUES ( $hostname, $cpu_number, $cpu_architecture, $cpu_model, $cpu_mhz, $l2_cache, $timestamp, $total_mem);"

psql -c "$query"

exit $?
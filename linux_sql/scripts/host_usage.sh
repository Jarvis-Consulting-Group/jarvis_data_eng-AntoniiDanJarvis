#!/bin/bash

export PGHOST=$1;
export PGPORT=$2;
export PGUSER=$3;
export PGPASSWORD=$4;
export PGDATABASE=host_agent;

timestamp=\'$(date "+%Y-%m-%d %T")\'
memory_free=$(grep MemFree /proc/meminfo | awk '{print$2}')
cpu_idle=$(vmstat | tail -n 1 | awk '{print $15}')
cpu_kernel=$(vmstat | tail -n 1 | awk '{print $13}')
disk_io=$(vmstat -d | tail -n 1 | awk '{print$10}')
disk_available=$(df -m --total | grep total | awk '{print$4}')

hostname=\'$(hostname -f)\'
get_host_id_query="SELECT id FROM host_info WHERE hostname=$hostname";
host_id=$(psql -c "$get_host_id_query" | tail -n 3 | head -n 1 | tr -d "[:blank:]")

insert_query="INSERT INTO host_usage (timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
                  VALUES ($timestamp, $host_id, $memory_free, $cpu_idle, $cpu_kernel, $disk_io, $disk_available);"

psql -c "$insert_query"
exit $?
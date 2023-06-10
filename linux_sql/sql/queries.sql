--Group hosts by hardware info
select cpu_number, id as host_id, total_mem from host_info
order by cpu_number, total_mem desc;

--Average memory usage
select id, host_info.hostname,
(date_trunc('hour', host_usage."timestamp") + date_part('minute', host_usage."timestamp"):: int / 5 * interval '5 min'),
avg(total_mem - memory_free) as used_memory
from host_info inner join host_usage on host_info.id = host_usage.host_id
group by host_info.id, host_usage."timestamp";


--Detect host failure
select host_id,
(date_trunc('hour', host_usage."timestamp") + date_part('minute', host_usage."timestamp"):: int / 5 * interval '5 min') as ts, count(*) as num_data_points
from host_usage group by host_id, (date_trunc('hour', host_usage."timestamp") + date_part('minute', host_usage."timestamp"):: int / 5 * interval '5 min');
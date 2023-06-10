#! /bin/sh

action=$1
db_username=$2
db_password=$3

sudo systemctl status docker || sudo systemctl start docker
container_name=psql
docker inspect ${container_name}
is_cont_exist=$?
case $action in
  create)
        if [ -z $db_username ]
        then
          echo "username is not given"
          exit 1;
        fi

        if [ -z $db_password ]
        then
          echo "password is not given"
          exit 1;
        fi

        if [ $is_cont_exist = 0 ]
        then
          echo "container is already created"
          exit 1;
        fi
        docker volume create pgdata
        docker run --name ${container_name} \
         -e POSTGRES_PASSWORD=${db_password} -e POSTGRES_HOST_AUTH_METHOD=md5 \
         -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:9.6-alpine
        exit $?
        ;;

  start|stop)
        if [ $is_cont_exist = 1 ]
        then
          echo "container is not created"
          exit 1;
        fi
        docker $action $container_name
        exit $?
        ;;

  *)
        echo 'Illegal command'
        echo 'Commands: start|stop|create'
        exit 1
        ;;
esac
#!/bin/bash

mkdir rabbitmq-internal rabbitmq-internal/data rabbitmq-internal/logs # creates directories to access RabbitMQ data
chmod -R 777 rabbitmq-internal/logs # gives necessary rights to RabbitMQ to write logs

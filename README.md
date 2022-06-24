# SD-2022-hwproj

<img src="hwproj-logo.png" align="right"  alt="hwproj logo"/>

[![Run tests](https://github.com/GlebSolovev/SD-2022-hwproj/actions/workflows/ci-validate.yml/badge.svg)](https://github.com/GlebSolovev/SD-2022-hwproj/actions/workflows/ci-validate.yml)

## Краткое описание

_Software Design HW: client server web app for interaction between students and teachers._

**[Ссылка на документацию](https://glebsolovev.github.io/SD-2022-hwproj/)**

## Состояние разработки

На данный момент в проекте:

- реализован основной функционал
- настроен CI: запуск тестов и линтеров, сборка и публикация документации
- `ARCHITECTURE.md` содержит подробное описание архитектуры
- задокументирован и покрыт интеграционными тестами

## Запуск и использование приложения

### Сборка приложения

Прежде всего необходимо собрать проект локально с помощью Gradle:

```bash
./gradlew shadowJar
```

### Запуск сервера, брокера и исполнителей

Для запуска всего проекта достаточно воспользоваться оркестратором в корне репозитория:

```bash
docker-compose --compatibility up
```

Сервер, брокер и заданное число исполнителей будут запущены в соответствующих docker-контейнерах.

_**Дополнительно: настройка исполнителей.**_ Число исполнителей для проверки решений студентов легко настроить в
файле `docker-compose.yml` в поле `services -> runner -> deploy -> replicas`. Среду выполнения (т. е. доступные
приложения и глобальные переменные) исполнителей можно настроить в файле `dockerfiles/runner.Dockerfile` по аналогии с
установленным там `bash`-ом.

_**Замечание: конфликт нескольких RabbitMQ.**_ В случае, если на локальной машине уже запущен сервер `RabbitMQ`, то
запуск проекта не пройдет успешно (так как необходимый порт окажется занятым). Соответственно, потребуется приостановить
исполнение локального `RabbitMQ`.

### Использование приложения

Теперь все готово. Чтобы попасть в веб-интерфейс приложения, достаточно перейти по
ссылке: http://localhost:8080/. Также можно взаимодействовать по `REST API`, описание endpoint-ов ниже.

_**Замечание для учителя.**_ В качестве программы-чекера должен быть загружен bash-скрипт, принимающий ссылку
на решение первым аргументом. При этом ожидается выполнение инварианта: решение верное <=> скрипт завершается с нулевым
кодом возврата.<br/>

Пример всегда успешного чекера см. в файле `examples/check.sh`.

_**Дополнительно: управление брокером.**_
Чтобы отслеживать работу брокера сообщений и управлять ей (при необходимости), можно перейти по
ссылке http://localhost:15672/ и зайти под логином и паролем `guest` и `guest`
соответственно.

### Список REST endpoint-ов

С префиксом `/api/`:

* GET `/assignments`: получить список всех заданий
* POST `/assignments`: добавить новое задание
* GET `/assignments/{id}`: получить детали задания под номером `id`
* GET `/submissions`: получить список всех решений
* POST `/submissions`: отправить новое решение
* GET `/submissions/{id}`: получить детали решения под номером `id`

### Остановка приложения

При необходимости остановить исполнение приложения, необходимо сперва послать соответствующий сигнал в сессии терминала,
из которой был произведен запуск: т. е. по умолчанию `Ctrl + C`. Затем, чтобы удалить контейнеры, достаточно:

```bash
docker-compose down --volumes
```

Чтобы полностью очистить все данные Docker-а (внимание, в том числе других приложений), можно воспользоваться командой:

```bash
docker system prune -f -a --volumes
```

## Запуск тестов

Чтобы запустить тесты и получить по ним статистику:

```bash
./gradlew test
```

# SD-2022-hwproj

<img src="hwproj-logo.png" align="right"  alt="hwproj logo"/>

## Краткое описание

_Software Design HW: client server web app for interaction between students and teachers_

**[Ссылка на документацию](https://glebsolovev.github.io/SD-2022-hwproj/)**

## Состояние разработки

На данный момент в проекте:

- реализован основной функционал
- настроен CI: запуск тестов и линтеров, сборка и публикация документации
- `ARCHITECTURE.md` содержит подробное описание архитектуры
- задокументирован и покрыт интеграционными тестами

## Запуск и использование приложения

### Подготовка директорий

Для запуска в первый раз необходимо подготовить директорию `rabbitmq-server` для сервера `RabbitMQ`.

Для Unix:

```bash
cd rabbitmq-server
bash init.sh
```

Для Windows:

```cmd
cd rabbitmq-server
init.bat
```

### Запуск сервера

В новом терминале необходимо из корня проекта запустить приложение-сервер:

```bash
./gradlew run-server
```

Брокер сообщений при этом запустится автоматически с помощью `rabbitmq-server/docker-compose.yml`.

### Запуск исполнителей

Наконец, осталось запустить желаемое число `runner`-ов, которые будут проверять посылки студентов. Для запуска
нового `runner`-а необходимо в новом терминале из корня проекта выполнить:

```bash
./gradlew run-runner
```

Таким способом можно добавлять произвольное число исполнителей.

### Использование приложения

Теперь все готово. Чтобы попасть в веб-интерфейс приложения, достаточно перейти по
ссылке: http://localhost:8080/. Также можно взаимодействовать по `REST API`, описание появится
позже.

_Дополнительно._
Чтобы отслеживать работу брокера сообщений и управлять ей (при необходимости), можно перейти по
ссылке http://localhost:15672/ и зайти под логином и паролем `guest` и `guest`
соответственно.

## Запуск тестов

Чтобы запустить тесты и получить по ним статистику:

```bash
./gradlew test
```

## Список REST endpoint-ов

С префиксом `/api/`:

* GET `/assignments`: получить список всех заданий
* POST `/assignments`: добавить новое задание
* GET `/assignments/{id}`: получить детали задания под номером `id`
* GET `/submissions`: получить список всех решений
* POST `/submissions`: отправить новое решение
* GET `/submissions/{id}`: получить детали решения под номером `id`

###### Хотелось OpenAPI, но фреймворк Ktor так не умеет :(
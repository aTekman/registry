# Документация по проекту registry

Версия апи: v0.1
<br> Локальное окружение: `http://localhost:8080`

---
Пример формирования адреса:

```http
GET http://localhost:8080/api/v0.1/user/all
```

Где: <br>
`localhost:8080` - адрес сервера <br>
`/api/v0.1` - версия апи <br>
`/user` - название роли или сервиса <br>
`/all` - эндпоинт

---
# Пользователи
## Авторизация

---

### Вход

```http
POST http://localhost:8080/api/v0.1/auth/login
```

#### Запрос:

```curl
curl --request POST \
  --url http://localhost:8080/api/v0.1/auth/login \
  --header 'Content-Type: application/json' \
  --data '{
	"email": "test",
	"password":"test"
}'
```

где: <br>

| Параметр    | Значение           |
|-------------|--------------------|
| email       | Email пользователя |
| password    | Введенный пароль   |

### Ответ:

#### Успешный вход:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IHVzZXIiLCJpYXQiOjE3NDQwNDY2NzIsImV4cCI6MTc0NDEzMzA3Mn0.5o1l1kGDzOb10cPQNW-f4sBJHhwOVKXJ37ogK8G_ibI",
  "user": {
    "id": "5a86f10d-a94f-41f2-a192-8ec5d7c1b7c8",
    "fullName": "test user",
    "birthday": "12.3.4567",
    "phone": "test",
    "email": "test user",
    "passport": "test",
    "snils": "test",
    "medPolicy": "test",
    "role": "USER"
  }
}
```

```http
Status Code = 200 OK
```

#### Неверные данные:

```json
{
  "error" : "Неверный пароль"
}
```

```http
Status Code = 400 Bad Request
```

---

### Регистрация

```http
POST http://localhost:8080/api/v0.1/auth/register
```

#### Запрос:

```curl
curl --request POST \
  --url http://localhost:8080/api/v0.1/auth/register \
  --header 'Content-Type: application/json' \
  --data '{
    "birthday": "12.3.4567",
    "email": "test user",
    "fullName": "test user",
    "medPolicy": "test",
    "passport": "test",
    "phone": "test",
    "snils": "test",
    "password": "123"
}'
```

где: <br>

| Параметр  | Значение                                                   |
|-----------|------------------------------------------------------------|
| birthday  | День рождения                                              |
| email     | Email пользователя (должен быть уникальным)                |
| fullName  | ФИО                                                        |
| medPolicy | Мед. полис (должен быть уникальным, длина не более 16)     |
| passport  | Паспорт (должен быть уникальным, длина не более 10)        |
| phone     | Номер телефона (должен быть уникальным)                    |
| snils     | Снилс (должен быть уникальным, длина не более 11)          |
| password  | Пароль                                                     |
### Ответ:

---

#### Успешная регистрация:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NSIsImlhdCI6MTc0MzUyMTgyOCwiZXhwIjoxNzQzNjA4MjI4fQ.vXkDTsNhsrnXlRf_ei-eJK3q4X78YFcIZMzuyMUbCsY",
  "user": {
    "birthday": "12.3.4567",
    "email": " user",
    "fullName": "test user",
    "medPolicy": "test",
    "passport": "test",
    "phone": "test",
    "snils": "test",
    "role": "USER"
  }
}
```
```http
Status Code = 200 OK
```

---

#### Совпадающие данные:

```json
{
    "error": "Пользователь с таким email уже существует"
}
```
```http
Status Code = 400 Bad Request
```

---

## Запросы пользователя

### Создание приема:

```http
POST http://localhost:8080/api/v0.1/user/tikets/new
```

#### Запрос

```curl
curl --request GET \
  --url http://localhost:8080/api/v0.1/user/tikets/new \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ...' \
  --data '{
    "date": "01.01.2001 00:00",
    "description": "test",
    "doctor": "test doc",
    "user": "50c96c35-1b53-48a2-9d7a-b097aff2defa"
```

где: <br>

| Параметр                | Значение                                                 |
|-------------------------|----------------------------------------------------------|
| --header 'Authorization | Bearer ${token (получается при авторизации/регистрации)} |
| date                    | Дата и время приема (по паттерну: dd.MM.yyyy HH:mm       |
| description             | Описание болезни (жалоба)                                |
| doctor                  | ФИО лечащего врача                                       |
| user                    | id пользователя, за которым закрепляется прием           |
### Ответ:

---

#### Правильный токен:

```json
[
  "message: Запись успешно отправлена на подтверждение",
  {
    "id": "608280f5-1f7f-4735-a102-fbcaf396c171",
    "date": "01.01.2001 00:00",
    "description": "test",
    "results": null,
    "doctor": "test doc",
    "status": "подтверждается"
  }
]
```
```http
Status Code = 200 OK
```

---
#### Просроченный/неверный токен:

```http
Status Code = 403 Forbidden
```

---

### Вывод всех приемов, закрепленных за пользователем:

```http
POST http://localhost:8080/api/v0.1/user/tikets/<id>
```

#### Запрос

```curl
curl --request GET \
  --url http://localhost:8080/api/v0.1/user/tikets/<id> \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ...' \
```

где: <br>

| Параметр                | Значение                                                 |
|-------------------------|----------------------------------------------------------|
| --header 'Authorization | Bearer ${token (получается при авторизации/регистрации)} |
| id                      | id пользователя                                          |
### Ответ:

---

#### Правильный токен:

```json
[
  {
    "id": "608280f5-1f7f-4735-a102-fbcaf396c171",
    "date": "01.01.2001 00:00",
    "description": "test",
    "results": null,
    "doctor": "test doc",
    "status": "подтверждается"
  }
]
```
```http
Status Code = 200 OK
```

---
#### Просроченный/неверный токен:

```http
Status Code = 403 Forbidden
```

---


### Запрос пользователя по id или email:

```http
GET http://localhost:8080/api/v0.1/user/(id или email)/<id или email>
```

#### Запрос

```curl
curl --request GET \
  --url http://localhost:8080/api/v0.1/user/(id или email)/<id или email> \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ...' \
```

где: <br>

| Параметр                | Значение                                                 |
|-------------------------|----------------------------------------------------------|
| --header 'Authorization | Bearer ${token (получается при авторизации/регистрации)} |
| id                      | id искомого пользователя                                 |
| email                   | email искомого пользователя                              |
### Ответ:

---

#### Правильный токен:

```json
{
  "id": 11,
  "fullName": "Иван Иванов3",
  "birthday": "12.3.4567",
  "phone": "88005553513",
  "email": "12345",
  "passport": "1234567291",
  "snils": "12345677901",
  "medPolicy": "123456789033457",
  "role": "USER"
}
```
```http
Status Code = 200 OK
```

---

#### Несуществующий id/email:

```json
{
  "error": "Не найдено пользователя с email/id <email/id>"
}
```
```http
Status Code = 400 Bad Request
```


---

#### Просроченный/неверный токен:

```http
Status Code = 403 Forbidden
```

---

### Обновление пользователя:

```http
PUT http://localhost:8080/api/v0.1/user/id/<id>
```

#### Запрос

```curl
curl --request PUT \
  --url http://localhost:8080/api/v0.1/user/email/{email} \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ...' \
  --data '{
    "birthday": "12.3.4567",
    "email": "test",
    "fullName": "Иван Иванов Иванович",
    "medPolicy": "1234567890123456",
    "passport": "1234567890",
    "phone": "12345678901",
    "snils": "12345678901",
    "role": "USER"
}'
```

где: <br>

| Параметр                | Значение                                                 |
|-------------------------|----------------------------------------------------------|
| --header 'Authorization | Bearer ${token (получается при авторизации/регистрации)} |
| birthday                | День рождения                                            |
| id                      | id пользователя                                          |
| fullName                | ФИО                                                      |
| medPolicy               | Мед. полис (должен быть уникальным, длина не более 16)   |
| passport                | Паспорт (должен быть уникальным, длина не более 10)      |
| phone                   | Номер телефона (должен быть уникальным)                  |
| snils                   | Снилс (должен быть уникальным, длина не более 11)        |
| password                | Пароль                                                   |
| role                    | Роль человека (USER, MEDIC, ADMIN)                       |
Поля, которые не трубется обновлять может = null
### Ответ:

---

#### Правильный токен:

```json
{
    "id": 11,
    "fullName": "Иван Иванов3",
    "birthday": "12.3.4567",
    "phone": "88005553513",
    "email": "12345",
    "passport": "1234567291",
    "snils": "12345677901",
    "medPolicy": "123456789033457",
    "role": "USER"
}
```
```http
Status Code = 200 OK
```

---
#### Несуществующий id:

```json
{
  "error": "Не найдено пользователя с id <id>"
}
```
```http
Status Code = 400 Bad Request
```

---
#### Просроченный/неверный токен:

```http
Status Code = 403 Forbidden
```

---

### Удаление пользователя:

```http
DELETE http://localhost:8080/api/v0.1/user/id/<id>
```

#### Запрос

```curl
curl --request DELETE \
  --url http://localhost:8080/api/v0.1/user/id/<id> \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ...'
```

где: <br>

| Параметр                | Значение                                                 |
|-------------------------|----------------------------------------------------------|
| --header 'Authorization | Bearer ${token (получается при авторизации/регистрации)} |
| id                      | id удаляемого пользователя                               |
### Ответ:

---

#### Правильный токен:

```json
{
  "message":"Пользователь успешно удален"
}
```
```http
Status Code = 200 OK
```

---

#### Несуществующий email:

```json
{
  "error": "Не найдено пользователя с id <id>"
}
```
```http
Status Code = 400 Bad Request
```

---

#### Просроченный/неверный токен:

```http
Status Code = 403 Forbidden
```

---

### Получение списка врачей отдельного направления:

```http
DELETE http://localhost:8080/api/v0.1/user/med/<profession>
```

#### Запрос

```curl
curl --request DELETE \
  --url http://localhost:8080/api/v0.1/user/med/<profession> \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ...'
```

где: <br>

| Параметр                | Значение                                                 |
|-------------------------|----------------------------------------------------------|
| --header 'Authorization | Bearer ${token (получается при авторизации/регистрации)} |
| profession              | направление искомых врачей                               |
### Ответ:

---

#### Правильный токен:

```json
[
  {
    "id": "e0db2437-e7b1-449a-a73f-acc6889995c1",
    "fullName": "test user",
    "phone": "test",
    "email": "test doc",
    "prof": "Терапевт",
    "role": "MEDIC"
  }
]
```
```http
Status Code = 200 OK
```

---

#### Неправильное направление или несуществующее:

```json
{
  "error": "Нет докторов данного направления"
}
```
```http
Status Code = 400 Bad Request
```

---

#### Просроченный/неверный токен:

```http
Status Code = 403 Forbidden
```

---
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
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NSIsImlhdCI6MTc0MzUyMTgyOCwiZXhwIjoxNzQzNjA4MjI4fQ.vXkDTsNhsrnXlRf_ei-eJK3q4X78YFcIZMzuyMUbCsY",
  "user": {
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
    "email": "test",
    "fullName": "Иван Иванов Иванович",
    "medPolicy": "1234567890123456",
    "passport": "1234567890",
    "phone": "12345678901",
    "snils": "12345678901",
    "password": "12345",
    "role": "USER"
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
| role      | Роль человека (USER, MEDIC, ADMIN). При = null, будет USER |
### Ответ:

---

#### Успешная регистрация:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NSIsImlhdCI6MTc0MzUyMTgyOCwiZXhwIjoxNzQzNjA4MjI4fQ.vXkDTsNhsrnXlRf_ei-eJK3q4X78YFcIZMzuyMUbCsY",
  "user": {
    "birthday": "12.3.4567",
    "email": "test",
    "fullName": "Иван Иванов Иванович",
    "medPolicy": "1234567890123456",
    "passport": "1234567890",
    "phone": "12345678901",
    "snils": "12345678901",
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

### Запрос всех (временно, позже будет смещено на другую роль):

```http
GET http://localhost:8080/api/v0.1/user/all
```

#### Запрос

```curl
curl --request GET \
  --url http://localhost:8080/api/v0.1/user/all \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ...' \
```

где: <br>

| Параметр                | Значение                                                 |
|-------------------------|----------------------------------------------------------|
| --header 'Authorization | Bearer ${token (получается при авторизации/регистрации)} |
### Ответ:

---

#### Правильный токен:

```json
[
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

### Запрос по id или email:

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
PUT http://localhost:8080/api/v0.1/user/email/<email>
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

| Параметр                | Значение                                      |
|-------------------------|-----------------------------------------------|
| --header 'Authorization | Bearer ${token (получается при авторизации/регистрации)} |
| birthday  | День рождения                                 |
| email     | Email пользователя (должен быть уникальным)   |
| fullName  | ФИО                                           |
| medPolicy | Мед. полис (должен быть уникальным, длина не более 16) |
| passport  | Паспорт (должен быть уникальным, длина не более 10) |
| phone     | Номер телефона (должен быть уникальным)       |
| snils     | Снилс (должен быть уникальным, длина не более 11) |
| password  | Пароль                                        |
| role      | Роль человека (USER, MEDIC, ADMIN)            |
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
#### Несуществующий email:

```json
{
  "error": "Не найдено пользователя с email <email>"
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
DELETE http://localhost:8080/api/v0.1/user/email/<email>
```

#### Запрос

```curl
curl --request DELETE \
  --url http://localhost:8080/api/v0.1/user/email/<email> \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ...'
```

где: <br>

| Параметр                | Значение                                                 |
|-------------------------|----------------------------------------------------------|
| --header 'Authorization | Bearer ${token (получается при авторизации/регистрации)} |
| email                   | email удаляемого пользователя                            |
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
  "error": "Не найдено пользователя с email <email>"
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
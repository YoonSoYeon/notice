# 공지사항 프로젝트
공지사항 등록, 수정, 삭제, 조회에 대한 API를 구현하는 프로젝트이다.

공지사항에 여러개의 첨부파일을 등록하기 위해서 공지사항의 엔티티와 파일 엔티티가 필요하며, 1:N의 연관 관계를 맺는다.

이 프로젝트의 기술 스택은 다음과 같다.

## 기술 스택
본 프로젝트는 Spring boot 3.3.2와 PostgreSQL DB 기반이며, DAL는 Spring Data JPA 기반으로 되어 있는 프로젝트입니다. 

- Java JDK 17
- Spring Boot 3.3.2
- Spring Web
- Spring Data JPA
- Junit 5
- Lombok
- Gradle
- PostgreSQL 42.2.20

## 주요 기능
공지사항의 주요 기능은 다음과 같다.

- **공지사항 전제 조회**  : 등록한 모든 공지사항을 조회한다.
  - 페이징 기능이 적용되어 있으며, 기본 pageSize 수는 100건이다.
- **공지사항 조회**  : 등록한 공지사항을 조회한다.
  - 조회 수를 증가시킨다.
  - 조회 시, 제목, 내용, 등록일시, 조회수, 작성자 항목을 출력한다.
- **공지사항 생성**  : 새로운 공지사항을 등록한다.
  - 제목에 대한 중복 체크를 한다.
  - 제목, 내용, 공지 시작일시, 공지 종료일시는 필수입력 항목이다.
  - 첨부파일은 존재하지 않아도 된다.
  - 별도의 로그인 기능이 존재하지 않으므로 작성자 정보는 client ip로 대체한다.
- **공지사항 수정**  : 등록한 공지사항을 수정한다.
  - 제목에 대한 중복 체크를 한다.
  - 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일을 수정할 수 있다.
  - 첨부파일이 존재하지 않는 경우 삭제된 걸로 간주한다.
- **공지사항 삭제**  : 등록한 공지사항을 삭제한다.

## API 명세

<details>
 <summary><code>GET</code> <code><b>/api/notice/infos</b></code></summary>

##### Description
> 공지사항 전체 조회 API
  
##### Parameters

> | name      | type  |  required     | data type | description       |
> |-----------|------ |---------------|-----------|-------------------|
> | pageNo    | query | false         | Integer   | default value 0   |
> | pageSize  | query | false         | Integer   | default value 100 |


##### Responses

> | http code     | content-type                      |
> |---------------|-----------------------------------|
> | `200`         | `application/json`                |
>
> > ###### Response body
> > ```json
> > {
> > "success": true,
> > "code": 0,
> >   "result": {
> >     "data": {
> >       "content": [],
> >       "pageNo": 0,
> >       "pageSize": 100,
> >       "totalElements": 0,
> >       "totalPages": 1,
> >       "last": true
> >     }
> >   }
> > }
> > ```

##### Example cURL

> ```javascript
>  curl -X 'GET' \
>  'http://localhost:8080/api/notice/infos?pageNo=0&pageSize=100' \
>  -H 'accept: */*'
> ```

##### Request URL

> ```http
> http://localhost:8080/api/notice/infos?pageNo=0&pageSize=100
> ```

</details>

<details>
 <summary><code>GET</code> <code><b>/api/notice/info/{noticeNo}</b></code></summary>

##### Description
> 공지사항 조회 API
  
##### Parameters

> | name      | type  |  required     | data type | description       |
> |-----------|------ |---------------|-----------|-------------------|
> | noticeNo  | path  | true          | Integer   |                   |


##### Responses

> | http code     | content-type                      |
> |---------------|-----------------------------------|
> | `200`         | `application/json`                |
>
> > ###### Response body
> > ```json
> > {
> > "success": true,
> > "code": 0,
> >   "result": {
> >     "data": {
> >       "title": "",
> >       "contents": "",
> >       "writer": "",
> >       "createDate": "2024-07-21T10:26:29.983+00:00",
> >       "viewCount": 1
> >     }
> >   }
> > }
> > ```

> | http code     | content-type                      |
> |---------------|-----------------------------------|
> | `404`         | `application/json`                |
>
> > ###### Response body
> > ```json
> > {
> > "success": false,
> > "code": 404,
> >   "result": {
> >     "msg": "공지사항을 찾을 수 없습니다."
> >   }
> > }
> > ```

##### Example cURL

> ```javascript
>  curl -X 'GET' \
> 'http://localhost:8080/api/notice/info/1' \
> -H 'accept: */*'
> ```

##### Request URL

> ```http
> http://localhost:8080/api/notice/info/1
> ```

</details>

<details>
 <summary><code>POST</code> <code><b>/api/notice/info</b></code></summary>

##### Description
> 공지사항 등록 API
  
##### Parameters

###### Request header
| content-type        |
|---------------------|
|`multipart/form-data`|

###### Request body
> | name      |  required     | data type            | description       |
> |-----------|---------------|----------------------|-------------------|
> | title     | true          | String               | text              |
> | contents  | true          | String               | text              |
> | writer    | false         | String               | text              |
> | startDate | true          | String(date-time)    | text              |
> | endDate   | true          | String(date-time)    | text              |
> | file      | false         | array[string]        | file              |


##### Responses

> | http code     | content-type                      |
> |---------------|-----------------------------------|
> | `200`         | `application/json`                |
>
> > ###### Response body
> > ```json
> > {
> > "success": true,
> > "code": 0,
> >   "result": {
> >     "data": "공지사항 등록에 성공하였습니다."
> >   }
> > }
> > ```

> | http code     | content-type                      |
> |---------------|-----------------------------------|
> | `400`         | `application/json`                |
>
> > ###### Response body
> > ```json
> > {
> > "success": false,
> > "code": 400,
> >   "result": {
> >     "msg": "..."
> >   }
> > }
> > ```

> | http code     | content-type                      |
> |---------------|-----------------------------------|
> | `500`         | `application/json`                |
>
> > ###### Response body
> > ```json
> > {
> > "success": false,
> > "code": 500,
> >   "result": {
> >     "msg": "이미 존재하는 공지사항입니다."
> >   }
> > }
> > ```

##### Example cURL

> ```javascript
>  curl -X 'POST' \
> 'http://localhost:8080/api/notice/info' \
> -H 'accept: application/json' \
> -H 'Content-Type: multipart/form-data' \
> -F 'file=@\"C:/myfile.txt\"' \
> -F 'title=test111111' \
> -F 'contents=test' \
> -F 'startDate=2024-07-24 00:00:00' \
> -F 'endDate=2024-07-24 23:59:59'
> ```

##### Request URL

> ```http
> http://localhost:8080/api/notice/info
> ```

</details>

<details>
 <summary><code>PATCH</code> <code><b>/api/notice/info/{noticeNo}</b></code></summary>

##### Description
> 공지사항 수정 API
  
##### Parameters

> | name      | type  |  required     | data type | description       |
> |-----------|------ |---------------|-----------|-------------------|
> | noticeNo  | path  | true          | Integer   |                   |

###### Request header
| content-type        |
|---------------------|
|`multipart/form-data`|

###### Request body
> | name      |  required     | data type            | description       |
> |-----------|---------------|----------------------|-------------------|
> | title     | true          | String               | text              |
> | contents  | true          | String               | text              |
> | startDate | true          | String(date-time)    | text              |
> | endDate   | true          | String(date-time)    | text              |
> | file      | false         | array[string]        | file              |


##### Responses

> | http code     | content-type                      |
> |---------------|-----------------------------------|
> | `200`         | `application/json`                |
>
> > ###### Response body
> > ```json
> > {
> > "success": true,
> > "code": 0,
> >   "result": {
> >     "data": "공지사항 수정에 성공하였습니다."
> >   }
> > }
> > ```

> | http code     | content-type                      |
> |---------------|-----------------------------------|
> | `400`         | `application/json`                |
>
> > ###### Response body
> > ```json
> > {
> > "success": false,
> > "code": 400,
> >   "result": {
> >     "msg": "..."
> >   }
> > }
> > ```

> | http code     | content-type                      |
> |---------------|-----------------------------------|
> | `404`         | `application/json`                |
>
> > ###### Response body
> > ```json
> > {
> > "success": false,
> > "code": 404,
> >   "result": {
> >     "msg": "공지사항을 찾을 수 없습니다."
> >   }
> > }
> > ```

> | http code     | content-type                      |
> |---------------|-----------------------------------|
> | `500`         | `application/json`                |
>
> > ###### Response body
> > ```json
> > {
> > "success": false,
> > "code": 500,
> >   "result": {
> >     "msg": "이미 존재하는 공지사항입니다."
> >   }
> > }
> > ```

##### Example cURL

> ```javascript
>  curl -X 'PATCH' \
> 'http://localhost:8080/api/notice/info/1' \
> -H 'accept: application/json' \
> -H 'Content-Type: multipart/form-data' \
> -F 'file=@\"C:/myfile.txt\"' \
> -F 'title=test111111' \
> -F 'contents=test' \
> ```

##### Request URL

> ```http
> http://localhost:8080/api/notice/info/1
> ```

</details>

<details>
 <summary><code>DELETE</code> <code><b>/api/notice/info/{noticeNo}</b></code></summary>

##### Description
> 공지사항 삭제 API
  
##### Parameters

> | name      | type  |  required     | data type | description       |
> |-----------|------ |---------------|-----------|-------------------|
> | noticeNo  | path  | true          | Integer   |                   |


##### Responses

> | http code     | content-type                      |
> |---------------|-----------------------------------|
> | `200`         | `application/json`                |
>
> > ###### Response body
> > ```json
> > {
> > "success": true,
> > "code": 0,
> >   "result": {
> >     "data": "공지사항 삭제에 성공하였습니다."
> >   }
> > }
> > ```

> | http code     | content-type                      |
> |---------------|-----------------------------------|
> | `404`         | `application/json`                |
>
> > ###### Response body
> > ```json
> > {
> > "success": false,
> > "code": 404,
> >   "result": {
> >     "msg": "공지사항을 찾을 수 없습니다."
> >   }
> > }
> > ```

##### Example cURL

> ```javascript
>  curl -X 'DELETE' \
> 'http://localhost:8080/api/notice/info/1' \
> -H 'accept: */*'
> ```

##### Request URL

> ```http
> http://localhost:8080/api/notice/info/1
> ```

</details>

## DB ERD
![image](https://github.com/user-attachments/assets/02c21995-b1ef-43c5-85f1-36a56529b5be)



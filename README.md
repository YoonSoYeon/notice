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

- 공지사항 생성 : 새로운 공지사항을 등록한다.
  - 제목에 대한 중복 체크를 한다.
  - 제목, 내용, 공지 시작일시, 공지 종료일시는 필수입력 항목이다.
  - 첨부파일은 존재하지 않아도 된다.
- 공지사항 수정 : 등록한 공지사항을 수정한다.
  - 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일을 수정할 수 있다.
  - 제목에 대한 중복 체크를 한다.
- 공지사항 전제 조회 : 등록한 모든 공지사항을 조회한다. 
  - 페이징 기능이 적용되어 있으며, 기본 pageSize 수는 100건이다.
- 공지사항 조회 : 등록한 공지사항을 조회한다.
  - 조회 수를 증가시킨다.
  - 조회 시, 제목, 내용, 등록일시, 조회수, 작성자 항목을 출력한다.
- 공지사항 삭제 : 등록한 공지사항을 삭제한다.

## API 명세

| Description | Method | URI | Request | Response |
| ----------- | ------ | --- | ------- | -------- |
| 공지사항 전체 조회 | GET | /api/notice/infos | - | - |
| 공지사항 조회 | GET | /api/notice/info/:noticeNo | - | - |
| 공지사항 등록 | POST | /api/notice/info | - | - |
| 공지사항 수정 | PATCH | /api/notice/info/:noticeNo | - | - |
| 공지사항 삭제 | DELETE | /api/notice/info/:noticeNo | - | - |

## DB ERD
![image](https://github.com/user-attachments/assets/f028d76a-002d-4bba-8463-2a73241a195f)


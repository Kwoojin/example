# 예약시스템 구축

### - 개발 언어
Java (version : 17.0.3.1)

------------
### - 프레임워크
Spring Framework

> dependency
>> 1. Spring Web
>> 2. Spring Data JPA
>> 3. QueryDSL
>> 4. Validation
>> 5. Lombok
>> 6. H2 Database
>> 7. [p6spy](https://github.com/gavlyukovskiy/spring-boot-data-source-decorator)


------------
### - RDBMS
H2

------------
### - 데이터 설계
[src/main/resources/schema.sql](https://github.com/Kwoojin/example/blob/master/src/main/resources/schema.sql)

* Lecture
  ```sql
  create table lecture
  (
      lecture_id              bigint          NOT NULL AUTO_INCREMENT,    --강연 PK
      title                   varchar(50)     NOT NULL,                   --강연명
      place                   varchar(50)     NOT NULL,                   --강연 장소
      lecturer                varchar(10)     NOT NULL,                   --강연자
      content                 varchar(255),                               --강연 내용
      start_time              timestamp       NOT NULL,                   --강연 시작 시간
      end_time                timestamp       NOT NULL,                   --강연 종료 시간
      total_number_seats      int             NOT NULL DEFAULT 0,         --최대 수용 인원
      remaining_number_seats  int             NOT NULL DEFAULT 0,         --현재 남은 자리
      created_date            datetime        NOT NULL,
      last_modified_date      datetime        NOT NULL,
      PRIMARY KEY (lecture_id)
  );
  ```

* member
  ```sql
  create table member (
      member_id               bigint          NOT NULL AUTO_INCREMENT,    --회원 PK
      name                    varchar(10),                                --회원 이름
      emp_no                  varchar(10),                                --회원 사번
      created_date            datetime        NOT NULL,
      last_modified_date      datetime        NOT NULL,
      PRIMARY KEY (member_id),
      CONSTRAINT unq_member_emp_no UNIQUE (emp_no)
  );
  ```

* reservation
  ```sql
  create table reservation (
      reservation_id          bigint          NOT NULL AUTO_INCREMENT,    --예약 PK
      lecture_id              bigint          NOT NULL,                   --강연 PK (lecture 참고)
      member_id               bigint          NOT NULL,                   --회원 PK (member 참고)
      status                  varchar(10)     NOT NULL,                   --예약 상태 'COMPLETE', 'CANCEL'
      created_date            datetime        NOT NULL,
      last_modified_date      datetime        NOT NULL,
      PRIMARY KEY (reservation_id),
      CONSTRAINT fk_reservation_to_lecture FOREIGN KEY (lecture_id) REFERENCES lecture (lecture_id) ON DELETE RESTRICT ON UPDATE RESTRICT,
      CONSTRAINT fk_reservation_to_member FOREIGN KEY (member_id) REFERENCES member (member_id) ON DELETE RESTRICT ON UPDATE RESTRICT
  );
  ```

------------
### - API

#### 1. [GET] /back-office/lectures
###### 강연 목록 (전체 강연 목록)
> response example
> ```json
> {
>   "content": [
>     {
>       "id": "강연 ID",
>       "title": "강연명",
>       "place": "강연장",
>       "lecturer": "강연자",
>       "startTime": "시작시간",
>       "endTime": "종료시간",
>       "content": "강연내용"
>     },
>     {
>       "id": "강연 ID",
>       "title": "강연명",
>       "place": "강연장",
>       "lecturer": "강연자",
>       "startTime": "시작시간",
>       "endTime": "종료시간",
>       "content": "강연내용"
>     }
>   ]
> }
> ```

#### 2. [POST] /back-office/lectures
###### 강연 등록 (강연자, 강연장, 신청인원, 강연시간, 강연내용 입력)
> request example
> ```json
> {
>   "title": "강연명(입력하지 않을 시 강연자명)",
>   "place": "강연장",
>   "lecturer": "강연자",
>   "content": "강연내용",
>   "totalCount": "신청인원",
>   "startTime": "시작시간",
>   "endTime": "종료시간"
> }
> ```
> response example
> ```json
> {
>   "content": {
>     "id": "강연 ID",
>     "title": "강연명",
>     "place": "강연장",
>     "lecturer": "강연자",
>     "startTime": "시작시간",
>     "endTime": "종료시간",
>     "content": "강연내용"
>   }
> }
> ```

#### 3. [GET] /back-office/lectures/empNos
###### 강연신청자 목록(강연별 신청한 사번 목록)
> response example
> ```json
> {
>   "content": [
>     {
>       "id": "강연 ID",
>       "title": "강연명",
>       "place": "강연장",
>       "lecturer": "강연자",
>       "empNos": [
>         "강연에 신청한 사번1",
>         "강연에 신청한 사번2"
>       ]
>     },
>     {
>       "id": "강연 ID",
>       "title": "강연명",
>       "place": "강연장",
>       "lecturer": "강연자",
>       "empNos": [
>         "강연에 신청한 사번1",
>         "강연에 신청한 사번2"
>       ]
>     }
>   ]
> }
> ```

#### 4. [GET] /lectures
###### 강연 목록 (강연 시작 7일 전부터 1일 후까지 노출)
> response example
> ```json
> {
>   "content": [
>     {
>       "id": "강연 ID",
>       "title": "강연명",
>       "place": "강연장",
>       "lecturer": "강연자",
>       "startTime": "시작시간",
>       "endTime": "종료시간",
>       "content": "강연내용"
>     },
>     {
>       "id": "강연 ID",
>       "title": "강연명",
>       "place": "강연장",
>       "lecturer": "강연자",
>       "startTime": "시작시간",
>       "endTime": "종료시간",
>       "content": "강연내용"
>     }
>   ]
> }
> ```

#### 5. [POST] /reservations/{lectureId}
###### 강연 등록 (강연자, 강연장, 신청인원, 강연시간, 강연내용 입력)
> request example
> ```json
> {
>   "empNo": "사번"
> }
> ```
> response example
> ```json
> {
>   "content": {
>     "id": "예약 ID",
>     "empNo": "사번",
>     "lectureId": "강연 ID"
>   }
> }
> ```

#### 6. [GET] /reservations?empNo={empNo}
###### 신청내역 조회 (사번 입력)
> response example
> ```json
> {
>   "content": [
>     {
>       "id": "강연 ID",
>       "title": "강연명",
>       "place": "강연장",
>       "lecturer": "강연자",
>       "startTime": "시작시간",
>       "endTime": "종료시간",
>       "content": "강연내용",
>       "status": "예약상태"
>     },
>     {
>       "id": "강연 ID",
>       "title": "강연명",
>       "place": "강연장",
>       "lecturer": "강연자",
>       "startTime": "시작시간",
>       "endTime": "종료시간",
>       "content": "강연내용",
>       "status": "예약상태"
>     }
>   ]
> }
> ```

------------
### - ETC
1. API 중 강연별 신청한 사번 목록 조회를 위한 LectureService <> LectureQueryService 분리 (OSIV)  
   ( 화면에 표시되는 값을 만들기 위한 controller 계층에 종속적인 부분 분리, repository 또한 분리 예정)    
   studio.example.lecture.service.query.LectureQueryService  
   studio.example.lecture.service.LectureService
2. 강연 등록 입력 부분에서 강연명이 필요하지 않을까 싶어 title 을 추가했지만, title이 없을 경우 강사명으로 대체
3. studio.example.InitData - 테스트 데이터 추가 및 memoryDB 사용 시 data.sql 을 위한 query 생성용
4. 추후 URI /back-office/** - Filter 통해 Authorization 검증 (JWT 사용)
5. Get 요청 시 Spring Pageable 을 통해 페이징 기능을 추가했다가 단순 결과에 집중하기 위해 제거
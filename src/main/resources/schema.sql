DROP TABLE IF EXISTS lecture CASCADE;
DROP TABLE IF EXISTS member CASCADE;
DROP TABLE IF EXISTS reservation CASCADE;

drop sequence if exists lecture_seq;
drop sequence if exists member_seq;
drop sequence if exists reservation_seq;

create sequence lecture_seq start with 1 increment by 50;
create sequence member_seq start with 1 increment by 50;
create sequence reservation_seq start with 1 increment by 50;

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

create table member (
    member_id               bigint          NOT NULL AUTO_INCREMENT,    --회원 PK
    name                    varchar(10),                                --회원 이름
    emp_no                  varchar(10),                                --회원 사번
    created_date            datetime        NOT NULL,
    last_modified_date      datetime        NOT NULL,
    PRIMARY KEY (member_id),
    CONSTRAINT unq_member_emp_no UNIQUE (emp_no)
);

create table reservation (
    reservation_id          bigint          NOT NULL AUTO_INCREMENT,    --예약 PK
    lecture_id              bigint          NOT NULL,                   --강연 PK (lecture 참고)
    member_id               bigint          NOT NULL,                   --회원 PK (member 참고)
    status                  varchar(10),                                --예약 상태 'COMPLETE', 'CANCEL'
    created_date            datetime        NOT NULL,
    last_modified_date      datetime        NOT NULL,
    PRIMARY KEY (reservation_id),
    CONSTRAINT fk_reservation_to_lecture FOREIGN KEY (lecture_id) REFERENCES lecture (lecture_id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_reservation_to_member FOREIGN KEY (member_id) REFERENCES member (member_id) ON DELETE RESTRICT ON UPDATE RESTRICT
);
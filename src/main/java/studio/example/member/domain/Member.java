package studio.example.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import studio.example.model.BaseTimeEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@SequenceGenerator(
        name="member_seq_generator",
        sequenceName = "member_seq",
        initialValue = 1, allocationSize = 50
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_generator")
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String empNo;

    @Builder
    public Member(String name, String empNo) {
        this.name = name;
        this.empNo = empNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(getId(), member.getId()) && Objects.equals(getEmpNo(), member.getEmpNo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmpNo());
    }
}

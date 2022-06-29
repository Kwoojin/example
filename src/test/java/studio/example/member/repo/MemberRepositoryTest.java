package studio.example.member.repo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import studio.example.member.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @PersistenceContext EntityManager em;

    @Test
    public void findByEmpNo() {
        Member member = Member.builder().name("name").empNo("X1324").build();
        em.persist(member);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findByEmpNo("X1324").orElseThrow();
        assertThat(findMember).isEqualTo(member);
    }

}
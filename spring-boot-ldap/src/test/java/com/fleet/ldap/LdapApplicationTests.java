package com.fleet.ldap;

import com.fleet.ldap.enity.Person;
import com.fleet.ldap.repository.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LdapApplicationTests {

    @Resource
    private PersonRepository personRepository;

    @Test
    public void findAll() {
        personRepository.findAll().forEach(System.out::println);
    }

    @Test
    public void save() {
        Person person = new Person();
        person.setUid("uid:1");
        person.setSuerName("AAA");
        person.setCommonName("aaa");
        person.setUserPassword("123456");
        personRepository.save(person);

        findAll();
    }
}

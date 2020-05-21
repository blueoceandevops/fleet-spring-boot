package com.fleet.ldap;

import com.fleet.ldap.enity.Person;
import com.fleet.ldap.repository.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LdapApplicationTests {

    @Resource
    private PersonRepository personRepository;

    @Resource
    private LdapTemplate ldapTemplate;

    @Test
    public void findAll() {
        Iterable<Person> iterable = personRepository.findAll();
        iterable.forEach(p -> {
            System.out.println(p);
        });
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

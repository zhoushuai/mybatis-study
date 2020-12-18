package org.ken.study.admin.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ken.study.admin.domain.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author yszh@royole.com
 */
@Sql(scripts = {"/sql/h2/schema.sql", "/sql/h2/person-mapper.sql"})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
public class PersonMapperTest {

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    public void selectOne() {
        Person person = personMapper.selectOne(1L);
        assertEquals(Long.valueOf(1L), person.getId());
        assertEquals("飞", person.getFirstName());
        assertEquals("张", person.getLastName());
        assertEquals("河北涿州", person.getContactAddr());
    }

    @Test
    public void selectAll() {
        List<Person> personList = personMapper.selectAll();
        assertEquals(3, personList.size());
    }

    @Test
    public void insert() {
        Person person = new Person();
        person.setFirstName("操");
        person.setLastName("曹");
        person.setContactAddr("安徽亳州");
        transactionTemplate.execute(transactionStatus -> personMapper.insertPerson(person));
        assertNotNull(person.getId());
        Person result = personMapper.selectOne(person.getId());
        assertNotNull(result);
        assertEquals("曹", result.getLastName());
        assertEquals("操", result.getFirstName());
        assertEquals("安徽亳州", result.getContactAddr());
        assertNull(result.getContactPhone());
    }

    @Test
    public void update() {
        Person person = new Person();
        person.setId(1L);
        person.setFirstName("小飞飞");
        person.setLastName("张");
        person.setContactAddr("成都");
        Integer result = transactionTemplate.execute(transactionStatus -> personMapper.updatePerson(person));
        assertEquals(Integer.valueOf(1), result);

        Person result2 = personMapper.selectOne(1L);
        assertEquals(Long.valueOf(1), result2.getId());
        assertEquals("张", result2.getLastName());
        assertEquals("小飞飞", result2.getFirstName());
        assertEquals("成都", result2.getContactAddr());
        assertNull(result2.getContactPhone());
    }


}
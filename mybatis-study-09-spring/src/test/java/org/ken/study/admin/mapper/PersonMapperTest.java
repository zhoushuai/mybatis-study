package org.ken.study.admin.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ken.study.admin.domain.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author yszh@royole.com
 */
@Sql(scripts = {"/sql/h2/schema.sql", "/sql/h2/person-mapper.sql"})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
public class PersonMapperTest {

    @Autowired
    private PersonMapper personMapper;

    @Test
    public void selectOne() {
        Person person = personMapper.selectOne(1L);
        assertEquals(Long.valueOf(1L), person.getId());
        assertEquals("飞", person.getFirstName());
        assertEquals("张", person.getLastName());
        assertEquals("河北涿州", person.getContactAddr());
    }
}
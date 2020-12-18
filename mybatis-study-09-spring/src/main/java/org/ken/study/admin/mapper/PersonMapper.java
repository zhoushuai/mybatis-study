package org.ken.study.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ken.study.admin.domain.entity.Person;

import java.util.List;

/**
 * @author yszh@royole.com
 */
@Mapper
public interface PersonMapper {

    Person selectOne(Long id);

    int updatePerson(Person person);

    int deletePerson(Long id);

    List<Person> selectAll();

    int insertPerson(Person person);

}

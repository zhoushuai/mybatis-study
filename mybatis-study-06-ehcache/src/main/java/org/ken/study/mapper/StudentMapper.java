package org.ken.study.mapper;

import org.ken.study.domain.entity.Student;

/**
 * @author yszh@royole.com
 */
public interface StudentMapper {

    Student selectOne(Long id);

    void delete(Long id);

    void insert(Student student);
}

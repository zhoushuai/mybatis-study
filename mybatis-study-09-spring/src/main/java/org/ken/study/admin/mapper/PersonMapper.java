package org.ken.study.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ken.study.admin.domain.entity.Person;

/**
 * @author yszh@royole.com
 */
@Mapper
public interface PersonMapper {

    Person selectOne(Long id);

}

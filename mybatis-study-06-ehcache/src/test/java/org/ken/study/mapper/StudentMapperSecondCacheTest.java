package org.ken.study.mapper;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.ken.study.domain.entity.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * mybatis二级缓存属于namespace级别
 * <p>
 * 开启二级缓存
 * 1. 全局配置开启缓存 <setting name="cacheEnabled" value="true"/>
 * 2. &lt;cache/&gt;&lt;/cache&gt;开启namespace二级缓存
 * </p>
 * <p>
 *  二级缓存设置
 *  1.
 * </p>
 * <p>
 * 二级缓存总结
 * 1. 二级缓存的作用域是namespace
 * 2. 二级缓存是在SqlSession会话结束后才可以查询的到，SqlSession会话结束前为一级缓存。
 * 3. 二级缓存遇到insert/update/delete操作后会清空一级和二级缓存
 *
 * @author yszh@royole.com
 */
public class StudentMapperSecondCacheTest {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void init() throws IOException {
        File resource = Resources.getResourceAsFile("mybatis-config.xml");
        try (InputStream is = new FileInputStream(resource)) {
            this.sqlSessionFactory = new SqlSessionFactoryBuilder()
                    .build(is);
        }
    }

    @Test
    public void selectOneWithSuccess() {
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        StudentMapper studentMapper1 = sqlSession1.getMapper(StudentMapper.class);
        Student result1 = studentMapper1.selectOne(1L);
        System.out.println("result1 = " + result1);
        sqlSession1.close();

        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        StudentMapper studentMapper2 = sqlSession2.getMapper(StudentMapper.class);
        Student result2 = studentMapper2.selectOne(1L);
        System.out.println("result2 = " + result2);
        sqlSession2.close();

        System.out.println("result1.equals(result2) = " + result1.equals(result2));

        //验证二级缓存是否生效
        assertSame(result1, result2);

    }

    @Test
    public void selectOneDifferentSqlSessionCacheInvalid() {
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        try {
            StudentMapper studentMapper1 = sqlSession1.getMapper(StudentMapper.class);
            Student result1 = studentMapper1.selectOne(1L);
            System.out.println("result1 = " + result1);

            StudentMapper studentMapper2 = sqlSession2.getMapper(StudentMapper.class);
            Student result2 = studentMapper2.selectOne(1L);
            System.out.println("result2 = " + result2);

            System.out.println(String.format("(result1 == result2) = %b", result1 == result2));

            assertNotSame(result1, result2);
        } finally {
            sqlSession1.close();
            sqlSession2.close();
        }

    }

    @Test
    public void selectOneDifferentSelectCacheInvalid() {
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        StudentMapper studentMapper1 = sqlSession1.getMapper(StudentMapper.class);
        Student result1 = studentMapper1.selectOne(1L);
        System.out.println("result1 = " + result1);

        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        StudentMapper studentMapper2 = sqlSession2.getMapper(StudentMapper.class);
        Student result2 = studentMapper2.selectOne(2L);
        System.out.println("result2 = " + result2);
    }

    @Test
    public void selectOneWriteHandleCacheInvalid() {
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        StudentMapper studentMapper1 = sqlSession1.getMapper(StudentMapper.class);
        Student result1 = studentMapper1.selectOne(1L);
        System.out.println("result1 = " + result1);

        Student student = new Student();
        student.setFirstName("邓");
        student.setLastName("亚萍");
        student.setAddr("北京体育中心");
        studentMapper1.insert(student);

        Student result2 = studentMapper1.selectOne(1L);
        System.out.println("result2 = " + result2);
    }

    @Test
    public void selectOneClearCacheCacheInvalid() {
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        StudentMapper studentMapper1 = sqlSession1.getMapper(StudentMapper.class);
        Student result1 = studentMapper1.selectOne(1L);
        System.out.println("result1 = " + result1);

        sqlSession1.clearCache();

        Student result2 = studentMapper1.selectOne(1L);
        System.out.println("result2 = " + result2);
    }


}

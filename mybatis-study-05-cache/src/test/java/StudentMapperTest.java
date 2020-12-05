import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ken.study.domain.entity.Student;
import org.ken.study.mapper.StudentMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * mybatis一级缓存属于SqlSession级别，并且不允许关闭。
 * 1. 不同SqlSession直接是不共享缓存的。
 * 2. Mybatis的缓存是按照执行的SQL语句和参数作为缓存的key进行缓存的，
 *    使用相同查询语句和相同查询参数多次查询会得到第一次查询缓存的结果
 * 3.
 *
 * @author yszh@royole.com
 */
public class StudentMapperTest {

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
        SqlSession sqlSession = sqlSessionFactory.openSession();
        StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
        Student result1 = studentMapper.selectOne(1L);
        System.out.println("result1 = " + result1);

        Student result2 = studentMapper.selectOne(1L);
        System.out.println("result2 = " + result2);

        System.out.println("result1.equals(result2) = " + result1.equals(result2));
    }

    @Test
    public void selectOneDifferentSqlSessionCacheInvalid() {
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        StudentMapper studentMapper1 = sqlSession1.getMapper(StudentMapper.class);
        Student result1 = studentMapper1.selectOne(1L);
        System.out.println("result1 = " + result1);

        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        StudentMapper studentMapper2 = sqlSession2.getMapper(StudentMapper.class);
        Student result2 = studentMapper2.selectOne(1L);
        System.out.println("result2 = " + result2);

        System.out.println(String.format("(result1 == result2) = %b", result1 == result2));

        assertNotSame(result1, result2);

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

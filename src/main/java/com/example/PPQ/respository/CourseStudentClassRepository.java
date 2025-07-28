package com.example.PPQ.respository;

import com.example.PPQ.Entity.CourseStudentKey;
import com.example.PPQ.Entity.CourseStudentClassEntity;
import com.example.PPQ.Payload.Projection_Interface.CourseRegisterView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface CourseStudentClassRepository extends JpaRepository<CourseStudentClassEntity,CourseStudentKey> {
    // Ví dụ: tìm tất cả các khóa học mà sinh viên đã đăng ký
    List<CourseStudentClassEntity> findByIdStudent(int idStudent);

    // Tìm tất cả sinh viên đã đăng ký 1 khóa học
    List<CourseStudentClassEntity> findByIdCourse(int idCourse);

    boolean existsById(CourseStudentKey key); // dùng được existsById với composite key
    @Query(value = "SELECT " +
            "s.fullName as fullname, " +
            "co.nameCourse as nameCourse, " +
            "cl.className as nameClass, " +
            "c.enrollmentDate as enrollmentDate " +
            "from CourseStudentClass c " +
            "inner join Student s on c.idStudent = s.ID " +
            "inner join Class cl on c.idClass = cl.ID " +
            "inner join Course co on co.ID =c.idCourse WHERE " +
            "(:idCourse IS NULL OR c.idCourse = :idCourse) AND " +
            "(:nameStudent IS NULL OR s.fullName = '' or s.fullName like %:nameStudent%) AND " +
            "(:idClass IS NULL OR c.idClass = :idClass)",nativeQuery = true)
    List<CourseRegisterView> searchCourseRegister(Integer idCourse, String nameStudent, Integer idClass);

    @Modifying
    @Query("delete from CourseStudentClassEntity c where c.idCourse = :idCourse")
    void deleteCourseStudentClassByIdCourses(int idCourse);

    @Modifying
    @Query("delete from CourseStudentClassEntity c where c.idClass = :idClass")
    void deleteCourseStudentClassByIdClass(int idClass);
    List<CourseStudentClassEntity> findByIdClass(Integer idClass);

    List<CourseStudentClassEntity> findIdClasByIdCourse(Integer idCourse);

    @Modifying
    @Query("delete from CourseStudentClassEntity c where c.idStudent = :idStudent")
    void deleteCourseStudentClassByIdStudent(Integer idStudent);

    // hien danh sach hoc sinh da dang ki khoa hoc
    @Query(value = "SELECT " +
            "s.fullName as fullname, " +
            "co.nameCourse as nameCourse, " +
            "cl.className as nameClass, " +
            "c.enrollmentDate as enrollmentDate " +
            "from CourseStudentClass c " +
            "inner join Student s on c.idStudent = s.ID " +
            "inner join Class cl on c.idClass = cl.ID " +
            "inner join Course co on co.ID =c.idCourse ", nativeQuery = true)
    List<CourseRegisterView> findAllCourseRegister();

}
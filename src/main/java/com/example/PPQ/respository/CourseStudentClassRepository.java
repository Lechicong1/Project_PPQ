package com.example.PPQ.respository;

import com.example.PPQ.Entity.CourseStudentKey;
import com.example.PPQ.Entity.CourseStudentClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface CourseStudentClassRepository extends JpaRepository<CourseStudentClassEntity,CourseStudentKey> {
    // Ví dụ: tìm tất cả các khóa học mà sinh viên đã đăng ký
    List<CourseStudentClassEntity> findByIdStudent(int idStudent);
    // Tìm tất cả sinh viên đã đăng ký 1 khóa học
    List<CourseStudentClassEntity> findByIdCourse(int idCourse);
    boolean existsById(CourseStudentKey key); // dùng được existsById với composite key
    @Query("SELECT c FROM CourseStudentClassEntity c WHERE " +
            "(:idCourse IS NULL OR c.idCourse = :idCourse) AND " +
            "(:idStudent IS NULL OR c.idStudent = :idStudent) AND " +
            "(:idClass IS NULL OR c.idClass = :idClass)")
    List<CourseStudentClassEntity> searchCourseRegister(Integer idCourse,Integer idStudent,Integer idClass);

    List<CourseStudentClassEntity> findByIdClass(Integer idClass);
    List<CourseStudentClassEntity> findIdClasByIdCourse(Integer idCourse);
}

package com.example.PPQ.Service;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.DuplicateResourceException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.ClassRequest;
import com.example.PPQ.Payload.Response.Class_response;
import com.example.PPQ.Service_Imp.ClassServiceImp;
import com.example.PPQ.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ClassService implements ClassServiceImp {
    @Autowired
    ClassRespository classRepository;
    @Autowired
    CourseRespository CourseRepository;
    @Autowired
    TeacherRespository TeacherRepository;
    @Autowired
    ScheduleRespository scheduleRespository;
    @Autowired
    CourseStudentClassRepository courseStudentClassRepository;
    @Autowired
    UsersRepository usersRepository;
    @Override
    public List<Class_response> getAllClasses() {
        List<Class_response> list = new ArrayList<Class_response>();
        List<ClassesEntity> listclass=classRepository.findAll();
        for(ClassesEntity c:listclass){
            Class_response class_dto=new Class_response();
            class_dto.setId(c.getId());
            class_dto.setClassName(c.getClassName());
            class_dto.setStatus(c.getStatus());
            class_dto.setCurrentStudents(c.getCurrentStudents());
            if(c.getIdTeachers()==null)
            class_dto.setIdTeachers(-1);
            else
                class_dto.setIdTeachers(c.getIdTeachers());
            if(c.getIdTeachers()!=null){
            Teacher_Entity teacher=TeacherRepository.findById(c.getIdTeachers()).orElseThrow(()->new ResourceNotFoundException("Giáo viên không tồn tại")) ;
            class_dto.setNameTeacher(teacher.getFullName()); }
            if(c.getIdCourses()==null)
            class_dto.setIdCourses(-1);
             else
                 class_dto.setIdCourses(c.getIdCourses());
            // tra ve ten khoa hoc
            if(c.getIdCourses()!=null){
                CourseEntity course =CourseRepository.findById(c.getIdCourses()).orElseThrow(()->new ResourceNotFoundException("Khóa học không tồn tại")) ;
                class_dto.setNameCourse(course.getNameCourse());
            }

            class_dto.setMaxStudents(c.getMaxStudents());

            list.add(class_dto);
        }
        return list;
    }

    @Override
    public List<Class_response> getClassByIdTeacher() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        User_Entity user=usersRepository.findByUsername(username);
        if(user==null){
            throw new ResourceNotFoundException("Không tồn tại User");
        }
        Teacher_Entity teacher=TeacherRepository.findById(user.getId()).orElseThrow(()->new ResourceNotFoundException("Giáo viên không tồn tại"));
        List<ClassesEntity> class_entity=classRepository.findByIdTeachers(teacher.getId());
        List<Class_response> list = new ArrayList<Class_response>();
        for(ClassesEntity c:class_entity){
            Class_response class_dto=new Class_response();
            class_dto.setId(c.getId());
            class_dto.setClassName(c.getClassName());
            class_dto.setStatus(c.getStatus());
            class_dto.setCurrentStudents(c.getCurrentStudents());
            if(c.getIdTeachers()==null)
                class_dto.setIdTeachers(-1);
            else
                class_dto.setIdTeachers(c.getIdTeachers());
            if(c.getIdTeachers()!=null){
                class_dto.setNameTeacher(teacher.getFullName()); }
            if(c.getIdCourses()==null)
                class_dto.setIdCourses(-1);
            else
                class_dto.setIdCourses(c.getIdCourses());
            // tra ve ten khoa hoc
            if(c.getIdCourses()!=null){
                CourseEntity course =CourseRepository.findById(c.getIdCourses()).orElseThrow(()->new ResourceNotFoundException("Khóa học không tồn tại")) ;
                class_dto.setNameCourse(course.getNameCourse());
            }
            class_dto.setMaxStudents(c.getMaxStudents());
            list.add(class_dto);
        }
        return list;
    }

    @Override
    public boolean addClasses(ClassRequest classRequest) {
        // kiem tra xem co ton tai idCourse khong
        CourseEntity course_entity=CourseRepository.findById(classRequest.getIdCourses()).orElseThrow(()->new ResourceNotFoundException("Khóa học không tồn tại "));
        //Kiem tra ton tai id_teacher k
        Teacher_Entity teacherEntity=TeacherRepository.findById(classRequest.getIdTeachers()).orElseThrow(()->new RuntimeException("Giáo viên không tồn tại"));
        //kiem tra xem lop da ton tai chua
        ClassesEntity classes=classRepository.findByClassName(classRequest.getClassName());
        if(classes!=null){
            throw new DuplicateResourceException("Đã tồn tại lớp học này rồi");
        }
        ClassesEntity c=new ClassesEntity();
        c.setClassName(classRequest.getClassName());
        c.setIdCourses(classRequest.getIdCourses());
        c.setMaxStudents(classRequest.getMaxStudents());
        c.setIdTeachers(classRequest.getIdTeachers());
        c.setStatus(classRequest.getStatus());
        try{
            classRepository.save(c);
            return true;
        }
        catch(Exception e){
            System.out.println("co loi khi them class" +e.getMessage());
            return false;
        }

    }

    @Override
    public boolean updateClass(int id, ClassRequest classRequest) {
        ClassesEntity classes=classRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Lớp học không tồn tại"));
        if(classRequest.getClassName()!=null){
            classes.setClassName(classRequest.getClassName());
        }
        if(classRequest.getIdCourses()!=null){
            classes.setIdCourses(classRequest.getIdCourses());
        }
        if(classRequest.getMaxStudents()!=null){
            classes.setMaxStudents(classRequest.getMaxStudents());
        }
        if(classRequest.getIdTeachers()!=null){
            classes.setIdTeachers(classRequest.getIdTeachers());
        }
       if(classRequest.getStatus()!=null){
           classes.setStatus(classRequest.getStatus());
       }

        try{
            classRepository.save(classes);
            return true;
        }
        catch(Exception e){
            System.out.println("co loi khi sua class" +e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteClass(int id) {
        //xoa schedule truoc vi id_class dang la khoa ngoai o bang schedule
        if(!classRepository.existsById(id)){
            throw new ResourceNotFoundException("Lớp học không tồn tại");
        }
        List<Schedule_Entity> schedule =scheduleRespository.findByIdClass(id);
        if(schedule.size()>0){
            scheduleRespository.deleteAll(schedule);
        }
        //xoa idclass o bang coursestudentclass
        List<CourseStudentClassEntity> courseclass = courseStudentClassRepository.findByIdClass(id);
        if(courseclass.size()>0){
            courseStudentClassRepository.deleteAll(courseclass);
        }
        try{
            classRepository.deleteById(id);

            return true;
        }   
        catch(Exception e){
            System.out.println("co loi khi xoa class" +e.getMessage());
            return false;
        }

    }

    @Override
    public List<Class_response> getClassByCourse(int idCourse) {
        List<Class_response> listClassResponse=new ArrayList<>();
        List<ClassesEntity > listClassEntity=classRepository.findByIdCourses(idCourse);
        for(ClassesEntity c:listClassEntity){
            Class_response classresponse=new Class_response();
            classresponse.setId(c.getId());
            classresponse.setClassName(c.getClassName());
            listClassResponse.add(classresponse);
        }
        return listClassResponse;
    }
}

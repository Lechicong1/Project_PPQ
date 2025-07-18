package com.example.PPQ.Service;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.DuplicateResourceException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.ClassRequest;
import com.example.PPQ.Payload.Response.ClassDTO;
import com.example.PPQ.Service_Imp.ClassServiceImp;
import com.example.PPQ.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
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
    @Autowired
    private CourseRespository courseRespository;

    @Override
    public List<ClassDTO> getAllClasses() {
        List<ClassDTO> list = new ArrayList<ClassDTO>();
        List<ClassesEntity> listclass=classRepository.findAll();
        Set<Integer> listIdTeacher = listclass.stream().map(ClassesEntity::getIdTeachers).collect(Collectors.toSet());
        List<TeacherEntity> listTeacher=TeacherRepository.findAllByIdIn(listIdTeacher);
        Map<Integer,TeacherEntity> mapTeacher=listTeacher.stream().collect(Collectors.toMap(TeacherEntity::getId, Function.identity()));
        Set<Integer> listIdCourse = listclass.stream().map(ClassesEntity::getIdCourses).collect(Collectors.toSet());
        List<CourseEntity> listCourse=courseRespository.findAllByIdIn(listIdCourse);
        Map<Integer,CourseEntity> mapCourse=listCourse.stream().collect(Collectors.toMap(CourseEntity::getId, Function.identity()));

        for(ClassesEntity c:listclass){
            ClassDTO class_dto=new ClassDTO();
            class_dto.setId(c.getId());
            class_dto.setClassName(c.getClassName());
            class_dto.setStatus(c.getStatus());
            class_dto.setCurrentStudents(c.getCurrentStudents());
            if(c.getIdTeachers()==null)
            class_dto.setIdTeachers(-1);
            else
                class_dto.setIdTeachers(c.getIdTeachers());
            if(c.getIdTeachers()!=null){
            TeacherEntity teacher=mapTeacher.get(c.getIdTeachers());
            if(teacher==null)
              throw new ResourceNotFoundException("Giáo viên không tồn tại") ;
            class_dto.setNameTeacher(teacher.getFullName()); }
            if(c.getIdCourses()==null)
            class_dto.setIdCourses(-1);
             else
                 class_dto.setIdCourses(c.getIdCourses());
            // tra ve ten khoa hoc
            if(c.getIdCourses()!=null){
                CourseEntity course =mapCourse.get(c.getIdCourses());
                if(course==null)
                    throw new ResourceNotFoundException("Khóa học không tồn tại");
            class_dto.setNameCourse(course.getNameCourse());
            }

            class_dto.setMaxStudents(c.getMaxStudents());

            list.add(class_dto);
        }
        return list;
    }

    @Override
    public List<ClassDTO> getClassByIdTeacher() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        UserEntity user=usersRepository.findByUsername(username);
        if(user==null){
            throw new ResourceNotFoundException("Không tồn tại User");
        }
        TeacherEntity teacher=TeacherRepository.findById(user.getId()).orElseThrow(()->new ResourceNotFoundException("Giáo viên không tồn tại"));
        List<ClassesEntity> class_entity=classRepository.findByIdTeachers(teacher.getId());
        List<ClassDTO> list = new ArrayList<ClassDTO>();
        Set<Integer> listIdTeacher = class_entity.stream().map(ClassesEntity::getIdTeachers).collect(Collectors.toSet());
        List<TeacherEntity> listTeacher=TeacherRepository.findAllByIdIn(listIdTeacher);
        Map<Integer,TeacherEntity> mapTeacher=listTeacher.stream().collect(Collectors.toMap(TeacherEntity::getId, Function.identity()));
        Set<Integer> listIdCourse = class_entity.stream().map(ClassesEntity::getIdCourses).collect(Collectors.toSet());
        List<CourseEntity> listCourse=courseRespository.findAllByIdIn(listIdCourse);
        Map<Integer,CourseEntity> mapCourse=listCourse.stream().collect(Collectors.toMap(CourseEntity::getId, Function.identity()));
        for(ClassesEntity c:class_entity){
            ClassDTO class_dto=new ClassDTO();
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
                CourseEntity course =mapCourse.get(c.getIdCourses());
                if(course==null)
                    throw new ResourceNotFoundException("Khóa học không tồn tại");
                class_dto.setNameCourse(course.getNameCourse());
            }
            class_dto.setMaxStudents(c.getMaxStudents());
            list.add(class_dto);
        }
        return list;
    }

    @Override
    public void addClasses(ClassRequest classRequest) {
        // kiem tra xem co ton tai idCourse khong
        CourseEntity course_entity=CourseRepository.findById(classRequest.getIdCourses()).orElseThrow(()->new ResourceNotFoundException("Khóa học không tồn tại "));
        //Kiem tra ton tai id_teacher k
        TeacherEntity teacherEntity=TeacherRepository.findById(classRequest.getIdTeachers()).orElseThrow(()->new RuntimeException("Giáo viên không tồn tại"));
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

        classRepository.save(c);

    }

    @Override
    public void updateClass(int id, ClassRequest classRequest) {
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
       classRepository.save(classes);
    }

    @Override
    public void deleteClass(int id) {
        //xoa schedule truoc vi id_class dang la khoa ngoai o bang schedule
        if(!classRepository.existsById(id)){
            throw new ResourceNotFoundException("Lớp học không tồn tại");
        }
        List<ScheduleEntity> schedule =scheduleRespository.findByIdClass(id);
        if(schedule.size()>0){
            scheduleRespository.deleteAll(schedule);
        }
        //xoa idclass o bang coursestudentclass
        List<CourseStudentClassEntity> courseclass = courseStudentClassRepository.findByIdClass(id);
        if(courseclass.size()>0){
            courseStudentClassRepository.deleteAll(courseclass);
        }

        classRepository.deleteById(id);
    }

    @Override
    public List<ClassDTO> getClassByCourse(int idCourse) {
        List<ClassDTO> listClassResponse=new ArrayList<>();
        List<ClassesEntity > listClassEntity=classRepository.findByIdCourses(idCourse);
        for(ClassesEntity c:listClassEntity){
            ClassDTO classresponse=new ClassDTO();
            classresponse.setId(c.getId());
            classresponse.setClassName(c.getClassName());
            listClassResponse.add(classresponse);
        }
        return listClassResponse;
    }
}

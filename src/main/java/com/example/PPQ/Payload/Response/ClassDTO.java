package com.example.PPQ.Payload.Response;

import com.example.PPQ.Entity.ClassesEntity;
import com.example.PPQ.Payload.Projection_Interface.ClassView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassDTO {
    private int id;
    private String className;
    private String nameCourse;
    private String nameTeacher;
    private Integer maxStudents;
    private Integer currentStudents;
    private String status;
    private int idCourses;
    private int idTeachers;
    private String roadMap;
    public ClassDTO(ClassesEntity entity) {
        this.id = entity.getId();
        this.className = entity.getClassName();
    }
    public ClassDTO() {
    }
    public ClassDTO(ClassView classView) {
       this.id = classView.getId();
       this.className=classView.getNameClass();
       this.nameCourse=classView.getNameCourse();
       this.nameTeacher=classView.getNameTeacher();
       this.maxStudents=classView.getMaxStudents();
       this.currentStudents=classView.getCurrentStudents();
       this.status=classView.getStatus();
       this.roadMap=classView.getRoadMap();

    }

}

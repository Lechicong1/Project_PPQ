package com.example.PPQ.Payload.Response;

import com.example.PPQ.Entity.CourseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
    public class CourseDTO {
            private Integer id;
            private String nameCourse;
            private String Description;
            private BigDecimal Fee;
            private String imagePath;
            private Integer numberSessions;
            private String language;
            private  String nameClass;

    private Float score1;
    private Float score2;
    private Float score3;
    private Float totalScore;
    private Float scoreHomework;
    private Integer absentDays;
    private Integer attentedDay;
    private String result;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime enrollmentDate;
        public CourseDTO(){}
        public CourseDTO(CourseEntity entity) {
            this.id = entity.getId();
            this.nameCourse = entity.getNameCourse();
            this.Description = entity.getDescription();
            this.Fee = entity.getFee();
            this.numberSessions = entity.getNumberSessions();
            this.language = entity.getLanguage();

        }

}

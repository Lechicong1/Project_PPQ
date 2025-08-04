package com.example.PPQ.Entity;

import com.example.PPQ.Payload.Request.TeacherRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
    @Entity
    @Table(name = "Teacher")
    public class TeacherEntity {
        @Id
        private int id;
        private String fullName;
        private String phoneNumber;
        private String educationLevel;
        @Column(columnDefinition = "LONGTEXT")
        private String description;
        private Integer idUsers;
        private String email;
        private String imagePath;
        public TeacherEntity() {}
        public TeacherEntity(TeacherRequest req){
            this.fullName = req.getFullName();
            this.phoneNumber = req.getPhoneNumber();
            this.educationLevel = req.getEducationLevel();
            this.description = req.getDescription();
            this.idUsers = req.getIdUsers();
            this.email = req.getEmail();
            this.setId(req.getIdUsers());
        }

}

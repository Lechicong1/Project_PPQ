    package com.example.PPQ.Entity;

    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;

    @Getter
    @Setter
    @Entity
    @Table(name = "Student")
    public class StudentEntity {
        @Id
        private Integer id;
        private String fullName;
        private int idUsers;
        private String phoneNumber;



    }

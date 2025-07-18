    package com.example.PPQ.Entity;

    import jakarta.persistence.*;

    @Entity
    @Table(name = "Student")
    public class StudentEntity {
        @Id
        private Integer id;
        private String fullName;
        private int idUsers;
        private String phoneNumber;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public int getIdUsers() {
            return idUsers;
        }

        public void setIdUsers(int idUsers) {
            this.idUsers = idUsers;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }

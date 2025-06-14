package com.example.PPQ.Payload.Response;

import java.math.BigDecimal;

    public class Course_response {
        private Integer id;
        private String nameCourse;
        private String Description;
        private BigDecimal Fee;
        private String imagePath;
        public Integer getId() {
        return id;
    }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public void setId(Integer id) {
        this.id = id;
    }

    public String getNameCourse() {
        return nameCourse;
    }

    public void setNameCourse(String nameCourse) {
        this.nameCourse = nameCourse;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public BigDecimal getFee() {
        return Fee;
    }

    public void setFee(BigDecimal fee) {
        Fee = fee;
    }
}

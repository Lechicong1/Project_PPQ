package com.example.PPQ.Specification;

import com.example.PPQ.Entity.StudentEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
// SINH CAU WHERE THEO DIEU KIEN DONG
public class StudentSpecification {
    public static Specification<StudentEntity> search(String name, String phoneNumber) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // fullName LIKE CONCAT(name, '%')
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(root.get("fullName"), name + "%"));
            }

            // phoneNumber = :phoneNumber
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                predicates.add(cb.equal(root.get("phoneNumber"), phoneNumber));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}

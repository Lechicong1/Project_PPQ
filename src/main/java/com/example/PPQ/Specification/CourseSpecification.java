package com.example.PPQ.Specification;

import com.example.PPQ.Entity.CourseEntity;
import com.example.PPQ.Entity.StudentEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CourseSpecification {
    public static Specification<CourseEntity> search(String language) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // fullName LIKE CONCAT(name, '%')
            if (language != null && !language.isEmpty()) {
                predicates.add(cb.equal(root.get("language"), language ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

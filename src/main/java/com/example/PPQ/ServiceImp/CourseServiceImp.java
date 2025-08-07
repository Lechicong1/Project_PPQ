
package com.example.PPQ.ServiceImp;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.CourseRequest;
import com.example.PPQ.Payload.Response.CourseDTO;
import com.example.PPQ.Payload.Projection_Interface.CourseView;
import com.example.PPQ.Payload.Response.PageResponse;
import com.example.PPQ.Service.CourseService;
import com.example.PPQ.Service.FileStorageService;
import com.example.PPQ.respository.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@Transactional
public class CourseServiceImp implements CourseService {
    @Autowired
    CourseRepository courseRespository;
    @Autowired
    ClassRepository classRespository;
    @Autowired
    CourseStudentClassRepository courseStudentClassRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    StudentRepository studentRespository;
    @Autowired
    FileStorageService fileStorageService;
    @Value("${app.base-url}")
    private String baseUrl;

    private String sanitizeHtml(String html) {
        if (html == null || html.isEmpty()) {
            return "";
        }

        Safelist safelist = Safelist.relaxed()
                .addTags("div", "span", "p", "br", "hr")
                .addAttributes(":all", "style", "class", "id", "dir")  // Thêm dir cho hỗ trợ RTL/LTR
                .addProtocols("a", "href", "ftp", "http", "https", "mailto")
                .addEnforcedAttribute("a", "rel", "nofollow")  // SEO tốt hơn
                .preserveRelativeLinks(true);  // Giữ liên kết tương đối

        // Cấu hình thêm output
        Document.OutputSettings outputSettings = new Document.OutputSettings()
                .prettyPrint(false)  // Tắt in đẹp để tiết kiệm dung lượng
                .charset("UTF-8")
                .escapeMode(Entities.EscapeMode.xhtml);

        return Jsoup.clean(html, "", safelist, outputSettings);
    }
    private Sort getSortFromOption(String sortOption) {
        return switch (sortOption) {
            case "name_asc" -> Sort.by("nameCourse").ascending();
            case "name_desc" -> Sort.by("nameCourse").descending();
            case "price_asc" -> Sort.by("Fee").ascending();
            case "price_desc" -> Sort.by("Fee").descending();
            case "session_desc" -> Sort.by("numberSession").descending();
            default -> Sort.by("nameCourse").ascending();
        };
    }

    @Override
    public PageResponse<CourseDTO> getAllCourses(String language, Integer page, Integer size , String sortOption) {
        Sort sort = getSortFromOption(sortOption);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CourseEntity> coursePageEntity;
        if (language != null && !language.isEmpty()) {
            coursePageEntity = courseRespository.findByLanguage(language, pageable);
        } else {
            coursePageEntity = courseRespository.findAll(pageable);
        }

        String Url = baseUrl + "/upload/courses/";
        Page<CourseDTO> courseDTOPage = coursePageEntity.map(courseEntity -> {
               CourseDTO c= new CourseDTO(courseEntity);
               c.setImagePath(Url+courseEntity.getImagePath());
               return c;
        });
        return new PageResponse<>(courseDTOPage);
    }

    @Override
    public CourseDTO getCourseByID(int id) {
        String Url = baseUrl + "/upload/courses/";
        CourseEntity courseEntity = courseRespository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Khóa học không tồn tại "));
        CourseDTO course_dto = new CourseDTO(courseEntity);
        course_dto.setImagePath(Url + courseEntity.getImagePath());
        return course_dto;
    }

    @Override
    public void addCourse(CourseRequest courseRequest, MultipartFile file) {
        // Lưu ảnh vào thư mục
        String fileName = fileStorageService.saveFile(file, "course");
        // Làm sạch HTML
        String safeDescription = sanitizeHtml(courseRequest.getDescription());
        CourseEntity courseEntity = new CourseEntity(courseRequest);
        courseEntity.setImagePath(fileName);
        courseRespository.save(courseEntity);
    }

    @Override
    public void updateCourse(int id, CourseRequest courseRequest, MultipartFile file) {
        CourseEntity courseEntity = courseRespository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Khóa học không tồn tại "));
        if (courseRequest.getNameCourse() != null) courseEntity.setNameCourse(courseRequest.getNameCourse());

        if (courseRequest.getDescription() != null) {
            String safeDescription = sanitizeHtml(courseRequest.getDescription());
            courseEntity.setDescription(safeDescription);
        }
        if (courseRequest.getFee() != null) courseEntity.setFee(courseRequest.getFee());
        if (courseRequest.getLanguage() != null) courseEntity.setLanguage(courseRequest.getLanguage());
        if (courseRequest.getNumberSessions() != null)
            courseEntity.setNumberSessions(courseRequest.getNumberSessions());

        // Xử lý ảnh
        String imagePath = courseEntity.getImagePath(); // Giữ imagePath cũ làm mặc định
        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.saveFile(file, "course");
            imagePath = fileName; //ten file anh
        }
        if (imagePath != null) courseEntity.setImagePath(imagePath);
        courseRespository.save(courseEntity);

    }

    @Override
    public void deleteCourse(int id) {
        //xoa idcourse o bang class
        classRespository.deleteClassByIdCourses(id);
        // xoa bản ghi liên quan đến khóa học trong coursestudentclass
        courseStudentClassRepository.deleteCourseStudentClassByIdCourses(id);
        CourseEntity course_delete = courseRespository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tồn tại khóa học "));
        // Lấy tên file ảnh
        String fileName = course_delete.getImagePath();
        fileStorageService.deleteFile("teachers", fileName);
        courseRespository.deleteById(id);
    }

    @Override
    public List<String> getAllLanguages() {
        List<String> languages = courseRespository.getAllLanguages();
        return languages;
    }


    @Override
    public List<CourseDTO> getCourseByIdStudent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        StudentEntity student = studentRespository.findByUserName(username);
        if (student == null) throw new ResourceNotFoundException("Học sinh không tồn tại");
        List<CourseView> courseView = courseRespository.findCourseByIdStudent(student.getId());
        if (courseView.isEmpty()) {
            throw new ResourceNotFoundException("Bạn chưa đăng kí khóa học nào");
        }
        List<CourseDTO> course_response = new ArrayList<>();
        for (CourseView c : courseView) {
            CourseDTO courseStudent = new CourseDTO();
            courseStudent.setNameCourse(c.getNameCourse());
            courseStudent.setFee(c.getFee());
            courseStudent.setEnrollmentDate(c.getEnrollmentDate());
            courseStudent.setNameClass(c.getNameClass());
            courseStudent.setNumberSessions(c.getNumberSessions());
            Float score1 = c.getScore1() == null ? 0f : c.getScore1();
            Float score2 = c.getScore2() == null ? 0f : c.getScore2();
            Float score3 = c.getScore3() == null ? 0f : c.getScore3();
            Integer absent = c.getAbsentDays() == null ? 0 : c.getAbsentDays();
            Integer attented = c.getAttentedDay() == null ? 0 : c.getAttentedDay();
            Float scoreHomework = c.getScoreHomework() == null ? 0f : c.getScoreHomework();
            courseStudent.setScore1(score1);
            courseStudent.setScore2(score2);
            courseStudent.setScore3(score3);
            courseStudent.setScoreHomework(scoreHomework);
            courseStudent.setAbsentDays(absent);
            courseStudent.setAttentedDay(attented);
            Float totalScore = (score1 + score2 + 2 * score3 + scoreHomework) / 5f;
            courseStudent.setTotalScore(totalScore);
            if (totalScore < 6 || absent > 4) {
                courseStudent.setResult("fail");
            } else {
                courseStudent.setResult("pass");
            }
            course_response.add(courseStudent);
        }
        return course_response;
    }
}

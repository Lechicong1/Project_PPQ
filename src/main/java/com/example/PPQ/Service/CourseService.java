
package com.example.PPQ.Service;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.CourseRequest;
import com.example.PPQ.Payload.Response.CourseDTO;
import com.example.PPQ.Payload.Projection_Interface.CourseView;
import com.example.PPQ.Service_Imp.CourseServiceImp;
import com.example.PPQ.respository.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class CourseService implements CourseServiceImp {
    @Autowired
    CourseRespository courseRespository;
    @Autowired
    ClassRespository classRespository;
    @Autowired
    CourseStudentClassRepository courseStudentClassRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    StudentRespository studentRespository;
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
    @Override
    public List<CourseDTO> getAllCourses() {
        List<CourseDTO> listcourse_dto = new ArrayList<>();
        List<CourseEntity> course=courseRespository.findAll();
        if(course.isEmpty())
            throw new ResourceNotFoundException("Khóa học không tồn tại ");
        String Url =baseUrl+ "/upload/courses/";
        for(CourseEntity c:course){
            CourseDTO course_dto=new CourseDTO();
            course_dto.setId(c.getId());
            course_dto.setNameCourse(c.getNameCourse());
            course_dto.setFee(c.getFee());
            course_dto.setDescription(c.getDescription());
            course_dto.setImagePath(Url+c.getImagePath());
            course_dto.setNumberSessions(c.getNumberSessions());
            course_dto.setLanguage(c.getLanguage());
            listcourse_dto.add(course_dto);
        }

        return listcourse_dto;
    }

    @Override
    public CourseDTO getCourseByID(int id) {
        CourseDTO course_dto=new CourseDTO();
        String Url =baseUrl+ "/upload/courses/";
        CourseEntity courseEntity=courseRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("Khóa học không tồn tại "));
        course_dto.setNameCourse(courseEntity.getNameCourse());
        course_dto.setFee(courseEntity.getFee());
        course_dto.setDescription(courseEntity.getDescription());
        course_dto.setLanguage(courseEntity.getLanguage());
        course_dto.setNumberSessions(courseEntity.getNumberSessions());
        course_dto.setImagePath(Url+courseEntity.getImagePath());
        course_dto.setId(courseEntity.getId());
        return course_dto;
    }

    @Override
    public void addCourse(CourseRequest courseRequest, MultipartFile file ) {
            // Lưu ảnh vào thư mục
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("upload/courses");
            try {
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                // Làm sạch HTML
                String safeDescription = sanitizeHtml(courseRequest.getDescription());
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


                CourseEntity courseEntity = new CourseEntity();
                courseEntity.setNameCourse(courseRequest.getNameCourse());
                courseEntity.setFee(courseRequest.getFee());
                courseEntity.setDescription(safeDescription);
                courseEntity.setImagePath(fileName);
                courseEntity.setNumberSessions(courseRequest.getNumberSessions());
                courseEntity.setLanguage(courseRequest.getLanguage());
                courseRespository.save(courseEntity);
            }
              catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void updateCourse(int id, CourseRequest courseRequest,MultipartFile file) {

        try{
            CourseEntity courseEntity=courseRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("Khóa học không tồn tại ") );
            if(courseRequest.getNameCourse()!=null){
                courseEntity.setNameCourse(courseRequest.getNameCourse());
            }
            if(courseRequest.getDescription()!=null){
                String safeDescription = sanitizeHtml(courseRequest.getDescription());
                courseEntity.setDescription(safeDescription);

            }
            if(courseRequest.getFee()!=null){
                courseEntity.setFee(courseRequest.getFee());
            }
            if(courseRequest.getLanguage()!=null){
                courseEntity.setLanguage(courseRequest.getLanguage());
            }
            if(courseRequest.getNumberSessions()!=null){
                courseEntity.setNumberSessions(courseRequest.getNumberSessions());
            }
            // Xử lý ảnh
            String imagePath = courseEntity.getImagePath(); // Giữ imagePath cũ làm mặc định
            if (file != null && !file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("upload/courses");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                imagePath = fileName; //ten file anh
                courseEntity.setImagePath(imagePath);
            } else {
                // Giữ đường dẫn đầy đủ nếu có
                imagePath = courseEntity.getImagePath() != null ? "/upload/courses/" + courseEntity.getImagePath() : null;
            }
            courseRespository.save(courseEntity);


        }
        catch(Exception e){
            System.out.println("co loi khi sua course "+ e.getMessage());

        }
    }

    @Override
    public void deleteCourse(int id) {
        //xoa idcourse o bang class
        classRespository.deleteClassByIdCourses(id);
        // xoa bản ghi liên quan đến khóa học trong coursestudentclass
        courseStudentClassRepository.deleteCourseStudentClassByIdCourses(id);

        CourseEntity course_delete=courseRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("Không tồn tại khóa học "));
        // Lấy tên file ảnh
        String fileName = course_delete.getImagePath(); // ví dụ: "abc123.jpg"
        String fullPath = "upload/courses/" + fileName;
        //xoa anh
        File file = new File(fullPath);
        if (file.exists()) {
            file.delete();
        }
        courseRespository.deleteById(id);

    }

    @Override
    public List<String> getAllLanguages() {
        List<String> languages= courseRespository.getAllLanguages();
        return languages;
    }

    @Override
    public List<CourseDTO> getAllCoursesByLanguage(String language) {
        List<CourseDTO> listcourse_dto = new ArrayList<>();
        List<CourseEntity> course=courseRespository.findByLanguage(language);
        if(course.isEmpty())
            throw new ResourceNotFoundException("Khóa học không tồn tại ");
        String Url =baseUrl+ "/upload/courses/";
        for(CourseEntity c:course){
            CourseDTO course_dto=new CourseDTO();
            course_dto.setId(c.getId());
            course_dto.setNameCourse(c.getNameCourse());
            course_dto.setFee(c.getFee());
            course_dto.setDescription(c.getDescription());
            course_dto.setImagePath(Url+c.getImagePath());
            course_dto.setNumberSessions(c.getNumberSessions());
            course_dto.setLanguage(c.getLanguage());
            listcourse_dto.add(course_dto);
        }

        return listcourse_dto;
    }

    @Override
    public List<CourseDTO> getCourseByIdStudent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        StudentEntity student = studentRespository.findByUserName(username);
        if(student==null) throw new ResourceNotFoundException("Học sinh không tồn tại");
        List<CourseView> courseView=courseRespository.findCourseByIdStudent(student.getId());
        if(courseView.isEmpty()){
            throw new ResourceNotFoundException("Bạn chưa đăng kí khóa học nào");
        }
        List<CourseDTO> course_response=new ArrayList<>();
        for(CourseView c:courseView){
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
            Float totalScore = ( score1 + score2 + 2*score3 + scoreHomework) / 5f;
            courseStudent.setTotalScore(totalScore);
            if(totalScore < 6  || absent > 4  ){
                courseStudent.setResult("fail");
            }
            else{
                courseStudent.setResult("pass");
            }
            course_response.add(courseStudent);
        }
        return course_response;
    }
}

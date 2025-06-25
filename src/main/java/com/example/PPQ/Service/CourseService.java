
package com.example.PPQ.Service;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.CourseRequest;
import com.example.PPQ.Payload.Response.Course_response;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
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
    public List<Course_response> getAllCourses() {
        List<Course_response> listcourse_dto = new ArrayList<>();
        List<CourseEntity> course=courseRespository.findAll();
        if(course.isEmpty())
            throw new ResourceNotFoundException("Khóa học không tồn tại ");
        String Url =baseUrl+ "/upload/courses/";
        for(CourseEntity c:course){
            Course_response course_dto=new Course_response();
            course_dto.setId(c.getID());
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
    public Course_response getCourseByID(int id) {
        Course_response course_dto=new Course_response();
        String Url =baseUrl+ "/upload/courses/";
        CourseEntity courseEntity=courseRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("Khóa học không tồn tại "));
        course_dto.setNameCourse(courseEntity.getNameCourse());
        course_dto.setFee(courseEntity.getFee());
        course_dto.setDescription(courseEntity.getDescription());
        course_dto.setLanguage(courseEntity.getLanguage());
        course_dto.setNumberSessions(courseEntity.getNumberSessions());
        course_dto.setImagePath(Url+courseEntity.getImagePath());
        course_dto.setId(courseEntity.getID());
        return course_dto;
    }

    @Override
    public boolean addCourse(CourseRequest courseRequest, MultipartFile file ) {

        try{
            // Lưu ảnh vào thư mục
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("upload/courses");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // Làm sạch HTML
            String safeDescription = sanitizeHtml(courseRequest.getDescription());
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            CourseEntity courseEntity=new CourseEntity();
            courseEntity.setNameCourse(courseRequest.getNameCourse());
            courseEntity.setFee(courseRequest.getFee());
            courseEntity.setDescription(safeDescription);
            courseEntity.setImagePath(fileName);
            courseEntity.setNumberSessions(courseRequest.getNumberSessions());
            courseEntity.setLanguage(courseRequest.getLanguage());

            courseRespository.save(courseEntity);
            return true;
        }
        catch(Exception e){
            System.out.println("co loi khi them course "+ e.getMessage());
            return false;
        }

    }

    @Override
    public boolean updateCourse(int id, CourseRequest courseRequest,MultipartFile file) {

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
            return true;

        }
        catch(Exception e){
            System.out.println("co loi khi sua course "+ e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteCourse(int id) {
        //xoa idcourse o bang class
        List<ClassesEntity> classesEntity=classRespository.findByIdCourses(id);
        if(!classesEntity.isEmpty()) {
            classRespository.deleteAll(classesEntity);
        }
        // xoa bản ghi liên quan đến khóa học trong coursestudentclass
        List<CourseStudentClassEntity> courestudentclass =courseStudentClassRepository.findByIdCourse(id);
        if(!courestudentclass.isEmpty()) {
            courseStudentClassRepository.deleteAll(courestudentclass);
        }

        CourseEntity course_delete=courseRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("Không tồn tại khóa học "));
        // Lấy tên file ảnh
        String fileName = course_delete.getImagePath(); // ví dụ: "abc123.jpg"
        String fullPath = "upload/courses/" + fileName;
        //xoa anh
        File file = new File(fullPath);
        if (file.exists()) {
            file.delete();
        }
        try{
            courseRespository.deleteById(id);
            return true;
        }
        catch(Exception e){
            System.out.println("co loi khi xoa course "+ e.getMessage());
            return false;
        }

    }

    @Override
    public List<String> getAllLanguages() {
        List<String> languages= courseRespository.getAllLanguages();

        return languages;
    }

    @Override
    public List<Course_response> getAllCoursesByLanguage(String language) {
        List<Course_response> listcourse_dto = new ArrayList<>();
        List<CourseEntity> course=courseRespository.findByLanguage(language);
        if(course.isEmpty())
            throw new ResourceNotFoundException("Khóa học không tồn tại ");
        String Url =baseUrl+ "/upload/courses/";
        for(CourseEntity c:course){
            Course_response course_dto=new Course_response();
            course_dto.setId(c.getID());
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
    public List<Course_response> getCourseByIdStudent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        User_Entity user=usersRepository.findByUsername(username);
        if(user==null){
            throw new ResourceNotFoundException("Không tồn tại User");
        }
        Student_Entity student = studentRespository.findById(user.getId()).orElseThrow(()->new ResourceNotFoundException("Học sinh không tồn tại")) ;
        List<CourseStudentClassEntity> courseStudentClassEntities=courseStudentClassRepository.findByIdStudent(student.getId());
        if(courseStudentClassEntities==null){
            throw new ResourceNotFoundException("Khóa học này chưa có sinh viên đăng kí ");
        }
        List<Course_response> course_response=new ArrayList<>();
        for(CourseStudentClassEntity c:courseStudentClassEntities){
            Course_response courseStudent = new Course_response();
            CourseEntity course =courseRespository.findById(c.getIdCourse()).orElseThrow(()->new ResourceNotFoundException("Không tồn tại khóa học "));
            courseStudent.setId(course.getID());
            courseStudent.setNameCourse(course.getNameCourse());
            courseStudent.setFee(course.getFee());
            courseStudent.setEnrollmentDate(c.getEnrollmentDate());
            ClassesEntity classes = classRespository.findById(c.getIdClass()).orElseThrow(()->new ResourceNotFoundException("Không tồn tại lớp học "));
            courseStudent.setNameClass(classes.getClassName());
            courseStudent.setNumberSessions(course.getNumberSessions());
            course_response.add(courseStudent);

        }
        return course_response;
    }
}

package com.example.PPQ.Service;

import com.example.PPQ.Entity.ClassesEntity;
import com.example.PPQ.Entity.CourseEntity;
import com.example.PPQ.Entity.CourseStudentClassEntity;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.CourseRequest;
import com.example.PPQ.Payload.Response.Course_response;
import com.example.PPQ.Service_Imp.CourseServiceImp;
import com.example.PPQ.respository.ClassRespository;
import com.example.PPQ.respository.CourseRespository;
import com.example.PPQ.respository.CourseStudentClassRepository;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${app.base-url}")
    private String baseUrl;
    private String sanitizeHtml(String html) {
        Whitelist whitelist = Whitelist.relaxed()
                .addTags("div", "span", "p", "br", "hr")
                .addAttributes(":all", "style", "class", "id")
                .addProtocols("a", "href", "ftp", "http", "https", "mailto");

        return Jsoup.clean(html, whitelist);
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
            listcourse_dto.add(course_dto);
        }

        return listcourse_dto;
    }

    @Override
    public Course_response getCourseByID(int id) {
        Course_response course_dto=new Course_response();
        CourseEntity courseEntity=courseRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("Khóa học không tồn tại "));
        course_dto.setNameCourse(courseEntity.getNameCourse());
        course_dto.setFee(courseEntity.getFee());
        course_dto.setDescription(courseEntity.getDescription());
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
}

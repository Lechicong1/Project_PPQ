package com.example.PPQ.ServiceImp;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Enums.Role;
import com.example.PPQ.Exception.DuplicateResourceException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Projection_Interface.paymentView;
import com.example.PPQ.Payload.Request.PaymentRequest;
import com.example.PPQ.Payload.Response.PaymentRespone;
import com.example.PPQ.Payload.Response.QRRespone;
import com.example.PPQ.Util.VietQRUtil;
import com.example.PPQ.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentServiceImp implements com.example.PPQ.Service.PaymentService {
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    PaymentRepository paymentRespository;
    @Autowired
    CourseRepository courseRespository;
    @Autowired
    StudentRepository studentRespository;
    @Autowired
    ClassRepository classRespository;
    @Autowired
    CourseStudentClassRepository courseStudentClassRepository;
    @Autowired
    RoleRepository rolesRespository;
    @Value("${payment.bank-code}")
    private String bankCode;

    @Value("${payment.account-number}")
    private String accountNumber;

    @Value("${payment.account-name}")
    private String accountName;
    @Override
    public Integer createPayment(PaymentRequest paymentRequest,int idCourse) {
        // Lấy user từ token hiện tại
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserEntity user = usersRepository.findByUsername(username);
        if (user == null) throw new ResourceNotFoundException("User không tồn tại");
        String qrContent = "PPQ" + user.getId()+idCourse;
        CourseEntity course = courseRespository.findById(idCourse).orElseThrow(()->new ResourceNotFoundException("Khóa học không tồn tại"));
        // Lưu payment
        PaymentEntity payment ;
        PaymentEntity existing = paymentRespository.findTopByUserIdAndCourseIdOrderByCreatedAtDesc(user.getId(), idCourse);
        if (existing != null
                && existing.getStatus().equals("Chờ xác nhận")
                && existing.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(5))) {
            throw new DuplicateResourceException("Bạn vừa đăng ký khóa học này, vui lòng chờ 5 phút để gửi lại yêu cầu.");
        }
        else {
            payment=new PaymentEntity();
            payment.setUserId(user.getId());
            payment.setCourseId(course.getId());
            payment.setClassId(paymentRequest.getClassId());
            payment.setFullName(paymentRequest.getFullName());
            payment.setPhoneNumber(paymentRequest.getPhoneNumber());
            payment.setAmount(course.getFee());
            payment.setQrContent(qrContent);
            payment.setCreatedAt(LocalDateTime.now());
            payment.setStatus("Chờ xác nhận");
            try {
                PaymentEntity saved = paymentRespository.save(payment);
                return saved.getId();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }


    }
    public QRRespone generateQr(int paymentId) {
        PaymentEntity payment = paymentRespository.findById(paymentId).orElseThrow(()->new ResourceNotFoundException("Không tồn tại bản thanh toán"));
        // Tạo QR
        String qrUrl = VietQRUtil.generateVietQRUrl(
                bankCode, accountNumber, accountName,
                payment.getAmount(), payment.getQrContent()
        );

        QRRespone response = new QRRespone();
        response.setQrUrl(qrUrl);
        response.setQrContent(payment.getQrContent());
        response.setAmount(payment.getAmount());
        response.setBankCode(bankCode);
        response.setBankName("MBBank"); // hardcode hoặc lấy từ map bankCode → tên
        response.setAccountNumber(accountNumber);
        response.setAccountName(accountName);
        return response;
    }

    @Override
    public void confirmPayment(PaymentRequest paymentRequest,int paymentId) {
        // kiem tra xem student da ton tai chua (vi du user a muon dang ki them 1 khoa hoc thi khong can tao moi 1 student nua )
        StudentEntity student_Entity = studentRespository.findByIdUsers(paymentRequest.getUserId());
        //kiem tra xem lop hoc co ton tai hay khong
        ClassesEntity classes = classRespository.findById(paymentRequest.getClassId()).orElseThrow(()->new ResourceNotFoundException("Lớp học không tồn tại"));
        // kiem tra xem khoa hoc co ton tai khong
        CourseEntity course =courseRespository.findById(paymentRequest.getCourseId()).orElseThrow(()->new ResourceNotFoundException("Khóa học không tồn tại "));
        CourseStudentClassEntity course_StudentEntity = new CourseStudentClassEntity();
        if(student_Entity==null){ // them thong tin vao bang student
            student_Entity=new StudentEntity();
            student_Entity.setFullName(paymentRequest.getFullName());
            student_Entity.setPhoneNumber(paymentRequest.getPhoneNumber());
            student_Entity.setIdUsers(paymentRequest.getUserId());
            student_Entity.setId(paymentRequest.getUserId());
        }
        // luu thong tin vao bang chung
        course_StudentEntity.setIdCourse(paymentRequest.getCourseId());
        course_StudentEntity.setIdStudent(paymentRequest.getUserId());
        course_StudentEntity.setEnrollmentDate(LocalDateTime.now());
        course_StudentEntity.setIdClass(paymentRequest.getClassId());
        // thay doi status trong payment
        PaymentEntity payment=paymentRespository.findById(paymentId).orElseThrow(()->new ResourceNotFoundException("Bản thanh toán không tồn tại"));
        payment.setStatus("Đã thanh toán");
            studentRespository.save(student_Entity);
            courseStudentClassRepository.save(course_StudentEntity);
            // sau khi dang ki khoa hoc thanh cong set role cua user do thanh student
            RolesEntity roleStudent=rolesRespository.findByRoleName(Role.STUDENT);
            UserEntity user= usersRepository.findById(paymentRequest.getUserId()).orElseThrow(()-> new ResourceNotFoundException("Không tồn tại user"));
            user.setIdRoles(roleStudent.getId());
            usersRepository.save(user);
            paymentRespository.save(payment);
            classes.setCurrentStudents(classes.getCurrentStudents()+1);
            classRespository.save(classes);
    }

    @Override
    public List<PaymentRespone> getAllPayments() {
        List<paymentView> listPayment = paymentRespository.findAllPayment();


        return listPayment.stream()
                .map(c->new PaymentRespone(c))
                .collect(Collectors.toList());
    }
    // tu dong xóa bản ghi chờ xác nhận quá thời hạn
    @Scheduled(cron = "0 0 2 * * *") // chạy mỗi ngày lúc 2h sáng
    public void deleteOldUnconfirmedPayments() {
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(2);
        List<PaymentEntity> expiredPayments = paymentRespository
                .findByStatusAndCreatedAtBefore("Chờ xác nhận", expiredTime);

        if (!expiredPayments.isEmpty()) {
            paymentRespository.deleteAll(expiredPayments);
        }
    }

}




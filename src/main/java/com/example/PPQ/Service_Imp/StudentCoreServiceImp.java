package com.example.PPQ.Service_Imp;

import com.example.PPQ.Payload.Request.AttendanceRequest;
import com.example.PPQ.Payload.Request.StudentCoreRequest;
import com.example.PPQ.Payload.Response.StudentCoreDTO;

import java.util.List;

public interface StudentCoreServiceImp {
    List<StudentCoreDTO> findCoreByStudentAndClass(Integer studentId, Integer classId);
    void updateCoreStudent( Integer idStudent,Integer idClass, StudentCoreRequest studentCoreRequest);
    void updateAbsentStudent(List<AttendanceRequest> attendanceRequest);
}

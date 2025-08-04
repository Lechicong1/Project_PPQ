package com.example.PPQ.Service;

import com.example.PPQ.Payload.Request.AttendanceRequest;
import com.example.PPQ.Payload.Request.StudentCoreRequest;
import com.example.PPQ.Payload.Response.StudentCoreDTO;

import java.util.List;

public interface StudentCoreService {

    void updateCoreStudent( Integer idStudent,Integer idClass, StudentCoreRequest studentCoreRequest);
    void updateAbsentStudent(List<AttendanceRequest> attendanceRequest);
}

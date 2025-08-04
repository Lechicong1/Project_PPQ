package com.example.PPQ.Payload.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceRequest {
        private Integer idStudent;
    private Integer idClass ;
    private String status;


}

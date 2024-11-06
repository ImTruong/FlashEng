package com.education.flashEng.payload.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetDTO {
    private String name;
    private String description;
    private Long classId;
    private String privacyStatus;
}

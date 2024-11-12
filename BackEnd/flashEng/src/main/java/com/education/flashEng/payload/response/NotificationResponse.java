package com.education.flashEng.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponse {
    private String message;
    private String type;
    private Boolean isRead;
    private Map<String, String> additionalInfo;

}

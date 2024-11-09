package com.education.flashEng.payload.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassInvitationResponse {

    private ClassInformationResponse classInformationResponse;

    private String inviterName;

    private Long invitationId;

}
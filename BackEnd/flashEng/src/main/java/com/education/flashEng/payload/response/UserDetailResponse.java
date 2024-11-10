package com.education.flashEng.payload.response;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailResponse {

    private Long id;

    private String fullName;

    private String username;

    private String email;

    private String country;

}

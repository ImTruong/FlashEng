package com.education.flashEng.payload.response;

import com.education.flashEng.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetResponse {
    private Long id;
    private String name;
    private String description;
    private String privacyStatus;
    private Long numberOfWords;
    private UserDetailResponse userDetailResponse;

    public void setUserDetailResponse(String fullName, String username, String email, String country) {
        userDetailResponse = new UserDetailResponse(fullName, username, email, country);
    }
}

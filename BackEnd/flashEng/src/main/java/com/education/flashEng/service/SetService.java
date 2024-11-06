package com.education.flashEng.service;

import com.education.flashEng.entity.SetEntity;
import com.education.flashEng.payload.DTO.SetDTO;
import com.education.flashEng.payload.request.CreateSetRequest;

import java.util.List;

public interface SetService {
    public boolean createSet(CreateSetRequest createSetRequest);
    public List<SetEntity> getPublicSet();
}

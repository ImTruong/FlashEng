package com.education.flashEng.service;

import com.education.flashEng.entity.SetEntity;
import com.education.flashEng.payload.request.CreateSetRequest;
import com.education.flashEng.payload.request.UpdateSetRequest;
import com.education.flashEng.payload.response.SetResponse;

import java.util.List;

public interface SetService {
    public boolean createSet(CreateSetRequest createSetRequest);
    public List<SetResponse> getPublicSet();
    public List<SetResponse> getPrivateSet();
    public List<SetResponse> getSetByClassID(Long classID);
    public boolean updateSet(UpdateSetRequest updateSetRequest);
    public boolean deleteSetById(Long setID);
}

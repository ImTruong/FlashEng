package com.education.flashEng.service;

import com.education.flashEng.payload.request.CreateWordRequest;
import com.education.flashEng.payload.request.UpdateWordRequest;
import com.education.flashEng.payload.response.WordResponse;
import com.education.flashEng.payload.response.WordResponse;
import com.education.flashEng.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface WordService {
    public WordResponse createWord(CreateWordRequest createWordRequest);
    public List<WordResponse> getWordBySetId(Long setId);
    public boolean updateWord(UpdateWordRequest updateWordRequest);
    public boolean deleteWordById(Long wordId);
}

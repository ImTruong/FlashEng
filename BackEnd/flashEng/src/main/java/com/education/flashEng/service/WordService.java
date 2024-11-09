package com.education.flashEng.service;

import com.education.flashEng.payload.request.CreateWordRequest;
import com.education.flashEng.payload.request.UpdateWordRequest;
import com.education.flashEng.payload.response.WordListResponse;
import com.education.flashEng.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface WordService {
    public boolean createWord(CreateWordRequest createWordRequest);
    public List<WordListResponse> getWordBySetId(Long setId);
    public boolean updateWord(UpdateWordRequest updateWordRequest);
    public boolean deleteWordById(Long wordId);
}

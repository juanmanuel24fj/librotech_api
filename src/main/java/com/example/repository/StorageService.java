package com.example.repository;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Repository
public interface StorageService {
    void init() throws IOException;
    String store(MultipartFile file);
    Resource loadAsResource(String filename);
}

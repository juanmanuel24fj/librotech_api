package com.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.repository.StorageService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.apache.tomcat.util.http.RequestUtil.normalize;
@Service
public class FileServiceStorageService implements StorageService {
    @Value("${media.location}")
    private String mediaLocation;

    private Path rootLocation;

    @Override
    @PostConstruct
    public void init() throws IOException {
rootLocation=Paths.get(mediaLocation);
Files.createDirectories(rootLocation);
    }

    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to");
        }
        String filename = file.getOriginalFilename();
        Path destinationFile = rootLocation.resolve(Paths.get(filename))
                .normalize().toAbsolutePath();
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return filename;
    }


    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource((file.toUri()));
            if(resource.exists()|| resource.isReadable()){
                return resource;
            }else {
                throw new RuntimeException("Could not read file:"+ filename);

            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}

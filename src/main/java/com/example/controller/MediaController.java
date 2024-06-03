package com.example.controller;


import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.repository.StorageService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@RestController
@RequestMapping("media")
@AllArgsConstructor
@CrossOrigin(origins = "https://librotech.vercel.app")

public class MediaController {
	private final StorageService storageService = null;
	private final HttpServletRequest request = null;
	
	@PostMapping("upload")
	public Map<String,String> uploadFile(@RequestParam("file") MultipartFile multipartFile){
	    String path = storageService.store(multipartFile);
	    String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
	    String url = ServletUriComponentsBuilder
	            .fromHttpUrl(host)
	            .path("/media/")
	            .path(path)
	            .toUriString();
	    return Map.of("url", url);
	}
	
    @GetMapping("{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
        Resource file = storageService.loadAsResource(filename);
        String contentType = Files.probeContentType(file.getFile().toPath());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(file);
    }

}



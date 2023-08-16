package com.xcally.ars.domain.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Component
//@RequiredArgsConstructor
public class S3Uploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    private S3Client s3; 
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public String uploadFiles(MultipartFile multipartFile, String dirName) throws IOException {
    	
    	try {
            File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                    .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));
            return upload(uploadFile, dirName);
    	}catch (Exception e) {
			return "Fail Convert"+ExceptionUtils.getPrintStackTrace(e);
		}    	
    }

    public String upload(File uploadFile, String filePath) {
    	
    	try {
        	String originalFileName = uploadFile.getName(); // 원본 파일 이름
        	String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 파일 확장자
            String fileName = filePath + "/" + UUID.randomUUID()+extension;   // S3에 저장된 파일 이름
            String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
            if(uploadImageUrl.contains("Fail")) {             	
            	return "Fail: "+uploadFile.getAbsolutePath();
            }   
            removeNewFile(uploadFile);
            return uploadImageUrl;
    	}catch (Exception e) {
			return "Fail Upload"+ExceptionUtils.getPrintStackTrace(e);
		}    	

    }

    private String putS3(File uploadFile, String fileName) {
        try  {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();
            s3.putObject(putObjectRequest, RequestBody.fromFile(uploadFile));
            return s3.utilities().getUrl(builder -> builder.bucket(bucket).key(fileName)).toExternalForm();
        } catch (Exception e) {
            return "Fail Upload S3" + ExceptionUtils.getPrintStackTrace(e);
        }
    }
    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {    	
    	try {
            if (targetFile.delete()) {
                logger.info("File delete success");
                return;
            }
            logger.error("File delete fail");
    	}catch (Exception e) {
    		logger.error("Fail removeFile" + ExceptionUtils.getPrintStackTrace(e));
		}

    }

    // 로컬에 파일 업로드 하기
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
    
}
package com.klaatus.mall.utils;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${com.klaatus.upload.path}")
    private  String uploadPath;

    /**
     * 프로젝트 실행시 무조건 실행
     */
    @PostConstruct
    public void init() {
        File temFolder = new File(uploadPath);
        if(!temFolder.exists()){
            temFolder.mkdirs();
        }
        uploadPath =temFolder.getAbsolutePath();
        log.info("******파일 업로드 경로:{}", uploadPath);

    }

    /**
     * 이미지 파일 저장및 썸네일 이미지 생성
     * @param files
     * @return 이미지 파일명 리스트 리턴
     * @throws IOException
     */
    public List<String> saveFiles(List<MultipartFile> files) throws IOException {

        if(files==null || files.isEmpty()){
            return null;
        }

        List<String> uploadNames = new ArrayList<>();

        files.forEach(uploadFile->{
            String savedName = UUID.randomUUID().toString()+"_"+uploadFile.getOriginalFilename();
            Path savePath = Paths.get(uploadPath,savedName);
            try{
                Files.copy(uploadFile.getInputStream(), savePath);

                //썸네일 생성
                String contentType = uploadFile.getContentType();
                if(contentType!=null && contentType.startsWith("image")){
                    Path thumbPath = Paths.get(uploadPath,"s_"+savedName);
                    Thumbnails.of(savePath.toFile()).size(400,400).toFile(thumbPath.toFile());
                }
                uploadNames.add(savedName);
            }catch(IOException e){
                throw  new RuntimeException(e.getMessage());
            }
        });

        return uploadNames;
    }
    /**
     * 업로드 이미지 가져 뷰에서 보기
     * @param fileName
     * @return
     */
    public ResponseEntity<Resource> getFile(String fileName){
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);

        if(!resource.exists()){
            resource = new FileSystemResource(uploadPath+File.separator+"default,jpg");
        }

        HttpHeaders httpHeaders = new HttpHeaders();

        try{
            httpHeaders.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(httpHeaders).body(resource);
    }
    /**
     * 사용자가 삭제한 파일 리스트 물리적으로 삭제(원본이미지, 썸네일 이미지)
     * @param fileNames
     */
    public void deleteFile(List<String> fileNames){

        if(fileNames==null || fileNames.isEmpty()){
            return;
        }

        fileNames.forEach(fileName->{

            String thumbNailFileName = "s_"+fileName;
            Path thumbNailPath = Paths.get(uploadPath,thumbNailFileName);
            Path filePath = Paths.get(uploadPath,fileName);

            try{
                Files.deleteIfExists(filePath);
                Files.deleteIfExists(thumbNailPath);
            }catch (IOException e){
                throw  new RuntimeException(e.getMessage());
            }
        });

    }

}

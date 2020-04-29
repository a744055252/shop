package com.juan.shop.file;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * @author liguanhuan
 */
public interface IFileService {

    long upload(MultipartFile file);

    Optional<UploadFile> findById(Long id);

}

package com.juan.shop.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UploeaFileRepository extends JpaRepository<UploadFile, Long> {

    /**
     * 通过文件的服务器名字获取上传文件对象
     * @param realName 服务器名字{@link UploadFile#getRealName()}
     * @return 文件对象的Optional对象
     */
    Optional<UploadFile> findByRealName(String realName);

}

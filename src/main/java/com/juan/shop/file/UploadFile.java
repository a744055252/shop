package com.juan.shop.file;

import com.juan.shop.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author liguanhuan
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class UploadFile extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** 文件上传名字 */
    private String fileName;

    /** 文件服务器名字 */
    private String realName;
    
    /** 服务器存储位置 */
    private String path;

    /** web图片地址 */
    private String src;
}

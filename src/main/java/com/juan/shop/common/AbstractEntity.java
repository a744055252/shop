package com.juan.shop.common;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 基础实体类
 * @author liguanhuan
 */
@Data
@MappedSuperclass
public class AbstractEntity {

    /** 创建时间 */
    private Date createDate;

    /** 更新时间 */
    private Date updateDate;

    /** 备注 */
    private String remark;

}

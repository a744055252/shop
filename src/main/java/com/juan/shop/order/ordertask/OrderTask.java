package com.juan.shop.order.ordertask;

import com.juan.shop.common.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author guanhuan_li
 */
@Data
@Entity
public class OrderTask extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 执行时间(精确到分) */
    private Date exeDate;

    /** 是否成功 */
    private Boolean flag;

}

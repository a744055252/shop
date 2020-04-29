package com.juan.shop.order;

import com.juan.shop.common.AbstractEntity;
import com.juan.shop.three.jd.Platform;
import com.juan.shop.user.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 订单表
 * @author guanhuan_li
 */
@Data
@Entity
@Table(name = "SOrder")
public class Order extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 平台订单id */
    private Long paltformId;

    /** 订单所属平台 */
    private Platform platform;

    /** 订单所有人 {@link User#getId()} */
    private Long userId;

    /** 订单完成时间 */
    private Date finishDate;

    /** 下单设备(1:PC,2:无线) */
    private Integer orderEmt;

    /** 下单时间 */
    private Date orderDate;

    /** 父订单id */
    private Long parentId;
    
    /** 订单维度预估结算时间 yyyyMMdd */
    private Date payMonth;

    /** 是否是会员下单 */
    private Boolean flag;

    /** 推客的联盟ID */
    private Long unionId;
    
    /** 订单维度的推客生成推广链接时传入的扩展字段，不建议使用，可以用订单行sku维度ext1参考 */
    private String ext1;

    /** 订单维度的有效码，不建议使用 */
    private int validCode;
}

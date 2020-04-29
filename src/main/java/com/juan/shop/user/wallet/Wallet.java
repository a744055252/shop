package com.juan.shop.user.wallet;

import com.juan.shop.common.AbstractEntity;
import com.juan.shop.user.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author guanhuan_li
 */
@Entity
@Data
public class Wallet extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 用户id {@link User#getId()} */
    private Long userId;

    /** 预估金额 */
    private Double estimate;

    /** 实际金额 */
    private Double actual;

}

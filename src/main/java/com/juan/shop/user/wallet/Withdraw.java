package com.juan.shop.user.wallet;

import com.juan.shop.common.AbstractEntity;
import com.juan.shop.user.User;
import com.juan.shop.user.wallet.model.WithdrawStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 提现
 *
 * @author guanhuan_li
 */
@Data
@Entity
public class Withdraw extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 提现状态 */
    private WithdrawStatus status;

    /** 提现金额 */
    private Double number;

    /** 提现日期 */
    private Date withdrawDate;

    /** 提现人 {@link User#getId()} */
    private Long userId;
}

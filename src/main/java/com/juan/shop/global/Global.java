package com.juan.shop.global;

import com.juan.shop.common.AbstractEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

/**
 * 存放系统的配置
 * @author guanhuan_li
 */
@Data
@Entity
public class Global extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 键 */
    @Enumerated(EnumType.STRING)
    private GlobalKey globalKey;

    /** 值 */
    @Lob
    private String globalValue;
}

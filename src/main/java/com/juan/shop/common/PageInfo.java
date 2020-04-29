package com.juan.shop.common;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.PageRequest;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * @author guanhuan_li
 */
@Data
public class PageInfo {
    /** 第几页 */
    @Min(0)
    private Integer pageNumber;
    /** 页面大小 */
    @Min(1)
    private Integer pageSize;

    public PageRequest toPageabel() {
        return PageRequest.of(this.pageNumber, this.pageSize);
    }
}

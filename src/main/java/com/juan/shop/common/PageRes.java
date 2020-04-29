
package com.juan.shop.common;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author liguanhuan
 */
@Data
public class PageRes<T> {
    private int currPage;
    private int pageSize;
    private int totalPage;
    private int totalSize;
    private List<T> dataList;

    public PageRes() {
    }

    public static <T> PageRes<T> valueOf(int totalSize, int pageSize, int currPage, List<T> dataList) {
        PageRes<T> result = new PageRes<>();
        int totalPage = totalSize / pageSize;
        if (totalSize == 0) {
            currPage = 0;
        } else if (totalSize % pageSize != 0) {
            ++totalPage;
        }

        if (totalPage < currPage) {
            currPage = totalPage;
        }

        result.setTotalSize(totalSize);
        result.setPageSize(pageSize);
        result.setCurrPage(currPage);
        result.setTotalPage(totalPage);
        result.setDataList(dataList);
        return result;
    }

    public static <T> PageRes<T> valueOf(Page<?> page, List<T> dataList) {
        return valueOf(page.getNumberOfElements(), page.getSize(), page.getNumber(), dataList);
    }

}

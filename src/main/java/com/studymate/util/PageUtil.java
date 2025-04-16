package com.studymate.util;

import org.springframework.stereotype.Component;

@Component
public class PageUtil {

    public int validatePageNumber(Integer pageNumber) {
        if (pageNumber == null || pageNumber < 1) {
            return 1;
        }
        return pageNumber;
    }

    public int validatePageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            return 10;
        }
        return pageSize;
    }

}

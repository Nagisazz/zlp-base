package com.nagisazz.base.config.page;

import com.nagisazz.base.util.ServletUtil;

/**
 * 表格数据处理
 */
public class PageSupport {

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 分页参数合理化
     */
    public static final String REASONABLE = "reasonable";

    /**
     * 封装分页对象
     */
    public static PageParam buildPageRequest() {
        PageParam pageParam = new PageParam();
        pageParam.setPageNum(ServletUtil.getParameterToInt(PAGE_NUM, 1));
        pageParam.setPageSize(ServletUtil.getParameterToInt(PAGE_SIZE, 10));
        pageParam.setOrderByColumn(ServletUtil.getParameter(ORDER_BY_COLUMN));
        pageParam.setIsAsc(ServletUtil.getParameter(IS_ASC));
        pageParam.setReasonable(ServletUtil.getParameterToBool(REASONABLE));
        return pageParam;
    }
}

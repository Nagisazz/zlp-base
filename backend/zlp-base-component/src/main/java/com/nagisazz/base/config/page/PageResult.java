package com.nagisazz.base.config.page;

import com.github.pagehelper.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 表格分页数据对象
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总数据条数
     */
    private Long totalRecords;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 分页数据
     */
    private List<T> datas = new ArrayList<>();

    /**
     * 构造方法
     */
    public PageResult() {
    }

    /**
     * 构造方法
     *
     * @param totalRecords 总笔数
     * @param totalPages   总页数
     * @param pageNum      页码
     * @param pageSize     每页笔数
     * @param datas        分页数据
     */
    public PageResult(Long totalRecords, Integer totalPages, Integer pageNum, Integer pageSize, List<T> datas) {
        this.totalRecords = totalRecords;
        this.totalPages = totalPages;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.datas = datas;
    }

    /**
     * 构造方法
     *
     * @param list 泛型列表
     */
    public PageResult(List<T> list) {
        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            this.totalRecords = page.getTotal();
            this.totalPages = page.getPages();
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.datas = page.getResult();
        } else {
            this.totalRecords = (long) list.size();
            this.totalPages = this.totalRecords > 0 ? 1 : 0;
            this.pageNum = this.totalRecords > 0 ? 1 : 0;
            this.pageSize = list.size();
            this.datas = list;
        }
    }
}
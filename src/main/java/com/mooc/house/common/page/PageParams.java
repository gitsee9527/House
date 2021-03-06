package com.mooc.house.common.page;


/**
 * 将pagesize 和 pagenum转化成 offset 和 limit
 * @author 77165
 *
 */
public class PageParams {
	private static final Integer PAGE_SIZE = 2;
	private Integer pageSize;
	private Integer pageNum;
	private Integer offset;
	private Integer limit;
	
	public static PageParams build(Integer pageSize,Integer pageNum) {
		if(pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		if(pageNum == null) {
			pageNum = 1;
		}
		return new PageParams(pageSize,pageNum);
	}
	
	public PageParams() {
		this(PAGE_SIZE,1);
	}
	
	public PageParams(Integer pageSize,Integer pageNum) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.offset = pageSize*(pageNum-1);
		this.limit = pageSize;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	@Override
	public String toString() {
		return "PageParams [pageSize=" + pageSize + ", pageNum=" + pageNum + ", offset=" + offset + ", limit=" + limit
				+ "]";
	}
	
}

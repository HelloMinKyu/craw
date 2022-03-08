package util;

import org.springframework.data.domain.Page;

import java.util.List;

public class PagingInfo {
    private int beginPage;
    private int endPage;
    private int curPage;
    private List items;
    private int totalPage;
    private long totalItems;
    private String category;
    private String value;
    private String pathParam = null;
    private int showNum;
    private int pagingSize = 10;

    public PagingInfo() {
    }

    public PagingInfo(Page page) {
        int pageSize = this.showNum = page.getSize();
        this.curPage = page.getNumber();
        this.totalPage = page.getTotalPages();
        this.beginPage = (curPage % pagingSize) == 0 ? curPage : curPage - (curPage % pagingSize);
        if (this.beginPage < 0) this.beginPage = 0;
        this.endPage = ((beginPage + pagingSize) >= totalPage) ? totalPage - 1 : beginPage + pagingSize - 1;
        this.totalItems = page.getTotalElements();
        this.items = page.getContent();

        if (endPage < 0) this.endPage = 0;
    }

    public void setItems(List items) {
        this.items = items;
    }

    public int getBeginPage() {
        return beginPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public int getCurPage() {
        return curPage;
    }

    public List getItems() {
        return items;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setPathParam(String pathParam) {
        this.pathParam = pathParam;
    }

    public String getPathParam() {
        if (pathParam == null)
            return "?category=" + category + "&" + "value=" + value;

        return pathParam;
    }

    public int getShowNum() {
        return showNum;
    }

    public void setShowNum(int showNum) {
        this.showNum = showNum;
    }

    public int getPagingSize() {
        return pagingSize;
    }

    public void setPagingSize(int pagingSize) {
        this.pagingSize = pagingSize;
    }

    public void setBeginPage(int beginPage) {
        this.beginPage = beginPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    @Override
    public String toString() {
        return "PagingInfo{" +
                "beginPage=" + beginPage +
                ", endPage=" + endPage +
                ", curPage=" + curPage +
                ", items=" + items +
                ", totalPage=" + totalPage +
                ", totalItems=" + totalItems +
                ", category='" + category + '\'' +
                ", value='" + value + '\'' +
                ", pathParam='" + pathParam + '\'' +
                ", showNum=" + showNum +
                ", pagingSize=" + pagingSize +
                '}';
    }
}


package dev.guilhermesebastiao.authenticationapi.application.dto.query;

public class PagingParam {
    private int page;
    private int size;

    public PagingParam(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int zeroBasedPage() {
        return page - 1;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
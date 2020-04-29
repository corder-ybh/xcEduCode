package com.xuecheng.framework.domain.goods.response;

import com.xuecheng.framework.model.response.ResultCode;
import lombok.ToString;

@ToString
public enum BrandCode implements ResultCode {
    BRAND_ADD_EXISTSNAME(false, 34001, "品牌名称已存在"),
    BRAND_NOTEXISTS(false, 34004, "品牌信息不存在");
    boolean success;
    int code;
    String message;
    private BrandCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}

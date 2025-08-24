package com.maxiaowei.yupicturebackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求，接受删除数据的id作为参数
 */
@Data
public class DeleteRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}

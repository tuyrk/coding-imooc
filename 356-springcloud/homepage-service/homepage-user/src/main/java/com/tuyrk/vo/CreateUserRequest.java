package com.tuyrk.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * 创建用户请求对象
 *
 * @author tuyrk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private String username;
    private String email;

    /**
     * 请求有效性验证
     *
     * @return 是否有效
     */
    public boolean validate() {
        return StringUtils.isNotBlank(username) && StringUtils.isNotBlank(email);
    }
}

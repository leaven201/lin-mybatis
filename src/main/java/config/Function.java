package config;

import lombok.Data;

/**
 * @author LinZebin
 * @date 2019-06-04 12:12
 */
@Data
public class Function {
    private String sqlType;
    private String funcName;
    private String sql;
    private Object resultType;
    private String parameterType;
}

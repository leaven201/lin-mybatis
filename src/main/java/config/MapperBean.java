package config;

import lombok.Data;

import java.util.List;

/**
 * @author LinZebin
 * @date 2019-06-04 12:12
 */
@Data
public class MapperBean {

    /**
     * 接口名
     */
    private String interfaceName;
    /**
     * 接口下所有方法
     */
    private List<Function> list;
}

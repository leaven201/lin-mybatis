package mapper;

import bean.User;

/**
 * @author LinZebin
 * @date 2019-06-04 11:43
 */
public interface UserMapper {

    /**
     * 根据id查找user
     * @param id
     * @return
     */
    User getUserById(String id);
}

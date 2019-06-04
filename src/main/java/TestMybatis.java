import bean.User;
import mapper.UserMapper;
import sqlSession.MySqlSession;

/**
 * @author LinZebin
 * @date 2019-06-04 16:47
 */
public class TestMybatis {

    public static void main(String[] args) {
        MySqlSession sqlSession = new MySqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.getUserById("1");
        System.out.println(user);
    }

}

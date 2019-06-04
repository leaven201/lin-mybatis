package sqlSession;

/**
 * @author LinZebin
 * @date 2019-06-04 15:59
 */
public interface Executor {
    <T> T query(String statement,Object parameter);
}

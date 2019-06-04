package sqlSession;

import config.Function;
import config.MapperBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author LinZebin
 * @date 2019-06-04 15:59
 */
public class MyMapperProxy implements InvocationHandler {
    private MySqlSession mySqlSession;
    private MyConfiguration myConfiguration;

    public MyMapperProxy(MyConfiguration myConfiguration, MySqlSession mySqlSession) {
        this.myConfiguration = myConfiguration;
        this.mySqlSession = mySqlSession;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MapperBean readMapper = myConfiguration.readMapper("UserMapper.xml");
        //是否是xml文件对应的接口
        if (!method.getDeclaringClass().getName().equals(readMapper.getInterfaceName())){
            return null;
        }
        List<Function> list = readMapper.getList();
        if (list != null || list.size() != 0){
            for (Function func : list){
                //id是否和方法接口名一样
                if(method.getName().equals(func.getFuncName())){
                    return mySqlSession.selectOne(func.getSql(),String.valueOf(args[0]));
                }
            }
        }
        return null;
    }
}

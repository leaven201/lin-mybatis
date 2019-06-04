package sqlSession;

import config.Function;
import config.MapperBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author LinZebin
 * @date 2019-06-04 11:48 描述：读取与解析配置信息，并返回处理后的environment
 */
public class MyConfiguration {

    private static ClassLoader loader = ClassLoader.getSystemClassLoader();

    /**
     * 读取xml信息并处理
     */
    public Connection build(String resource) {
        try {
            InputStream stream = loader.getResourceAsStream(resource);
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            Element root = document.getRootElement();
            return evalDataSource(root);
        } catch (Exception e) {
            throw new RuntimeException("error occured while evaling xml" + resource);
        }
    }

    private Connection evalDataSource(Element node) throws ClassNotFoundException {
        if (!node.getName().equals("database")) {
            throw new RuntimeException("root should be <database>");
        }
        String driverClassName = null;
        String url = null;
        String username = null;
        String password = null;
        //获取属性节点
        for (Object item : node.elements("property")) {
            Element i = (Element) item;
            String value = getValue(i);
            String name = i.attributeValue("name");
            if (name == null || value == null) {
                throw new RuntimeException("[database]: <property> should contain name and value");
            }
            //赋值
            if ("url".equals(name)) {
                url = value;
            } else if ("username".equals(name)) {
                username = value;
            } else if ("password".equals(name)) {
                password = value;
            } else if ("driverClassName".equals(name)) {
                driverClassName = value;
            } else {
                throw new RuntimeException("[database]: <property> unknown name");
            }
        }
        Class.forName(driverClassName);
        Connection connection = null;
        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 获取property属性的值，如果有value的值，则读取，没有设置value，则读取内容
     */
    private String getValue(Element node) {
        return node.hasContent() ? node.getText() : node.attributeValue("value");
    }

    @SuppressWarnings("rawtypes")
    public MapperBean readMapper(String path) {
        MapperBean mapper = new MapperBean();
        try {
            InputStream stream = loader.getResourceAsStream(path);
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            Element root = document.getRootElement();
            //把mapper节点的namespace值存为接口名
            mapper.setInterfaceName(root.attributeValue("namespace").trim());
            //用来存储方法的list
            List<Function> list = new ArrayList<Function>();
            //遍历根结点下的所有子节点
            for (Iterator rootIter = root.elementIterator(); rootIter.hasNext(); ) {
                Function func = new Function();
                Element e = (Element) rootIter.next();
                String sqlType = e.getName().trim();
                String funcName = e.attributeValue("id").trim();
                String sql = e.getText().trim();
                String resultType = e.attributeValue("resultType").trim();
                func.setSqlType(sqlType);
                func.setFuncName(funcName);
                Object newInstance = null;
                try {
                    newInstance = Class.forName(resultType).newInstance();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                } catch (InstantiationException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                func.setResultType(newInstance);
                func.setSql(sql);
                list.add(func);
            }
            mapper.setList(list);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return mapper;
    }
}

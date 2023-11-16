package myssm.basedao;

import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class BaseDAO<T> {
    public final String DRIVER = "org.postgresql.Driver" ;
    public final String URL = "jdbc:postgresql://localhost:5432/resourceDB";
    public final String USER = "postgres";
    public final String PWD = "111131719" ;
    protected Connection conn ;
    protected PreparedStatement psmt ;
    protected ResultSet rs ;
    //T的Class对象
    private Class entityClass ;
    public BaseDAO(){
        //getClass() 获取Class对象，当前我们执行的是new FruitDAOImpl() , 创建的是FruitDAOImpl的实例
        //那么子类构造方法内部首先会调用父类（BaseDAO）的无参构造方法
        //因此此处的getClass()会被执行，但是getClass获取的是FruitDAOImpl的Class
        //所以getGenericSuperclass()获取到的是BaseDAO的Class
        Type genericType = getClass().getGenericSuperclass();
        //ParameterizedType 参数化类型
        Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
        //获取到的<T>中的T的真实的类型
        Type actualType = actualTypeArguments[0];
        try {
            entityClass = Class.forName(actualType.getTypeName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new DAOException("sb! BaseDAO 构造方法出错了，可能的原因是没有指定<>中的类型");
        }
    }
    protected Connection getConn(){
        try {
            //1.加载驱动
            Class.forName(DRIVER);
            //2.通过驱动管理器获取连接对象
            return DriverManager.getConnection(URL, USER, PWD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }
    protected void close(ResultSet rs , PreparedStatement psmt , Connection conn){
        try {
            if (rs != null) {
                rs.close();
            }
            if(psmt!=null){
                psmt.close();
            }
            if(conn!=null && !conn.isClosed()){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //给预处理命令对象设置参数
    private void setParams( PreparedStatement psmt , Object... params ) throws SQLException {
        if( params!= null && params.length>0 ){
            for ( int i = 0; i < params.length; i++ ) {
                System.out.println(" Param type : " + params[i].getClass() );
                System.out.println(" Date.class : " + Date.class);

                if( params[i].getClass() == Date.class ){
                    System.out.println("sb! Flag_1 params[" + i +"]" + params[i] + "; i : " + i  );
                    psmt.setObject(i+1,params[i],Types.TIMESTAMP);
                }else {
                    System.out.println("sb! Flag_2 params[" +i +"]" + params[i] + "; i : " + i  );
                    psmt.setObject(i+1,params[i]);
                }
                System.out.println(" ============================================= "  );

                //psmt.setObject(i+1,params[i]);
            }
        }
    }
    //执行更新，返回影响行数
    protected int executeUpdate(String sql , Object... params){
        boolean insertFlag = false ;
        insertFlag = sql.trim().toUpperCase().startsWith("INSERT");
        try {
            conn = getConn();
            if(insertFlag){
                psmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            }else {
                psmt = conn.prepareStatement(sql);
            }
            setParams(psmt,params);
            int count = psmt.executeUpdate() ;

            if(insertFlag){
                rs = psmt.getGeneratedKeys();
                if(rs.next()){
                    return ((Long)rs.getLong(1)).intValue();
                }
            }
            return 0 ;
        }catch (SQLException e){
            e.printStackTrace();
            throw new DAOException("sb! BaseDAO executeUpdate出错了");
        }
    }
    //通过反射技术给obj对象的property属性赋propertyValue值
    private void setValue(Object obj , String property ,Object propertyValue) throws ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        Class clazz = obj.getClass();

            //获取property这个字符串对应的属性名 ， 比如 "fid"  去找 obj对象中的 fid 属性
            Field field = clazz.getDeclaredField(property);
            if( field != null ){//  如果 field不为null, 就直接setValue了； 这里 有一个问题， 得到的值 的类型，和被setValue的类型 不匹配；

                String typeName = field.getType().getName();//获取 当前 字段的类型的 名称；
                //判断当前类型是否是自定义类型
                if( isMyType(typeName) ){
                    Class typeNameClass = Class.forName(typeName);
                    Constructor constructor = typeNameClass.getDeclaredConstructor(Integer.class);//获取构造器
                    propertyValue = constructor.newInstance(propertyValue);//
                }
                field.setAccessible(true);
                field.set(obj,propertyValue);
            }
    }
    private static boolean isNotMyType(String typeName){
        return "java.lang.Integer".equals(typeName)
                ||"java.lang.String".equals(typeName)
                    ||"java.util.Date".equals(typeName)
                        || "java.sql.Date".equals(typeName)
                            || "java.lang.Double".equals(typeName);
    }
    private static boolean isMyType(String typeName){
        /*if("java.lang.Integer".equals(typeName)){
            return false;
        }else if("java.lang.String".equals(typeName)){
            return false;
        }else if("java.lang.Date".equals(typeName)){
            return false;
        }
        return true; 这个方法纯纯的精神病*/
        return !isNotMyType(typeName);
    }
    //执行复杂查询，返回例如统计结果
    protected Object[] executeComplexQuery(String sql , Object... params){
        System.out.println(" credit ： " + sql);
        try {
            conn = getConn() ;
            psmt = conn.prepareStatement(sql);
            setParams(psmt,params);
            rs = psmt.executeQuery();

            //通过rs可以获取结果集的元数据
            //元数据：描述结果集数据的数据 , 简单讲，就是这个结果集有哪些列，什么类型等等

            ResultSetMetaData rsmd = rs.getMetaData();
            //获取结果集的列数
            int columnCount = rsmd.getColumnCount();
            Object[] columnValueArr = new Object[columnCount];
            //6.解析rs
            if(rs.next()){
                for(int i = 0 ; i<columnCount;i++){
                    Object columnValue = rs.getObject(i+1);     //33    苹果      5
                    columnValueArr[i]=columnValue;
                }
                return columnValueArr ;
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw new DAOException("BaseDAO executeComplexQuery出错了 丫出错了 sb!");
        }
        return null ;
    }
    //执行查询，返回单个实体对象
    protected T load(String sql , Object... params){
        System.out.println("sb! 进入 返回单个实体 对象的查询 sql : " + sql );
        try {
            conn = getConn() ;
            psmt = conn.prepareStatement(sql);
            setParams(psmt,params);
            rs = psmt.executeQuery();
            System.out.println(" rs : "+rs);
            //通过rs可以获取结果集的元数据
            //元数据：描述结果集数据的数据 , 简单讲，就是这个结果集有哪些列，什么类型等等
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取结果集的列数
            int columnCount = rsmd.getColumnCount();
            //6.解析rs
            if(rs.next()){
                T entity = (T)entityClass.newInstance();
                for( int i = 0 ; i<columnCount; i++ ){
                    String columnName = rsmd.getColumnName(i+1);            //fid   fname   price
                    Object columnValue = rs.getObject(i+1);      //33    苹果      5
                    System.out.println("sb! ColumnName : "+ columnName +" ; ColumnValue : "+ columnValue +" ; ");
                    setValue(entity,columnName,columnValue);
                }
                return entity ;
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new DAOException("sb! BaseDAO load出错了");
        }
        return null ;
    }
    //执行查询，返回List
    protected List<T> executeQuery(String sql , Object... params){
        System.out.println(sql);
        List<T> list = new ArrayList<>();
        try {
            conn = getConn() ;
            psmt = conn.prepareStatement(sql);

            setParams(psmt,params);
            rs = psmt.executeQuery();
           //通过rs可以获取结果集的元数据
            //元数据：描述结果集数据的数据 , 简单讲，就是这个结果集有哪些列，什么类型等等

            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println(" rsmd.getColumnCount() : "+ rsmd.getColumnCount());
            //获取结果集的列数
            int columnCount = rsmd.getColumnCount();
            //6.解析rs
            while( rs.next()){
                T entity = (T)entityClass.newInstance();
                for( int i = 0 ; i <columnCount ; i++ ){
                    String columnName = rsmd.getColumnLabel(i+1);       //fid   fname   price
                    System.out.println("columnName : " + columnName);
                    Object columnValue = rs.getObject(i+1);     //33    苹果      5
                    System.out.println("columnValue : " + columnValue);
                    setValue(entity,columnName,columnValue);
                    // 这一步 这里面 进行了 改变，如果 返回的 不是基本 数据类型; 方法中 重新new对象 然后 扔进list里面
                }
                list.add(entity);
            }
        } catch ( Exception e ){
            e.printStackTrace();
            throw new DAOException("sb! BaseDAO executeQuery出错了");
        }
        return list ;
    }
}

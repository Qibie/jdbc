### 字符集和字符编码（Charset & Encoding）
[字符编码常识及问题解析](http://blog.jobbole.com/76376/)  
[字符集和字符编码](http://blog.jobbole.com/86813/)  
字符编码是字符集（字符编码标准）的一种实现  

常用字符集|常用字符编码|字符占字节数|兼容性
:|:|:|:
ASCII字符集：主要包括控制字符（回车键、退格、换行键等）；可显示字符（英文大小写字符、阿拉伯数字和西文符号）|ASCII编码|1（7bit）|EASCII(8bit)
GB2312《信息交换用汉字编码字符集·基本集》收录6763个汉字|GB2312编码|1-2|1字节-兼容ASCII，2字节-汉字
BIG5统一繁体字符集称大五码或五大码|BIG5编码？|2|兼容ASCII，与BG2312冲突
GBK汉字编码国家标准,是对GB2312编码的扩充|GBK编码？|2|兼容GB2312
Unicode（统一码、万国码、单一码、标准万国码）|UTF-32/ UTF-16/ UTF-8|4|  

1.windows Notepad中的编码ANSI保存选项，代表什么含义？  
ANSI是windows的默认的编码方式，对于英文文件是ASCII编码，对于简体中文文件是GB2312编码（只针对Windows简体中文版，如果是繁体中文版会采用Big5码）。所以，如果将一个UTF-8编码的文件，另存为ANSI的方式，对于中文部分会产生乱码。

2.什么是UTF-8的BOM？  
BOM的全称是Byte Order Mark，BOM是微软给UTF-8编码加上的，用于标识文件使用的是UTF-8编码，即在UTF-8编码的文件起始位置，加入三个字节“EE BB BF”。这是微软特有的，标准并不推荐包含BOM的方式。采用加BOM的UTF-8编码文件，对于一些只支持标准UTF-8编码的环境，可能导致问题。比如，在Go语言编程中，对于包含BOM的代码文件，会导致编译出错。


### char类型  
|转义序列|名称|Unicode值|
|-|-|-|
|\t|制表|\u0009|
|\n|换行|\u000a|
|\r|回车|\u000d|

~~~java
public Test() throws RepletException {
	try {
		System.out.println("Test this Project!")
		}
	catch (Exception e) {
		throw new Exception(e.toString());
		}
}
~~~
throws是用来声明一个方法可能抛出的所有异常信息
throw则是指抛出的一个具体的异常类型。
通常在一个方法（类）的声明处通过throws声明方法（类）可能抛出的异常信息，而在方法（类）内部通过throw声明一个具体的异常信息。
throws通常不用显示的捕获异常，可由系统自动将所有捕获的异常信息抛给上级方法；
throw则需要用户自己捕获相关的异常，而后在对其进行相关包装，最后在将包装后的异常信息抛出。  
Eclipse在某个类名按F3进入类，变量按F3跳转创建的地方,Ctril+O打开类的所有成员方法，成员变量

#### JDBC六个步骤 ####
1. 注册驱动
2. 建立连接
3. 创建语句
4. 执行语句
5. 处理结果
6. 释放资源

#### PreparedStatement VS Statement ####
1. PreparedStatement是Statement的子接口
2. PreparedStatement是一种预处理，防止注入
3. 创建语句不一样，Statement创建语句时调用Connection接口中的createStatement()方法，不带参数。PreparedStatement创建语句调用Connection接口中的prepareStatement(String sql)方法，带参数，此时创建语句得先传入Sql语句。
4. 执行语句不一样，以executeQuery()查询方法为例：PreparedStatement接口中重写抽象方法的executeQuery()不带参数，Statement接口中executeQuery(String sql)带参数。

~~~Statement
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;
	try {
		// 2.建立连接
		conn = conn = JdbcUtils.getConnection();
		// 3.创建语句
		st = conn.createStatement();
		// 4.执行语句
		String sql = "select id, name, money, birthday from user";
		rs = st.excuteQuery(sql);
		// 5.处理结果
		while (rs.next()) {
			System.out.println(rs.getObject("id") + "\t" + rs.getObject("name") + "\t" + rs.getObject("birthday")
						+ "\t" + rs.getObject("money"));
		}
	} finally {
		// 6.释放资源
		JdbcUtils.free(rs, st, conn);
	}  
~~~
~~~PreparedStatement
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
		// 2.建立连接
		conn = conn = JdbcUtils.getConnection();
		// 3.创建语句 
		String sql = "select id, name, money, birthday from user where name = ?";
		ps = conn.preparedStatement(sql);
		ps.setString(1, name);
		// 4.执行语句
		rs = ps.excuteQuery();
	}
~~~

#### 23.事务的隔离级别 ####
~~~sql
#查询事务隔离级别
select @@tx_isolation;
#设置事务隔离级别为脏读 读未提交（Read uncommited）
set transaction isolation level read uncommitted;
#开启事务
start transaction;
select * from user;
#提交
commit;
#更改事务隔离级别提交事务才能读，但不能重复读 读已提交（Read commited）
set transaction isolation level read committed;
#开启事务
start transaction;
#更改事务隔离级别提交事务重复读 可重复读(Repeatable read)
set transaction isolation level repeatable read;
#查询事务隔离级别
select @@tx_isolation;
#开启事务
start transaction;
select * from user;
#插一条数据
insert into user(name, birthday, money) values ('tx_name', now(), 100);
select * from user;
#更改事务隔离级别提交事务 可串行化 序列化(Serializable)
set transaction isolation level serializable;
#开启事务
start transaction;
select * from user;
insert into user(name, birthday, money) values ('tx_name1', now(), 100);
~~~
#### 24.使用JDBC调用存储过程 ####
~~~sql
#创建addUser存储过程 
DELIMITER $$

DROP PROCEDURE IF EXISTS `jdbc`.`addUser` $$
CREATE PROCEDURE `jdbc`.`addUser` (IN pname VARCHAR(45), IN birthday date, IN money float, out pid int)
BEGIN
	insert into user(name, birthday, money) values (pname, birthday, money);
	select LAST_INSERT_ID() into pid;
END $$

DELIMITER ;

#查看表结构
show create table user;
#修改表之增加索引
alter table [tbName] add [unique|fulltext] index 索引名(列名);
alter table user add unique index unique_name(name);
#查询
select * from user;
#删除id>10的记录
delete from user where id > 10;
~~~
#### 26.可滚动结果集与分页技术 ####
~~~sql
#mysql支持的分页技术
#第一个参数指定第一个返回记录行的偏移量，第二个参数指定返回记录行的最大数目。初始记录行的偏移量是 0(而不是 1)
SELECT * FROM table LIMIT [offset,] rows | rows OFFSET offset;
select id, name, money, birthday from user limit 150,10;
~~~
#### 27.可更新和对更新敏感的结果集 ####
#### 28.数据库的元数据信息 ####
~~~java
接口java.sql.DatabaseMetaData 
DatabaseMetaData用到的两个抽象方法，一个获取数据库名称，一个查询是否支持事务

/**
     * Retrieves the name of this database product.
     *
     * @return database product name
     * @exception SQLException if a database access error occurs
     */
    String getDatabaseProductName() throws SQLException;
     /**
     * Retrieves whether this database supports transactions. If not, invoking the
     * method <code>commit</code> is a noop, and the isolation level is
     * <code>TRANSACTION_NONE</code>.
     *
     * @return <code>true</code> if transactions are supported;
     *         <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    boolean supportsTransactions() throws SQLException;
~~~
#### 29.参数的元数据信息 ####
程序报错:SQL java.sql.SQLException: Parameter metadata not available for the given statement  
`配置数据库地址url加上generateSimpleParameterMetadata=true`  
参数元数据：ParameterMetaData接口  
~~~java
ParameterMetaData pmd = ps.getParameterMetaData();
// 获取参数总数（sql中的？占位符个数） 
pmd.getParameterCount()
// 获取列名
pmd.getParameterClassName(i)
// 获取类型
pmd.getParameterType(i)
// 获取类型名字
pmd.getParameterTypeName(i) 若不查库，默认返回VARCHAR
结果集：
java.lang.String	12	VARCHAR
java.lang.String	12	VARCHAR
java.lang.String	12	VARCHAR
~~~
#### 30.利用结果集元数据将查询结果封装为map ####
结果集元数据：ResultSetMetaData接口
~~~java
ResultSetMetaData rsmd = rs.getMetaData();
// 获取结果的总列数
rsmd.getColumnCount();
// 获取该列的java类型 准确，与ParameterMetaData.getParameterClassName比
rsmd.getColumnClassName(i)
// 获取列名
rsmd.getColumnName(i)
// 获取列名的别名
rsmd.getColumnLabel(i)
~~~
#### 31.Java反射技术入门 ####
*.class文件的二进制头为CA FE BA BE，虚拟机验证用  
System.out.println()会自动调用Obejct的toString()成员方法
#### 32.Java反射的更多细节 ####
~~~ java
getDeclaredMethods()方法拿到当前类的公有私有方法,拿不到超类继承的方法  
Method[] ms = obj.getClass().getDeclaredMethods();  
getMethods()方法拿到公共方法，包括从超类继承的方法   
ms = obj.getClass().getMethods();  

Class类getMethod()方法：第一个传方法名，后面传参数类型  
public Method getMethod(String name, Class<?>... parameterTypes)  

Class类getDeclaredFields()方法拿到当前类的所有公有私有属性Field数组  
Field[] fs = clazz.getDeclaredFields();  
Class类getFields()方法拿到公有属性  
fs = clazz.getFields();  
~~~
#### 33.利用Java反射技术将查询结果封装为对象 ####
Method类的invoke方法，第一个参数传调用的对象，后面传参数类型  
Ibatis是是使用ORM这种方式的，Hibernate通过配置文件
#### 34.编写一个基本连接池来实现连接的复用 ####
对集合频繁的增加删除操作，LinkedList优于ArrayList  
从集合里拿出来相当于从集合中删除一个元素，放入集合中相当于增加一个元素  
~~~java
LinkedList的removeFirst()方法
Removes and returns the first element from this list.
~~~
#### 35.对基本连接池进行一些工程细节上的优化 ####
web的多线程并发获取连接，如果连接池中的5个连接都被取完了，池中是空的：1.加入同步块；2.建立新连接  
基于数据库的保护机制，连接不能一直建立，mysql可以承载约200-300左右的并发连接，oracle可以更多，基于硬件的性能（例如CPU），再多就会影响性能，查询变慢
#### 36.通过代理模式来保持用户关闭连接的习惯（懵逼） ####  
不释放资源，如果没有使用JdbcUtils.free方法，或者其他开发没有使用封装工具类的规范,而是使用Connection的close方法，关闭的连接就不能放入连接池中  
为了不影响其他人的使用习惯，需要对JdbcUtils.getConnection()方法进行优化，相当于把Connection.close()方法拦截下来：
1.继承Connection接口的实现类：工作量太大，麻烦，如果换一个数据库，要重新定义继承类，而且有限制，例如mysql的Connection没有public的构造器
2.组合优先于继承
面对对象编程的第一原则：如果能使用接口就使用接口  
MyConnection中的构造器没有public修饰方法，和MyDataSource都处于同一个datasource package里面，MyDataSource类只能在同一包内调用，别人不能使用，只能拿到Connection，拿不到MyConnection,为以后演化留下余地，使客户端不会依赖MyConnection
循环双向调用，MyConnection调用MyDataSource，MyDataSource调用MyConnection。  
运用了设计模式中的代理模式，MyConnection是代理，在close方法做了判断，放回连接池中

#### 37.Java的动态代理及使用该技术完善连接代理
实现java.lang.reflect.InvocationHandler接口（调用处理器）
可以把这句  
this.warpedConnection = (Connection) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { Connection.class }, this);
理解为通过代理模式构造出来的  
MyConnection myConnection = new MyConnection(realConn, this);

### 38.标准DataSource接口及数据源的总结介绍
实际中不用自己来写数据源，主要实现了DataSource接口  
DataSource取代DriverManage原因：  
1. 获得Connection速度快，因为内部有一个连接池，比DriverManage创建要快。  
2. 通过DataSource获得的Connection都是已经被包裹过的（不是驱动原来的连接），他的close（）方法已经被修改。  
3. 有一个连接池，存放Connection的集合Collection。  
4. 我们的程序之和DataSource打交道，不会直接访问连接池。  

### 39.如何使用开源项目DBCP（实际项目中常用）
1. dbcpconfig.properties（使用配置文件替代DriverManger）；
2. 引入3个jar包commons-collections-3.1.jar、commons-dbcp-1.2.2.jar、commons-pool.jar；
3. 修改配置文件比修改代码效率高，因为修改代码后还需要重新编译打包；
4. Properties新建配置文件类，InputStream创建字节流，通过JdbcUtils.class.getClassLoader().getResourceAsStream("dbcpconfig.properties")来将配置文件写入输入流中，接着使用Properties的load()方法，将输入流信息放入配置类实例中prop.load(is);；
5. 数据源通过BasicDataSourceFactory.createDataSource来创建，myDataSource = BasicDataSourceFactory.createDataSource(properties);

### 40.将DAO中的修改方法提取到抽象父类中
代码重构主要将变的部分与不变的部分区分开来，去除冗余，将变的部分与不变的部分抽出来，以后只需要修改变的部分，不变的部分作为公用（通用）。  
UserDaoJdbcImpl类中update需要修改1.sql语句，2.set方法部分。  
所有对数据的操作都放入到超类中，之前的只能对User类进行操作  
新手在写子类的时候可能会忘记关掉连接，而在超类中继承的时候已经做了这个操作，避免故障  
通过修改UserDaoImpl可以实现增删改，只需要给AbstractDao传入sql和args参数，有了一个公共的超类可以公用，代码简化了很多，插入操作可能会麻烦，因为不能直接拿到主键

### 41.使用模板方法设计模式处理DAO中的查询方法
测试类(调用)-子类方法-父类方法-子类结果处理集方法  
模板设计模式：如果做一件事情需要几个步骤，其中有几个步骤是不确定的，那么把这几个步骤抽象出来，让子类去实现这些事情  
例如说超类的中rowMapper结果集处理方法在超类是无法实现的，那么推到子类中去做

### 42.使用策略模式对模板方法设计模式进行改进  
在UserDaoImpl中，如果只想查UserName的话，使用模板设计模式继承的话，效率不高  
新增一个行映射器（接口），处理结果集实现接口  
对比AbstractDao和MyDaoTemplate，前者把行映射交给类中方法来做，后者交给专门做映射的接口来做  
当查询结果更换列时，策略模式通过修改内部匿名类来实现行映射器，因为只需要实现RowMapper接口中mapRow方法，比较灵活  
策略设计模式通过类组合方式来达成，而模板模式通过继承来达到，没有策略灵活性高  
### 43.使用JdbcTemplate工具类（线程安全）简化对象查询
Spring框架中提供了一个JdbcTemplate工具类，对JDBC API进行了很好的封装，与之前做的类似而且功能更加强大，以后再实际项目中可以直接使用这个工具类，与直接使用JDBC API没有太大的性能区别，使用这个类需要从spring开发包中导入spring.jar和commons-logging.jar包  
JdbcTemplate类有一个构造方法需要放入数据源  
JdbcTemplateTest中演示了JdbcTemplate类queryForObject(sql, args, rowMapper)方法；  
还有通过反射结果集的方法，不再写结果集的行映射器匿名类了，BeanPropertyRowMapper(User.class)，里面传入需要映射的实体类。    

### 44.JdbcTemplate类中的其他各个查询方法
~~~Eclipse快捷鍵
Shift + Enter 另起一行  
Ctrl + Shift + Enter 当前行
~~~
JdbcTemplate.query返回的是一个List  
queryForInt返回一个int，传入参数是sql  
queryForMap  

### 45.使用JdbcTemplate完成数据库修改和其他功能  
更新操作JdbcTemplate.update(sql, args[]),传入两个参数来更新  
query还有一个可以传4个参数的重载方法  

### 46.使用支持命名参数的JdbcTemplate  
为了解决使用JdbcTemplate类中方法参数数组Object[] {id, name}必须要对齐的问题，可以使用支持命名参数NamedParameterJdbcTemplate类，内部封包含了JdbcTemplate类，这个类主要做了处理参数的操作  
SqlParameterSource接口通过反射，可以获取一个参数源，		SqlParameterSource ps = new BeanPropertySqlParameterSource(user)，传入一个对象，从而不用写参数的map，限制sql中列名必须和对象中的属性名字一样  
实际上NamedParameterJdbcTemplate类与JdbcTemplate类处理参数中区别在于：  
1. query方法的第二个参数，前者的参数是一个Map，而后者是一个Object，需要对齐，而使用Map后可以进行映射；  
2. query方法中，前者可以传入一个SqlParameterSource接口,通过BeanPropertySqlParameterSource反射获取数据源；  
3. 占位符取了名字，:name取代了？，而且限定列名必须和对象中的属性名字一样  

### 47.使用SimpleJdbcTemplate和泛型技术简化代码  
SimpleJdbcTemplate类必须在JDK1.5以上才能使用，使用了泛型  
在jdk1.5以后,Object...代表参数不确定的情况，可以理解为数组  
SimpleJdbcTemplate类两个特性是：  
1. 支持泛型
2. 使用可变长参数  
~~~行映射器
User user = simple.queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance(User.class), name,
				100f);
~~~   
三者关系：
SimpleJdbcTemplate类中包含了NamedParameterJdbcTemplate，而NamedParameterJdbcTemplate又包含了JdbcTemplate，JdbcTemplate是最底层实现，每包含一层就增加一些新特性  
举例说，如果SimpleJdbcTemplate类自身update方法，拿不到返回主键，可通过getgetNamedParameterJdbcOperations()拿到NamedParameterJdbcTemplate再使用update方法，就可以拿到，同理还有getJdbcOperations获取JdbcTemplate的实例  
~~~
simple.getNamedParameterJdbcOperations().update(sql, keyHolder);
simple.getJdbcOperations() 
~~~ 

### 48.使用JdbcTemplate实现DAO和用工厂灵活切换实现	
主要通过Spring里面的JdbcTemplate来简化JdbcDao实现里面的代码  
如果需要替换工厂的实现，修改daoconfig.properties文件中userDaoClass类，换成Spring的实现  

## 2018-06-14 00：14写在README.md最后
终于过了一遍Jdbc，从4月开始，也算在J2SE向J2EE发展的路线上，完成了一个坑

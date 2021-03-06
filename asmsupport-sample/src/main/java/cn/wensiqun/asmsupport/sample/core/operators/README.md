在这个包下主要介绍java下的常用操作的字节码生成。什么是操作呢，其实和java代码一样
比如有如下代码：

    String a = "str";
    int i = 1 + 2;
    i++;
    System.out.println(a + i);
    
这里存在6种操作，让我们一句一句解析出操作具体如下：

	1.String a = "str";
	  = : 赋值
	2.int i = 1 + 2;
	  + : 算数运算操作
	  = : 赋值
	3.i++; 
	  ++ : 增量操作
	4 System.out.println(a + i);
	  _第二个"." : 方法调用
	  + : 字符串拼接操作
  
既然是操作就有操作因子，什么是操作因子?
如下表:

|代码|操作因子|
|:-------------|:-------------|
| String a = "str"|"str"|
-----------------------------------------------
| int i = 1 + 2;| 1, 2, 1+2|
-----------------------------------------------
| i++|i|
-----------------------------------------------
| System.out.println(a + i) | a, i, a + 1     |
-----------------------------------------------

总共存在10种操作，具体如下(这里只列举每种操作的其中一个，并不全部列举)：

	1. String a = "str";
	   = : 赋值
	
	2. i++; 
	   ++ : 增量操作
	
	3. int i = 1 + 2;
	   + : 算数运算操作
	   = : 赋值
	
	4. 9&7
	   & ：位操作
	
	5. a > 1
	   > : 关系运算操作
	
	6. true && false
	   && : 逻辑运算操作
	  
	7. k = i < 0 ? -i : i
	   三元操作
	
	8. a instanceof String
	   instanceof : instanceof操作
	   
	9. "Hello " + "asmsupport!"
	   + : 字符串拼接操作
	   
	10. System.out.println(a + i);
	   第二个"." : 方法调用操作


在我们的程序中我们将所有的操作都通过IBlockOperators中的方法调用实现。而所有的操作因子是通過方法参数
传入的。并且有的操作同样能作为其他操作的操作因子,如上面的1+2就是作为赋值操作"="的操作因子。在
asmsupport中只要是继承了jw.asmsupport.Parameterized接口的都可以是作为操作因子。其实在使用asmsupport
的时候大部分只要在java代码中能作为操作因子的在asmsupport中同样也能作为操作因子。因为asmsupport保留了
java代码编写的方式。

下面我们将通过各个实例逐一解释每一种操作 。
  
	1.ReturnOperatorCreate.java
	  _介绍return操作
	2.AssignmentGenerate.java
	  _本地变量的赋值操作
	3.CrementOperatorGenerate.java
	  _增量操作++和--的操作
	4.ArithmeticOperatorGenerate.java
	  _算数运算+-*/%的运算操作
	5.BitwiseOperatorGenerate.java
	  _位运算符的操作
	6.RelationalOperatorGenerate.java
	  _关系运算符操作
	7.LogicalOperatorGenerate.java
	  _逻辑运算符操作
	8.TernaryOperatorGenerate.java
	  _三元运算操作
	9.InstanceofOperatorGenerate.java
	  _instanceof的操作
	10.ArrayOperatorGenerate.java
	  _数组操作
	11.MethodInvokeOperatorGenerate.java
	  _方法调用操作
  
  
  
  
  
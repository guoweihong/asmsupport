package example.helloworld;

import java.lang.reflect.InvocationTargetException;

import org.objectweb.asm.Opcodes;


import jw.asmsupport.block.method.common.StaticMethodBody;
import jw.asmsupport.clazz.AClass;
import jw.asmsupport.clazz.AClassFactory;
import jw.asmsupport.creator.ClassCreator;
import jw.asmsupport.definition.value.Value;
import jw.asmsupport.definition.variable.LocalVariable;


import example.AbstractExample;

/**
 * Create a "example.generated.HelloWorld" class and contain a main method.
 * Run the main method will be print a line "Hello World"
 */
public class HelloWorld extends AbstractExample{

	/**
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) {
		ClassCreator creator = new ClassCreator(Opcodes.V1_5, Opcodes.ACC_PUBLIC , "generated.helloworld.HelloWorldExample", null, null);
		creator.createStaticMethod("main", new AClass[]{AClassFactory.getProductClass(String[].class)}, new String[]{"args"}, null, null,
				Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, new StaticMethodBody(){

					@Override
					public void generateBody(LocalVariable... argus) {
						invoke(systemOut, "println", Value.value("Hello World"));
						//don't forget return.
						runReturn();
					}
			
		});
		generate(creator);
	}

}
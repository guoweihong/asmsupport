package demomodify;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import org.objectweb.asm.Opcodes;


import jw.asmsupport.block.control.Else;
import jw.asmsupport.block.control.IF;
import jw.asmsupport.block.method.common.CommonMethodBody;
import jw.asmsupport.block.method.common.MethodBodyForModify;
import jw.asmsupport.clazz.AClass;
import jw.asmsupport.clazz.AClassFactory;
import jw.asmsupport.creator.ClassModifier;
import jw.asmsupport.definition.value.Value;
import jw.asmsupport.definition.variable.GlobalVariable;
import jw.asmsupport.definition.variable.LocalVariable;
import jw.asmsupport.utils.ASConstant;


import demo.CreateMethod;

@org.junit.Ignore
public class Test {

	static GlobalVariable out = CreateMethod.out;
	
	/**
	 * @param args
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		ClassModifier cm = new ClassModifier(ByModify.class);
		cm.createGlobalVariable("age", Opcodes.ACC_STATIC + Opcodes.ACC_PRIVATE, AClass.INT_ACLASS);
		cm.createMethod("asmcreate", null,null,null,null, Opcodes.ACC_PUBLIC, new CommonMethodBody(){
			@Override
			public void generateBody(LocalVariable... argus) {
				invoke(out, "println", Value.value("created by asm"));
				runReturn();
			}
		});
		
		cm.modifyMethod(ASConstant.CLINIT, null, new MethodBodyForModify(){
			@Override
			public void generateBody(LocalVariable... argus) {
				GlobalVariable age = getMethodOwner().getGlobalVariable("age");
				assign(age, Value.value(20));
				this.invokeOriginalMethod();
				GlobalVariable name = getMethodOwner().getGlobalVariable("name");
				assign(name, Value.value("wensiqun"));
				invoke(out, "println", name);
				runReturn();
			}
		});
		
		cm.modifyMethod("helloWorld", null, new MethodBodyForModify(){

			@Override
			public void generateBody(LocalVariable... argus) {
				invoke(out, "println", Value.value("before"));
				
				AClass randomClass = AClassFactory.getProductClass(Random.class);
				LocalVariable random = this.createVariable("random", randomClass, false, this.invokeConstructor(randomClass, Value.value(1L)));
				ifthan(new IF(invoke(random, "nextBoolean")){
					@Override
					public void generateInsn() {
						invokeOriginalMethod();
					}

				}).elsethan(new Else(){
					@Override
					public void generateInsn() {
						invoke(out, "println", Value.value("call self"));
					}
					
				});
				invoke(out, "println", Value.value("after"));
				runReturn();
			}
			
		});
		
		cm.modifyMethod("String", null, new MethodBodyForModify(){

			@Override
			public void generateBody(LocalVariable... argus) {
				invoke(out, "println", Value.value("before"));
				LocalVariable lv = this.createVariable(null, getOriginalMethodReturnClass(), true, invokeOriginalMethod());
				invoke(out, "println", Value.value("after"));	
				runReturn(lv);
			}
			
		});
		cm.setClassOutPutPath(".\\target\\generate\\");
		Class<?> c = cm.startup();
		
		/*IByModify bm = (IByModify) c.newInstance();
		Method asmcreate = c.getDeclaredMethod("helloWorld");
		asmcreate.invoke(bm);*/
		
		IByModify bm =  new ByModifyChild();
		bm.helloWorld();
	}

}
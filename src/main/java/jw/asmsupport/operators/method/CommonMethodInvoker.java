package jw.asmsupport.operators.method;

import java.lang.reflect.Modifier;

import jw.asmsupport.Parameterized;
import jw.asmsupport.block.ProgramBlock;
import jw.asmsupport.definition.variable.IVariable;
import jw.asmsupport.entity.VariableEntity;
import jw.asmsupport.utils.ASConstant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectweb.asm.Type;

public class CommonMethodInvoker extends MethodInvoker {
	
	private static Log log = LogFactory.getLog(CommonMethodInvoker.class);
    
	private Parameterized callObjReference;
	
	protected CommonMethodInvoker(ProgramBlock block, Parameterized objRef, String name, Parameterized[] arguments) {
		super(block, objRef.getParamterizedType(), name, arguments);
		this.callObjReference = objRef;
		if(callObjReference.getParamterizedType().isPrimitive()){
			throw new IllegalArgumentException("cannot invoke method at primitive type \"" + callObjReference.getParamterizedType() +  "\" : must be a non-primitive variable");
		}
        //默认不保存引用
        setSaveReference(false);
        if(callObjReference instanceof MethodInvoker){
        	//set the method caller to save reference;
            ((MethodInvoker)callObjReference).setSaveReference(true);    
        }
	}

    @Override
    protected void checkAsArgument() {
    	callObjReference.asArgument();
        super.checkAsArgument();
    }

	@Override
    public void lastPrepareProcess() {
        //如果是静态方法那么则创建一个静态方法调用者到执行队列
        if(Modifier.isStatic(getModifiers())){
            //移除当前的方法调用
            MethodInvoker mi = new StaticMethodInvoker(block, getActuallyOwner(), name, arguments);
            block.removeExe(mi);
            block.replaceExe(this, mi);
        }
    }

	@Override
	protected void executing() {
        
        /* if method is non satic*/
        if(!Modifier.isStatic(getModifiers())){
            log.info("put reference to stack");
            //变量入栈
            callObjReference.loadToStack(block);
            argumentsToStack();
            if(callObjReference.getParamterizedType().isInterface()){
            	log.info("invoke interface method : " + name);
                //如果是接口
                insnHelper.invokeInterface(callObjReference.getParamterizedType().getType(), this.name, getReturnType(), mtdEntity.getArgTypes());
            }else{
                log.info("invoke class method : " + name);
                if(callObjReference instanceof IVariable){
                	 VariableEntity ve = ((IVariable)callObjReference).getVariableEntity();
                	 if(ve.getName().equals(ASConstant.SUPER)){
                         insnHelper.invokeSuperMethod(callObjReference.getParamterizedType().getType(), this.name, getReturnType(), mtdEntity.getArgTypes());
                     }else {
                         insnHelper.invokeVirtual(callObjReference.getParamterizedType().getType(), this.name, getReturnType(), mtdEntity.getArgTypes());
                     }
                }else{
                	insnHelper.invokeVirtual(callObjReference.getParamterizedType().getType(), this.name, getReturnType(), mtdEntity.getArgTypes());
                }
            }
            if(!isSaveReference()){
                if(!getReturnType().equals(Type.VOID_TYPE)){
                    insnHelper.pop();
                }
            }
        }
	}
	
    @Override
    public String toString() {
        return callObjReference + "." + mtdEntity;
    }
}
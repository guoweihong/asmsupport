/**    
 *  Asmsupport is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.wensiqun.asmsupport.client;

import cn.wensiqun.asmsupport.client.block.EnumStaticBlockBody;
import cn.wensiqun.asmsupport.core.build.resolver.EnumResolver;
import cn.wensiqun.asmsupport.core.loader.CachedThreadLocalClassLoader;
import cn.wensiqun.asmsupport.core.utils.CommonUtils;
import cn.wensiqun.asmsupport.core.utils.log.LogFactory;
import cn.wensiqun.asmsupport.standard.def.clazz.IClass;
import cn.wensiqun.asmsupport.standard.error.ASMSupportException;
import cn.wensiqun.asmsupport.standard.utils.ASMSupportClassLoader;
import cn.wensiqun.asmsupport.utils.lang.StringUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class DummyEnum extends AbstractDummy {

    /** Version of Class File Format */
    private int javaVersion = CommonUtils.getJDKVersion();

    /** Package name of class */
    private String packageName = StringUtils.EMPTY;

    /** Package name of class */
    private String name;

    /** Any interfaces in the class */
    private IClass[] interfaces;
 
    /** All enum constants. */
    private Set<String> enums = new HashSet<String>();

    /** All constructors */
    private LinkedList<DummyEnumConstructor> constructorDummies = new LinkedList<DummyEnumConstructor>();

    /** class static block */
    private EnumStaticBlockBody staticBlock;
    
    /** All fields */
    private LinkedList<DummyField> fieldDummies = new LinkedList<DummyField>();
    
    /** All methods */
    private LinkedList<DummyMethod> methodDummies = new LinkedList<DummyMethod>();

    /** Specify the classloader */
    private ASMSupportClassLoader classLoader;

    /** What's the class generate path of the class, use this for debug normally */
    private String classOutPutPath;
    
    /** Whether or not print log.*/
    private boolean printLog;
    
    /** The log file path.*/
    private String logFilePath;

    public DummyEnum() {
    	this(null, CachedThreadLocalClassLoader.getInstance());
    }
    
    public DummyEnum(String qualifiedName) {
    	this(qualifiedName, CachedThreadLocalClassLoader.getInstance());
    }
    
    public DummyEnum(String qualifiedName, ASMSupportClassLoader classLoader) {
    	super(classLoader);
        if(classLoader == null) {
        	throw new ASMSupportException("Class loader must be not null");
        }
        if(StringUtils.isNotBlank(qualifiedName)) {
            int lastDot = qualifiedName.lastIndexOf('.');
            if(lastDot > 0) {
                packageName = qualifiedName.substring(0, lastDot);
                name = qualifiedName.substring(lastDot + 1);
            } else {
            	name = qualifiedName;
            }
        }
    }
    
    /**
     * Set the jdk version of Class File Format
     * 
     * @param version
     * @return
     */
    public DummyEnum setJavaVersion(int version) {
        javaVersion = version;
        return this;
    }

    /**
     * 
     * Get the jdk version of Class File Format
     * 
     * @return
     */
    public int getJavaVersion() {
        return javaVersion;
    }
    

    
    /**
     * Set the package name of class. 
     * 
     * @param packageName
     * @return
     */
    public DummyEnum package_(String packageName) {
        this.packageName = packageName;
        return this;
    }

    /**
     * Get the class package name.
     * 
     * @return
     */
    public String getPackage() {
        return packageName;
    }

    /**
     * Set the class name.
     * 
     * @param name
     * @return
     */
    public DummyEnum name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get the class name
     * 
     * @return
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set the interfaces
     * 
     * @param interfaces
     * @return
     */
    public DummyEnum implements_(IClass... interfaces) {
        this.interfaces = interfaces;
        return this;
    }
    
    /**
     * Set the interfaces 
     * 
     * @param itfs
     * @return
     */
    public DummyEnum implements_(Class<?>... itfs) {
    	if(itfs != null) {
    		this.interfaces = new IClass[itfs.length];
    		for(int i=0; i<itfs.length; i++) {
    			this.interfaces[i] = getClassLoader().getType(itfs[i]);
    		}
    	}
    	return this;
    }


    /**
     * Set the classloader
     * 
     * @param cl
     * @return
     */
    public DummyEnum setClassLoader(ASMSupportClassLoader cl) {
        this.classLoader = cl;
        return this;
    }
    
    /**
     * Set the class out put path.
     * 
     * @param path
     * @return
     */
    public DummyEnum setClassOutPutPath(String path) {
        this.classOutPutPath = path;
        return this;
    }
    
    /**
     * Get the class out put path.
     * 
     * @return
     */
    public String getClassOutPutPath() {
        return classOutPutPath;
    }
    
    /**
     * Whether or not print log information. if set {@code true} than print it to console.
     * Also you can print to file by method {@link DummyClass#setLogFilePath(String)}
     * 
     * @param print
     * @return
     */
    public DummyEnum setPrintLog(boolean print) {
        printLog = print;
        return this;
    }

    /**
     * The log information out put path. call the method will set {@code printLog} to {@code true.}
     * 
     * @param logFile
     * @return
     */
    public DummyEnum setLogFilePath(String logFile){
        logFilePath = logFile;
        printLog = true;
        return this;
    }
    
    /**
     * Get the interfaces.
     * 
     * @return
     */
    public IClass[] getImplements() {
        if(interfaces == null) {
            return new IClass[0];
        }
        IClass[] copy = new IClass[interfaces.length];
        System.arraycopy(interfaces, 0, copy, 0, copy.length);
        return copy;
    }
    
    /**
     * create a new enum constant.
     * 
     * @param enumName
     * @return
     */
    public DummyEnum newEnum(String enumName) {
        if(enums.contains(enumName)) {
            throw new ASMSupportException("Then enum \"" + enumName + "\" type are duplicated declare.");
        }
        enums.add(enumName);
        return this;
    }
    
    /**
     * Create Constructor.
     * 
     * @return
     */
    public DummyEnumConstructor newConstructor() {
        constructorDummies.add(new DummyEnumConstructor(getClassLoader()));
        return constructorDummies.getLast();
    }
    

    /**
     * Create a static block.
     * 
     * @return
     */
    public DummyEnum newStaticBlock(EnumStaticBlockBody staticBlock) {
        if(this.staticBlock != null) {
            throw new ASMSupportException("Static Block is already existes.");
        }
        this.staticBlock = staticBlock;
        return this;
    }
    
    /**
     * Create a field
     * 
     * @return
     */
    public DummyField newField(IClass type, String name) {
        DummyField field = new DummyField(getClassLoader()); 
        fieldDummies.add(field);
        field.type(type).name(name);
        return fieldDummies.getLast();
    }
    
    /**
     * Create a field
     * 
     * @return
     */
    public DummyField newField(Class<?> type, String name) {
        DummyField field = new DummyField(getClassLoader()); 
        fieldDummies.add(field);
        field.type(type).name(name);
        return fieldDummies.getLast();
    }
    
    

    /**
     * Create a method.
     * 
     * @return
     */
    public DummyMethod newMethod(String name) {
        DummyMethod method = new DummyMethod(getClassLoader());
        methodDummies.add(method);
        method.name(name);
        return method;
    }
    


    /**
     * Build class. 
     * 
     * @return
     */
    public Class<?> build() {
    	LogFactory.LOG_FACTORY_LOCAL.remove();
        if(StringUtils.isNotBlank(logFilePath)) {
        	LogFactory.LOG_FACTORY_LOCAL.set(new LogFactory(logFilePath)); 
        } else if(printLog) {
        	LogFactory.LOG_FACTORY_LOCAL.set(new LogFactory()); 
        }
        EnumResolver eci;
        if(this.classLoader == null) {
        	eci = new EnumResolver(javaVersion,
            		StringUtils.isBlank(packageName) ? name : packageName + "." + name, interfaces);
        } else {
        	eci = new EnumResolver(javaVersion,
            		StringUtils.isBlank(packageName) ? name : packageName + "." + name, interfaces, classLoader);
        }
        		
        for(DummyEnumConstructor dummy : constructorDummies) {
            if(dummy.getConstructorBody() != null) {
                eci.createConstructor(dummy.getArgumentTypes(), dummy.getArgumentNames(), dummy.getConstructorBody().getDelegate());
            } else {
                throw new ASMSupportException("Not found body...");
            }
        }
        
        for(String enumConstant : enums) {
            eci.createEnumConstant(enumConstant);
        }
        
        for(DummyField dummy : fieldDummies) {
            if(dummy.getType() == null) {
                throw new ASMSupportException("Not specify field type for field '" +  dummy.getName() + "'");
            }
            if(dummy.getName() == null) {
                throw new ASMSupportException("Not specify field name.");
            }
            eci.createField(dummy.getName(), dummy.getModifiers(), dummy.getType(), dummy.getValue());
        }
        
        for(DummyMethod dummy : methodDummies) {
            eci.createMethod(dummy.getName(), dummy.getArgTypes(),
                    dummy.getArgNames(), 
                    dummy.getReturnType(), 
                    dummy.getThrows(), 
                    dummy.getModifiers(),
                    dummy.getMethodBody().getDelegate());
        }
        
        if(staticBlock != null) {
            eci.createStaticBlock(staticBlock.getDelegate());
        }
        eci.setClassOutputPath(classOutPutPath);
        return eci.resolve();
    }
}

/*
 *  Copyright 2008 Yuxing Huang <felix@webinit.org>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.webinit.gwt.rebind;

import java.io.PrintWriter;

import org.webinit.gwt.client.ProxyInterface;
import org.webinit.gwt.rebind.ann.EmptyImplementation;
import org.webinit.gwt.rebind.ann.OverrideProxy;
import org.webinit.gwt.rebind.ann.SkipProxy;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class InterfaceProxyGenerator extends Generator {
	
	private static String IMPLEMENTATION_SUFFIX = "_ProxiedImpl";
	private static String PROXY_SUFFIX = "_WIProxyImpl";
	private static String PROXY_OBJECT_PREFIX = "proxyObject_";
	private static String VERSION = "WebInit Interface Proxy Generator v0.1";
	
	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName)
			throws UnableToCompleteException {
		
		// retrieve the type oracle
		TypeOracle oracle = context.getTypeOracle();
		assert oracle != null;
		
		// get the implementation name from the type name
		String packageName;
		String implementationName;
		if (typeName.lastIndexOf('.') == -1) {
			packageName = "";
			implementationName = typeName + IMPLEMENTATION_SUFFIX; 
		}
		else {
			packageName= typeName.substring(0, typeName.lastIndexOf('.'));
			implementationName = typeName.substring(typeName.lastIndexOf('.')+1) + IMPLEMENTATION_SUFFIX;
		}
		
		JClassType sourceClass = oracle.findType(typeName);
		
		if (sourceClass == null) {
			logger.log(TreeLogger.ERROR, "Unable to find metadata for type '" + typeName + "'", null);
			throw new UnableToCompleteException();
		}
		
		// checking done, get the source writers

		ClassSourceFileComposerFactory oocf = new ClassSourceFileComposerFactory(
				packageName,
				implementationName);
	
		// if the source class is a Class, adds it as a superclass
		if (sourceClass.isClass() != null) {
			oocf.setSuperclass(typeName);
		}
		if (sourceClass.isInterface() != null) {
			oocf.addImplementedInterface(sourceClass.getQualifiedSourceName());
			JClassType[] implementedInterfaces = sourceClass.getImplementedInterfaces();
			for (JClassType intf: implementedInterfaces) {
				oocf.addImplementedInterface(intf.getQualifiedSourceName());
			}
		}
		
		oocf.addImplementedInterface(ProxyInterface.class.getCanonicalName());
		
		// go ahead to write source
		PrintWriter printWriter = context.tryCreate(logger, packageName, implementationName);

		if (null == printWriter) {
			return packageName + "." + implementationName;
		}
		
		logger.log(TreeLogger.INFO, "Generating " + packageName + "." + implementationName + " for "+typeName);
		
		SourceWriter sourceWriter = oocf.createSourceWriter(printWriter);
		
		sourceWriter.println("public String getWIInterfaceProxyGeneratorVersion() {");
		sourceWriter.println("\treturn \"" + VERSION + "\";");
		sourceWriter.println("}");
		
		for (JClassType intf: sourceClass.getImplementedInterfaces()) {
			JClassType targetClass = oracle.findType(intf.getQualifiedSourceName() + PROXY_SUFFIX);
			
			// the proxy implementation found
			if (targetClass != null) {
				String proxyObjectName = PROXY_OBJECT_PREFIX + intf.getSimpleSourceName();
				writeProxyObject(logger, targetClass.getQualifiedSourceName(), proxyObjectName, sourceWriter);
				// write methods
				JMethod[] methods = intf.getMethods();
				for (JMethod method: methods) {
					
					// do not write out the method if it is a tag method.
					if (method.isAnnotationPresent(SkipProxy.class)) {
						logger.log(TreeLogger.DEBUG, "Skip " + method.getReadableDeclaration() +
								" because it's skipped in the interface.");
						continue;
					}
					
					// test target class's eligibility
					final JMethod targetMethod = getMethod(targetClass, method);
					
					if (targetMethod != null && targetMethod.isAnnotationPresent(SkipProxy.class)) {
						logger.log(TreeLogger.DEBUG, "Skip " + method.getReadableDeclaration() +
								" because it's skipped in the proxy implementation.");
						continue;
					}
					
					// test source class's eligibility
					final JMethod sourceMethod = getMethod(sourceClass, method);
					if (sourceMethod != null && sourceMethod.isAnnotationPresent(OverrideProxy.class)) {
						logger.log(TreeLogger.DEBUG, "Skip " + method.getReadableDeclaration() +
								" because it's overriden in the source class.");
						continue;
					}
					
					// notify empty implementation
					if (targetMethod != null && targetMethod.isAnnotationPresent(EmptyImplementation.class))
						logger.log(TreeLogger.TRACE, "Empty implementation of " + method.getReadableDeclaration() +
								" in " + targetClass.getQualifiedSourceName() + ".\n" +
								"Please @OverrideProxy in proper classes.");
					
					
					writeMethod(logger, method, proxyObjectName, sourceWriter);
				}
			}
		}
		
		sourceWriter.println("}");
		context.commit(logger, printWriter);
		return packageName + "." + implementationName;
	}
	
	private JMethod getMethod(JClassType targetClass, JMethod method) {
		JParameter[] targetClassParameters = method.getParameters();
		JType[] targetClassParameterTypes = new JType[targetClassParameters.length];
		for (int index=0; index < targetClassParameters.length; index++) {
			targetClassParameterTypes[index] = targetClassParameters[index].getType();
		}
		try {
			JMethod targetMethod = targetClass.getMethod(method.getName(), targetClassParameterTypes);
			return targetMethod;
		}
		catch (NotFoundException nfe) {
			return null;
		}
	}
	
	private void writeProxyObject(TreeLogger log, String proxyType, String proxyObjectName, SourceWriter writer) {
		writer.println("private " + proxyType + " " + proxyObjectName + " = new " + proxyType + "();");
	}
	
	private void writeMethod(TreeLogger log, JMethod method, String proxyObjectName, SourceWriter writer) {
		StringBuffer buf = new StringBuffer();
		buf.append("public ");
		// return type
		JType returnType = method.getReturnType();
		buf.append(returnType.getQualifiedSourceName() + " ");
		// name
		buf.append(method.getName() + "(");
	
		// arguments
		JParameter[] args = method.getParameters();
		// set the first argument
		if (args.length > 0) {
			buf.append(args[0].getType().getQualifiedSourceName() + " arg0");
		}
		// following arguments if available
		for (int index = 1; index < args.length; index++) {
			JParameter arg = args[index];
			buf.append(", " + arg.getType().getQualifiedSourceName() + " arg" + index);
		}
		// parameter end
		buf.append(") ");
		
		JType[] thrs = method.getThrows();
		// set the first throw
		if (thrs.length > 0) {
			buf.append("throws " + thrs[0].getQualifiedSourceName());
		}
		// following throws
		for (int index = 1; index < thrs.length; index ++) {
			JType thr = thrs[index];
			buf.append(", " + thr.getQualifiedSourceName());
		}
		// throws end
		buf.append(" {\n");
		
		// main statement
		if (returnType.isPrimitive() != JPrimitiveType.VOID)
			buf.append("return ");
		buf.append(proxyObjectName + "." + method.getName() + "(");
		if (args.length > 0)
			buf.append("arg0");
		for (int index = 1; index < args.length; index ++)
			buf.append(", arg" + index);
		buf.append(");\n");
		buf.append("}\n");

		// write to writer
		writer.print(buf.toString());
	}

}

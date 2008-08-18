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

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class ObservableObjectGenerator extends Generator {
	
	private static String packageName = "org.webinit.gwt.client";
	private static String implementationSuffix = "_" +
			org.webinit.gwt.client.Observable.class.getSimpleName() + "Impl";
	
	private JType objectType_;

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		// retrieve the type oracle
		TypeOracle oracle = context.getTypeOracle();
		assert oracle != null;
		
		// get the implementation name from the type name
		String implementationName;
		if (typeName.lastIndexOf('.') == -1) {
			implementationName = typeName + implementationSuffix; 
		}
		else {
			implementationName = typeName.substring(typeName.lastIndexOf('.')+1) + implementationSuffix;
		}
		
		logger.log(TreeLogger.DEBUG, "Generating for "+typeName);
		
		// try to get java.lang.Object
		try {
			objectType_ = oracle.getType("java.lang.Object");
		}
		catch (NotFoundException nfe) {
			logger.log(TreeLogger.ERROR, typeName, nfe);
			return null;
		}
		
		JClassType sourceClass = oracle.findType(typeName);
		
		if (sourceClass == null) {
			logger.log(TreeLogger.ERROR, "Unable to find metadata for type '" + typeName + "'", null);
			throw new UnableToCompleteException();
		}
		
		// if target class is an interface and it has one or more unknown methods,
		// we cannot implement this interface because we cannot implement unknown methods.
		if (sourceClass.isInterface() != null) {
			JClassType targetInterface = oracle.findType(org.webinit.gwt.client.Observable.class.getCanonicalName());
			JMethod[] targetMethods = targetInterface.getMethods();
			JMethod[] methods = sourceClass.getMethods();
			int known = 0;
			for (JMethod method: methods) {
				for (JMethod targetMethod: targetMethods) {
					if (method.equals(targetMethod)) {
						known ++;
					}
				}
			}
			if (known < methods.length) {
				logger.log(TreeLogger.ERROR, "Unable to implement/extend " + typeName + ", because it has unknown methods.");
				throw new UnableToCompleteException();
			}

		}
		// target class is a class,
		// if it has unknown abstract methods, we cannot implement it.
		// if it has methods with the same signature as the observable interface, we cannot implement it.
		else {
			JClassType targetInterface = oracle.findType(org.webinit.gwt.client.Observable.class.getCanonicalName());
			JMethod[] targetMethods = targetInterface.getMethods();
			
			JMethod[] methods = sourceClass.getMethods();
			for (JMethod method: methods) {
				for (JMethod targetMethod: targetMethods) {
					if (!method.equals(targetMethod) && method.isAbstract()) {
						logger.log(TreeLogger.ERROR, "Unable to implement/extend " + typeName + ", because it has unknown method");
						throw new UnableToCompleteException();
					}
					if (method.getReadableDeclaration().equals(targetMethod.getReadableDeclaration()) && !method.isAbstract()) {
						logger.log(TreeLogger.ERROR, "Source class contains a conflict method " + method.getName());
						throw new UnableToCompleteException();
					}
				}
			}
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
		
		
		// add imports
		oocf.addImport("java.util.Map");
		oocf.addImport("java.util.Set");
		oocf.addImport("java.util.List");
		oocf.addImport("java.util.HashMap");
		oocf.addImport("java.util.HashSet");
		oocf.addImport("java.util.ArrayList");

		PrintWriter printWriter = context.tryCreate(logger, packageName, implementationName);
		SourceWriter sourceWriter = oocf.createSourceWriter(printWriter);
		
		writeObservableObjectClass(logger, sourceWriter);
		context.commit(logger, printWriter);
		
		return packageName + "." + implementationName;
	}
	
	private void writeObservableObjectClass(TreeLogger l, SourceWriter w) {
		w.println("private Map<String, List<Observer>> observers_ = null;");
		// getObservers()
		w.println("private Map<String, List<Observer>> getObservers() {");
		w.println("if (null == observers_)");
		w.println("observers_ = new HashMap<String, List<Observer>>();");
		w.println("return observers_;");
		w.println("}");
		// getObservedEvents
		w.println("public Set<String> getObservedEvents() {");
		w.println("return getObservers().keySet();");
		w.println("}");
		w.println("public List<Observer> getObserversOfEvent(String event) {");
		w.println("if (getObservers().containsKey(event)) {");
		w.println("return getObservers().get(event);");
		w.println("} else { return null; }");
		w.println("}");
		// addObserver
		w.println("public void addObserver(String event, Observer observer) {");
		w.println("List<Observer> eventObservers;");
		w.println("if (getObservers().containsKey(event)) {");
		w.println("eventObservers = getObservers().get(event);");
		w.println("} else {");
		w.println("eventObservers = new ArrayList<Observer>();");
		w.println("getObservers().put(event, eventObservers);");
		w.println("}");
		w.println("eventObservers.add(observer);");
		w.println("}");
		// removeObserver
		w.println("public boolean removeObserver(String event, Observer observer) {");
		w.println("List<Observer> eventObservers;");
		w.println("if (!getObservers().containsKey(event)) return false;");
		w.println("eventObservers = getObservers().get(event);");
		w.println("return eventObservers.remove(observer);");
		w.println("}");
		// trigger
		w.println("public boolean trigger(String event, Object object, Object arg) {");
		w.println("List<Observer> eventObservers;");
		w.println("if (!getObservers().containsKey(event)) return true;");
		w.println("eventObservers = getObservers().get(event);");
		w.println("boolean result = true;");
		w.println("for (Observer ob: eventObservers) result &= ob.observe(event, object, arg);");
		w.println("return result;");
		w.println("}");
		w.println("}");
	}

}

/*   Copyright 2004 The Apache Software Foundation
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.codehaus.annogen.generate.internal.joust;

import java.io.IOException;

/**
 * <p>A JavaOutputStream is a service which provides sequential, view-only
 * java code-generation service. This is not a general-purpose java code
 * output, but rather is tailored to produce java constructs required
 * for XMLbeans.</p>
 *
 * <p>By using this interface, the schema-to-java binding logic is isolated from
 * the details of java code generation.  This in turn allows for pluggability
 * of the generation logic - for example, one code output might generate
 * java source files, while another might directly generate byte codes in
 * memory.</p>
 *
 * <b>A note about 'Type Names'</b>
 *
 * <p>A number of method signatures in this interface contain a String parameter
 * which is described as a 'Type Name.'  This is expected to be any
 * type declaration as you would normally see in java source code, e.g.
 * <code>int</code>, <code>String[][]</code>, <code>java.util.List</code>,
 * or <code>MyImportedClass</code>.  More specifically, it must be a valid
 * <code>TypeName</code> as described in
 * <a href='http://java.sun.com/docs/books/jls/second_edition/html/names.doc.html#73064'>
 * section 6.5.5 </a> of the Java Language Specification.</p>
 *
 * @author Patrick Calahan <codehaus@bea.com>
 *
 */
public interface JavaOutputStream {

  /**
   * Instructs the stream to begin writing a new interface.
   *
   * @param packageName Fully-qualified name of the package which should
   *        contain the new interface.
   * @param interfaceOrClassName Unqualified name of the new class or
   *        interface that will be written in this file.
   *
   * @throws IllegalStateException if startFile has already been called
   *         without a call to endFile.
   * @throws IllegalArgumentException if classname is null or if any classname
   *         parameters is malformed.
   */
  public void startFile(String packageName,
                        String interfaceOrClassName)
          throws IOException;

  /**
   * Instructs the stream to begin writing a class with the given attributes.
   *
   * @param modifiers A java.lang.reflect.Modifier value describing the
   *        modifiers which apply to the new class.
   * @param extendsClassName Name the class which the new class extends, or
   *        null if it should not extend anything.  The class name must be
   *        fully-qualified.
   * @param implementsInterfaceNames Array of interface names, one
   *        for each interface implemented by the new class, or null if
   *        the class does not implement anything.  Each class name must be
   *        fully-qualified.
   *
   * @throws IllegalStateException if the current stream state does not allow
   *         beginning a new class.
   * @throws IllegalArgumentException if modifers is not valid for a class,
   *         if packagename or classname is null or malformed, or if
   *         any class name parameter is malformed.
   */
  public void startClass(int modifiers,
                         String extendsClassName,
                         String[] implementsInterfaceNames)
          throws IOException;

  /**
   * Instructs the stream to begin writing a static initialization block.
   * @throws IllegalStateException if the current position is not valid for
   *         starting a static initialization block.
   */
  public void startStaticInitializer() throws IOException;

  /**
   * Instructs the stream to begin writing a new interface.
   *
   * @param extendsInterfaceNames Array of interface names, one
   *        for each interface extendded by the new interface, or null if
   *        the interface does not extend anything.  Each class name must be
   *        fully-qualified.
   *
   * @throws IllegalStateException if the current stream state does not allow
   *         beginning a new interface.
   * @throws IllegalArgumentException if classname is null or if any classname
   *         parameters is malformed.
   */
  public void startInterface(String[] extendsInterfaceNames)
          throws IOException;

  /**
   * Instructs the stream to write out a field (member variable) declaration
   * for the current class.
   *
   * @param modifiers A java.lang.reflect.Modifier value describing the
   *        modifiers which apply to the new field.
   * @param typeName The Type Name (see above) for the new field.
   * @param fieldName The name of the new field.
   * @param defaultValue An Expression describing the default value for the
   *                     new field, or null if none should be declared.
   *
   * @throws IllegalStateException if the current stream state does not allow
   *         a field declaration (e.g. if startClass has not been called).
   * @throws IllegalArgumentException if any type name is null or malformed or
   *         fieldName is null or not a valid java identifier, or if modifiers
   *         cannot be applied to a field.
   *
   * @return A handle to the field that is created.
   */
  public Variable writeField(int modifiers,
                             String typeName,
                             String fieldName,
                             Expression defaultValue) throws IOException;

  /**
   * Instructs the stream to write out a constructor for the current class.
   *
   * @param modifiers A java.lang.reflect.Modifier value describing the
   *        modifiers which apply to the new constructor.
   * @param paramTypeNames An array of Type Names (see above) for each of the
   *        constructor's parameters, or null if this is to be the default
   *        constructor.
   * @param paramNames An array of parameter names for each of the
   *        constructor's parameters, or null if this is to be the default
   *        constructor.
   * @param exceptionClassNames An array of class names, one
   *        for each exception type to be thrown by the new constructor, or
   *        null if the constructor does not throw anything.  Each name need
   *        not be qualified.
   *
   * @throws IllegalStateException if the current stream state does not allow
   *         a constructor declaration.
   * @throws IllegalArgumentException if paramTypeNames and paramNames are
   *         not of the same length (or both null), if any type name or
   *         exception class name is null or malformed, if the modifiers
   *         cannot be applied to a constructor.
   *
   * @return An array of Variables which provide handles to the parameters
   *         of the generated constructor.  Returns an empty array if the
   *         constructor does not take any parameters.
   */
  public Variable[] startConstructor(int modifiers,
                                     String[] paramTypeNames,
                                     String[] paramNames,
                                     String[] exceptionClassNames)
          throws IOException;

  /**
   * Instructs the stream to write out a new method for the current class.
   *
   * @param modifiers A java.lang.reflect.Modifier value describing the
   *        modifiers which apply to the new method.
   * @param methodName A name for the new method.
   * @param returnTypeName A Type Name (see above) for the method's return
   *        value, or "<code>void</vode>" if the method is void.
   * @param paramTypeNames An array of Type Names (see above) for each of the
   *        method's parameters, or null if the method takes no parameters.
   * @param paramNames An array of parameter names for each of the
   *        method's parameters, or null if the method takes no parameters.
   * @param exceptionClassNames An array of class names, one
   *        for each exception type to be thrown by the method, or
   *        null if the methoddoes not throw anything.  Each class name must
   *        be fully-qualified.
   *
   * @throws IllegalStateException if the current stream state does not allow
   *         a new method declaration.
   * @throws IllegalArgumentException if paramTypeNames and paramNames are
   *         not of the same length (or both null), if any type name or
   *         exception class name is null or malformed, if the modifiers
   *         cannot be applied to a method, or if methodName is null or
   *         malformed.
   *
   * @return An array of Variables which provide handles to the parameters
   *         of the generated method.  Returns an empty array if the method
   *         does not take any parameters.
   */
  public Variable[] startMethod(int modifiers,
                                String returnTypeName,
                                String methodName,
                                String[] paramTypeNames,
                                String[] paramNames,
                                String[] exceptionClassNames)
          throws IOException;

  /**
   * <p>Writes out a source-code comment in the current class.  The comment
   * will usually be interpreted as applying to whatever is written next
   * in the stream, i.e. to write comments about a given class, you should
   * first call writeComment and then call writeClass.</p>
   *
   * <p>Line breaks in the comment will be respected, but it should NOT
   * include any formatting tokens such as '/*' or '//' - the implementation
   * will properly format the comments based on context.  Also note that
   * you should not use writeComment to add javadoc tags; code metadata
   * should be added with writeAnnotation.</p>
   *
   * @param comment The text of the comment.
   *
   * @throws IllegalStateException if the current stream state does not allow
   *         writing a comment (e.g. if startFile has not been called).
   *         writeComment should be a valid operation at all other times.
   */
  public void writeComment(String comment) throws IOException;

  /**
   * <p>Writes out an annotation in the current class.  The annotation will
   * apply to whatever is written next in the stream, i.e. to an annotation
   * for a method, call wrteAnnotation just before writing the method with
   * writeMethod.  The way in which the annotations are
   * implementation-dependent, but this typically will result in either
   * javadoc tags or JSR175 annotations being produced.</p>
   *
   * <p>Note that the caller is free to re-use the Annotation parameter object
   * after making a call to this method.</p>
   *
   * @param ann the annotation to write to the stream.
   *
   * @throws IllegalStateException if the current stream state does not allow
   *         writing an annotation (e.g. if startFile has not been called).
   *         writeComment should be a valid operation at most other times.
   */
  public void writeAnnotation(Annotation ann) throws IOException;

  /**
   * Returns an Annotation object of the given type that can be populated
   * with some values (setValue) and then passed to the writeAnnotation
   * method.
   *
   * @return An ExpressionFactory.  Must never return null.
   */
  public Annotation createAnnotation(String type);

  /**
   * Writes out an empty line, used for code formatting.
   */
  public void writeEmptyLine() throws IOException;

  /**
   * Writes out a Java statement represented by the given string.
   *
   * @param statement The string representation of the given statement
   *
   * @throws IllegalArgumentException if the statement string is null.
   */
  public void writeStatement(String statement) throws IOException;

  /**
   * Writes out an import statement of the given class.
   *
   * @param className The name of the class to be imported. Can contain '*'
   *                  for package imports.
   *
   * @throws IllegalArgumentException if className is null.
   * @throws IllegalStateException if the current stream state does not allow
   *         an import statement, ie the class definition has already started.
   */
  public void writeImportStatement(String className) throws IOException;

  /**
   * Writes out a return statement for the current method that returns
   * the given expression.
   *
   * @param  expression A handle to the expression to be returned.
   *
   * @throws IllegalArgumentException if expression is null.
   * @throws IllegalStateException if the current stream state does not allow
   *         a return declaration (e.g. if startMethod has not been called or
   *         the current method is void).
   */
  public void writeReturnStatement(Expression expression) throws IOException;

  /**
   * Writes out a return statement for the current method that returns
   * the given expression.
   *
   * @param  left A handle to the variable that goes on the left side
   *         of the equals sign.
   * @param  right A handle to the expression which goes on the right side
   *         of the equals sign.
   *
   * @throws IllegalArgumentException if either parameter is null.
   * @throws IllegalStateException if the current stream state does not allow
   *         an assignment declaration (e.g. if startMethod or
   *         startConstructor has not been called).
   */
  public void writeAssignmentStatement(Variable left, Expression right)
          throws IOException;

  /**
   * Instructs the stream to finish writing the current method or constructor.
   * Every call to startMethod or startConstructor must be balanced by a call
   * to endClassOrConstructor.
   *
   * @throws IllegalStateException if the current stream state does not allow
   *         the end of a class or interface.
   */
  public void endMethodOrConstructor() throws IOException;

  /**
   * Instructs the stream to finish writing the current class or interface.
   * Every call to startClass or startInterface must be balanced by a call to
   * endClassOrInterface.
   *
   * @throws IllegalStateException if the current stream state does not allow
   *         the end of a class or interface.
   */
  public void endClassOrInterface() throws IOException;

  /**
   * Instructs the stream to finish writing the current file.
   * Every call to startFile must be balanced by a call to endFile().
   *
   * @throws IllegalStateException if no file has been started.
   */
  public void endFile() throws IOException;

  /**
   * Closes the JavaOutputStream.  This should be called exactly once and
   * only when you are completely finished with the stream.  Note that in
   * the case where java sources are being generated, calling this method may
   * cause the sources to be javac'ed.
   */
  public void close() throws IOException;

  /**
   * Returns the ExpressionFactory that should be to create instances of
   * Expression to be used in conjunction with this JavaOutputStream.
   *
   * @return An ExpressionFactory.  Must never return null.
   */
  public ExpressionFactory getExpressionFactory();



}
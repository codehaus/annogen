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
package org.codehaus.jam.xml;

import org.codehaus.jam.JAnnotatedElement;
import org.codehaus.jam.JAnnotation;
import org.codehaus.jam.JAnnotationValue;
import org.codehaus.jam.JClass;
import org.codehaus.jam.JComment;
import org.codehaus.jam.JConstructor;
import org.codehaus.jam.JField;
import org.codehaus.jam.JInvokable;
import org.codehaus.jam.JMethod;
import org.codehaus.jam.JParameter;
import org.codehaus.jam.JSourcePosition;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.Writer;

/**
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
/*package*/ class JamXmlWriter implements JamXmlElements {

  // ========================================================================
  // Variables

  private XMLStreamWriter mOut;
  private boolean mInBody = false;
  private boolean mWriteSourceURI = false;

  // ========================================================================
  // Constructors

  public JamXmlWriter(Writer out) throws XMLStreamException  {
    if (out == null) throw new IllegalArgumentException("null out");
    mOut = XMLOutputFactory.newInstance().createXMLStreamWriter(out);
  }

  public JamXmlWriter(XMLStreamWriter out) {
    if (out == null) throw new IllegalArgumentException("null out");
    mOut = out;
  }

  // ========================================================================
  // Public methods


  public void begin() throws XMLStreamException {
    if (mInBody) throw new XMLStreamException("begin() already called");
    mOut.writeStartElement(JAMSERVICE);
    mInBody = true;
  }

  public void end() throws XMLStreamException {
    if (!mInBody) throw new XMLStreamException("begin() never called");
    mOut.writeEndElement();
    mInBody = false;
  }


  /*
  public void write(JPackage pkg) throws XMLStreamException {
    assertStarted();
    mOut.writeStartElement(PACKAGE);
    JClass[] c = pkg.getClasses();
    for(int i=0; i<c.length; i++) write(c[i]);
    writeAnnotatedElement(pkg);
    mOut.writeEndElement();
  }*/

  public void write(JClass clazz) throws XMLStreamException {
    assertStarted();
    mOut.writeStartElement(CLASS);
    writeValueElement(CLASS_NAME,clazz.getFieldDescriptor());
    writeValueElement(ISINTERFACE,clazz.isInterface());
    writeModifiers(clazz.getModifiers());
    JClass sc = clazz.getSuperclass();
    if (sc != null) writeValueElement(SUPERCLASS,sc.getFieldDescriptor());
    writeClassList(INTERFACE,clazz.getInterfaces());
    {
      JField[] f = clazz.getDeclaredFields();
      for(int i=0; i<f.length; i++) write(f[i]);
    }{
      JConstructor[] c = clazz.getConstructors();
      for(int i=0; i<c.length; i++) write(c[i]);
    }{
      JMethod[] m = clazz.getDeclaredMethods();
      for(int i=0; i<m.length; i++) write(m[i]);
    }
    //FIXME inner classes?
    writeAnnotatedElement(clazz);
    mOut.writeEndElement();
  }


  // ========================================================================
  // Private methods

  private void write(JMethod method) throws XMLStreamException {
    mOut.writeStartElement(METHOD);
    writeValueElement(NAME,method.getSimpleName());
    writeValueElement(RETURNTYPE,
                      method.getReturnType().getFieldDescriptor());
    writeInvokable(method);
    mOut.writeEndElement();
  }

  private void write(JConstructor ctor) throws XMLStreamException {
    mOut.writeStartElement(CONSTRUCTOR);
    writeInvokable(ctor);
    mOut.writeEndElement();
  }

  private void write(JField field) throws XMLStreamException {
    mOut.writeStartElement(FIELD);
    writeValueElement(NAME,field.getSimpleName());
    writeModifiers(field.getModifiers());
    writeValueElement(TYPE,field.getType().getFieldDescriptor());
    writeAnnotatedElement(field);
    mOut.writeEndElement();
  }

  private void writeInvokable(JInvokable ji) throws XMLStreamException {
    writeModifiers(ji.getModifiers());
    JParameter[] params = ji.getParameters();
    for(int i=0; i<params.length; i++) {
      mOut.writeStartElement(PARAMETER);
      writeValueElement(NAME,params[i].getSimpleName());
      writeValueElement(TYPE,params[i].getType().getFieldDescriptor());
      writeAnnotatedElement(params[i]);
      mOut.writeEndElement();
    }
    writeAnnotatedElement(ji);
  }

  private void writeClassList(String elementName, JClass[] clazzes)
    throws XMLStreamException
  {
    for(int i=0; i<clazzes.length; i++) {
      mOut.writeStartElement(elementName);
      mOut.writeCharacters(clazzes[i].getFieldDescriptor());
      mOut.writeEndElement();
    }
  }

  private void writeModifiers(int mods) throws XMLStreamException {
    mOut.writeStartElement(MODIFIERS);
    mOut.writeCharacters(String.valueOf(mods));
    mOut.writeEndElement();
  }

  private void writeValueElement(String elementName, boolean b)
    throws XMLStreamException
  {
    mOut.writeStartElement(elementName);
    mOut.writeCharacters(String.valueOf(b));
    mOut.writeEndElement();
  }

  private void writeValueElement(String elementName, int x)
    throws XMLStreamException
  {
    mOut.writeStartElement(elementName);
    mOut.writeCharacters(String.valueOf(x));
    mOut.writeEndElement();
  }

  private void writeValueElement(String elementName, String val)
    throws XMLStreamException
  {
    mOut.writeStartElement(elementName);
    mOut.writeCharacters(val);
    mOut.writeEndElement();
  }

  private void writeValueElement(String elementName, String[] vals)
    throws XMLStreamException
  {
    for(int i=0; i<vals.length; i++) writeValueElement(elementName,vals[i]);
  }


  private void writeAnnotatedElement(JAnnotatedElement ae)
    throws XMLStreamException
  {
    JAnnotation[] anns = ae.getAnnotations();
    for(int i=0; i<anns.length; i++) {
      writeAnnotation(anns[i]);
    }
    JComment jc = ae.getComment();
    if (jc != null) {
      String text = jc.getText();
      if (text != null) {
        text = text.trim();
        if (text.length() > 0) {
          mOut.writeStartElement(COMMENT);
          mOut.writeCData(jc.getText());
          mOut.writeEndElement();
        }
      }
    }
    JSourcePosition pos = ae.getSourcePosition();
    if (pos != null) {
      mOut.writeStartElement(SOURCEPOSITION);
      if (pos.getLine() != -1) {
        writeValueElement(LINE,pos.getLine());
      }
      if (pos.getColumn() != -1) {
        writeValueElement(COLUMN,pos.getColumn());
      }
      if (mWriteSourceURI && pos.getSourceURI() != null)
        writeValueElement(SOURCEURI,pos.getSourceURI().toString());
      mOut.writeEndElement();
    }
  }

  private void writeAnnotation(JAnnotation ann) throws XMLStreamException {
    mOut.writeStartElement(ANNOTATION);
    writeValueElement(NAME,ann.getQualifiedName());
    JAnnotationValue[] values = ann.getValues();
    for(int i=0; i<values.length; i++) {
      writeAnnotationValue(values[i]);
    }
    mOut.writeEndElement();
  }

  private void writeAnnotationValue(JAnnotationValue val)
    throws XMLStreamException
  {
    mOut.writeStartElement(ANNOTATIONVALUE);
    writeValueElement(NAME,val.getName());
    writeValueElement(TYPE,val.getType().getFieldDescriptor());
    if (val.getType().isArrayType()) {
      writeValueElement(VALUE,val.asStringArray());
    } else {
      writeValueElement(VALUE,val.asString());
    }

    mOut.writeEndElement();
    //FIXME what about asAnnotationArray?
/*    JAnnotation nestedAnn = val.asAnnotation();
    if (nestedAnn != null) {
      writeAnnotation(nestedAnn);
    } else {
      writeValueElement(VALUE,val.asString());
    }
    */
  }

  private void assertStarted() throws XMLStreamException {
    if (!mInBody) throw new XMLStreamException("begin() not called");
  }

}
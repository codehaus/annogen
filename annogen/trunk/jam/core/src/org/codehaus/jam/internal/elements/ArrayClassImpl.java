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
package org.codehaus.jam.internal.elements;

import org.codehaus.jam.JClass;
import org.codehaus.jam.JamClassLoader;

import java.io.StringWriter;


/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public final class ArrayClassImpl extends BuiltinClassImpl {

  // ========================================================================
  // Variables

  private int mDimensions;
  private JClass mComponentType;

  // ========================================================================
  // Factory methods


  /**
   * Creates an array JClass from a field descriptor as described in the JLS.
   * This is the nasty '[[[Lfoo.bar.Baz;'-style notation.
   */
  public static JClass createClassForFD(String arrayFD, JamClassLoader loader)
  {
    if (!arrayFD.startsWith("[")) {
      throw new IllegalArgumentException("must be an array type fd: "+arrayFD);
    }
    String componentType;
    if (arrayFD.endsWith(";")) {
      // if it's an array of complex types, we need to construct
      // an ArrayClassImpl wrapper and go back into the context to
      // get the component type, since a source description for it
      // might be available
      int dims = arrayFD.indexOf("L");
      if (dims != -1 && dims<arrayFD.length()-2) {
        componentType = arrayFD.substring(dims+1,arrayFD.length()-1);
        return new ArrayClassImpl(loader.loadClass(componentType),dims);
      } else {
        // name is effed
        throw new IllegalArgumentException("array type field descriptor '"+
                                           arrayFD+"' is malformed");
      }
    } else {
      int dims = arrayFD.lastIndexOf("[")+1;
      String compFd = arrayFD.substring(dims,dims+1);
      JClass primType = loader.loadClass(compFd);
      if (primType == null) {
        // if it didn't end with ';', it has to be a valid primitive
        // type name or it's effed
        throw new IllegalArgumentException("array type field descriptor '"+
                                           arrayFD+"' is malformed");
      }
      return new ArrayClassImpl(primType,dims);
    }
  }

  /**
   * Returns the normal form for a given array name.  This is a trimmed,
   * unspaced field descriptor, e.g. '[[[Ljava.lang.String;' for a three
   * dimensional array of strings.  This method will also normalize
   * understands declaration-style array names, e.g. 'java.lang.String[][][]'.
   */
  public static String normalizeArrayName(String declaration) {
    //REVIEW should we worry about internal spaces?
    if (declaration.startsWith("[")) return declaration;
    if (declaration.endsWith("]")) {
      int bracket = declaration.indexOf('[');
      if (bracket != -1) {
        String typeName = declaration.substring(0,bracket);
        String fd = PrimitiveClassImpl.getPrimitiveClassForName(typeName);
        if (fd == null) fd = 'L'+typeName+';';
        StringWriter out = new StringWriter();
        do {
          out.write('[');
          bracket = declaration.indexOf('[',bracket+1);
        } while(bracket != -1);
        out.write(fd);
        return out.toString();
      }
    }
    throw new IllegalArgumentException("'"+declaration+
                                       "' does not name an array");
  }



  // ========================================================================
  // Constructors - use factory method

  /**
   * Constructs a JDClass for the given ClassDoc in the given context.
   */
  private ArrayClassImpl(JClass componentType, int dimensions)
  {
    super(((ElementImpl)componentType).getContext());
    if (dimensions < 1) {
      throw new IllegalArgumentException("dimensions="+dimensions);
    }
    if (componentType == null) {
      throw new IllegalArgumentException("null componentType");
    }
    mComponentType = componentType;
    mDimensions = dimensions;
  }

  // ========================================================================
  // JElement implementation

//  public JElement getParent() { return null; }

  public String getSimpleName() {
    String out = getQualifiedName();
    int lastDot = out.lastIndexOf('.');
    return (lastDot == -1) ? out : out.substring(lastDot+1);
  }

  public String getQualifiedName() {
    StringWriter out = new StringWriter();
    out.write(mComponentType.getQualifiedName());
    for(int i=0; i<mDimensions; i++) out.write("[]");
    return out.toString();
  }

  // ========================================================================
  // JClass implementation

  public boolean isArrayType() { return true; }

  public JClass getArrayComponentType() { return mComponentType; }

  public int getArrayDimensions() { return mDimensions; }

  public JClass getSuperclass() {
    return getClassLoader().loadClass("java.lang.Object");
  }

  public boolean isAssignableFrom(JClass c) {
    return c.isArrayType() &&
            (c.getArrayDimensions() == mDimensions) &&
            (mComponentType.isAssignableFrom(c.getArrayComponentType()));
  }

  public String getFieldDescriptor() {
    //REVIEW should we cache this result?
    StringWriter out = new StringWriter();
    for(int i=0; i<mDimensions; i++) out.write("[");
    if (mComponentType.isPrimitiveType()) {
      out.write(mComponentType.getFieldDescriptor());
    } else {
      out.write("L");
      out.write(mComponentType.getQualifiedName());
      out.write(";");
    }
    return out.toString();
  }



}

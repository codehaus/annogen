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
package org.codehaus.jam;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

/**
 * <p>Singleton collection of utility methods which can be very useful in
 * some samples, but not often enough to warrant inclusion in the main
 * APIs.  </p>
 *
 * <p>Most of these are here to help you jump from the JClass
 * hierarchy to the java.lang.reflect hierarchy.  This is primarily
 * useful when you find yourself, for example, wanting to actually invoke
 * a method represented by a JMethod.</p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class JamUtils {

  // ========================================================================
  // Singleton

  public static final JamUtils getInstance() { return INSTANCE; }

  private JamUtils() {}

  private static final JamUtils INSTANCE = new JamUtils();


  // ========================================================================
  // Public methods

  /**
   * <p>Returns the java.lang.Method on a given
   * java.lang.Class which is represented by a given JMethod.</p>
   *
   * @param containedin Class to be searched for the method.
   * @return
   * @throws ClassNotFoundException if one of the paramType classes specified
   * for this MethodName cannot be loaded from the given class' classloader.
   * @throws NoSuchMethodException If the named method does not exist on
   * this class.
   * @throws IllegalArgumentException if any argument is null
   */
  public Method getMethodOn(JMethod method, Class containedin)
    throws NoSuchMethodException, ClassNotFoundException
  {
    if (containedin == null) throw new IllegalArgumentException("null class");
    if (method == null) throw new IllegalArgumentException("null method");
    Class[] types = null;
    JParameter[] params = method.getParameters();
    if (params != null && params.length > 0) {
      types = new Class[params.length];
      for (int i = 0; i < types.length; i++) {
        types[i] = loadClass(params[i].getType(),containedin.getClassLoader());
      }
    }
    return containedin.getMethod(method.getSimpleName(), types);
  }

  /**
   * <p>Returns the java.lang.Method on a given
   * java.lang.Class which is represented by a given JMethod.</p>
   *
   * @param containedin Class to be searched for the ctor.
   * @return
   * @throws ClassNotFoundException if one of the paramType classes specified
   * for this MethodName cannot be loaded from the given class' classloader.
   * @throws NoSuchMethodException If the named ctor does not exist on
   * this class.
   * @throws IllegalArgumentException if any argument is null
   */
  public Constructor getConstructorOn(JConstructor ctor, Class containedin)
    throws NoSuchMethodException, ClassNotFoundException
  {
    if (containedin == null) throw new IllegalArgumentException("null class");
    if (ctor == null) throw new IllegalArgumentException("null ctor");
    Class[] types = null;
    JParameter[] params = ctor.getParameters();
    if (params != null && params.length > 0) {
      types = new Class[params.length];
      for (int i = 0; i < types.length; i++) {
        types[i] = loadClass(params[i].getType(),containedin.getClassLoader());
      }
    }
    return containedin.getConstructor(types);
  }

  /**
   * <p>Returns the java.lang.Method on a given
   * java.lang.Class which is represented by a given JMethod.</p>
   *
   * @param containedin Class to be searched for the method.
   * @return
   * @throws NoSuchFieldException if the field does not exist on the class.
   * @throws IllegalArgumentException if any argument is null
   */
  public Field getFieldOn(JField field, Class containedin)
    throws NoSuchFieldException
  {
    if (containedin == null) throw new IllegalArgumentException("null class");
    if (field == null) throw new IllegalArgumentException("null field");
    return containedin.getField(field.getSimpleName());
  }


  /**
   * <p>Loads the java.lang.Class represented by a given JClass out of a given
   * classloader.</p>
   * @return
   * @throws ClassNotFoundException If the class is not found in the classloader
   */
  public Class loadClass(JClass clazz, ClassLoader inThisClassloader)
    throws ClassNotFoundException
  {
    return inThisClassloader.loadClass(clazz.getQualifiedName());
  }

  /**
   * Sorts the given array in place so that the elements are ordered by
   * their sourcePosition's line numbers.
   */
  public void placeInSourceOrder(JElement[] elements) {
    if (elements == null) throw new IllegalArgumentException("null elements");
    Arrays.sort(elements,SOURCE_POSITION_COMPARATOR);
  }

  // ========================================================================
  // Private

  private static Comparator SOURCE_POSITION_COMPARATOR = new Comparator() {

    public int compare(Object o, Object o1) {
      JSourcePosition p1 = ((JElement)o).getSourcePosition();
      JSourcePosition p2 = ((JElement)o1).getSourcePosition();
      if (p1 == null) return (p2 == null) ? 0 : -1;
      if (p2 == null) return 1;
      return (p1.getLine() < p2.getLine()) ? -1 :
        (p1.getLine() > p2.getLine()) ? 1 : 0;
    }
  };


}
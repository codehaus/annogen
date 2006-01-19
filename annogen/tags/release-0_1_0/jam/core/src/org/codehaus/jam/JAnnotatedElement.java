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

/**
 * <p>Base abstraction for JElements which can carry annotations and comments.
 * The only JElements which cannot do this are JAnnotation and JComment.</p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public abstract interface JAnnotatedElement extends JElement {

  // ========================================================================
  // Public methods

  /**
   * <p>Returns the metadata JAnnotations that are associated with
   * this abstraction.  Returns an empty array if there are no
   * annotations.</p>
   */
  public JAnnotation[] getAnnotations();

  /**
   * <p>Returns the JAnnotation representing the annotation on this element of
   * the given JSR 175 annotation type, or null if no such annotation exists.
   * </p>
   *
   * @throws IllegalArgumentException if the jsr175type parameter is null
   * or not a 175 type.
   */
  public JAnnotation getAnnotation(Class jsr175type);

  /**
   * <p>Finds an annotation on this element according to the following
   * rules:
   *
   * <ul>
   *   <li>If the element as a JSR175 annotation of type 'named',
   *   returns a JAnnotation for it.</li>
   *   <li>If a javadoc tag exists named 'named' returns a JAnnotation for
   *   it.  If more than such javadoc tags exists, one is chosen (no
   *   guarantees are made as to which.  For handling multiple javadoc
   *   tags, please use getAllJavadocTags().
   * </ul>
   */
  public JAnnotation getAnnotation(String named);
  // REVIEW we should consider breaking this up and deprecating this method.
  // Could have getJavadocTag(named) and get175Annotation(named).

  /**
   * Shortcut method which returns a given annotation value.  The 'valueId'
   * should be a string of the format 'annotation-name@value-name'.  The
   * value-name may be ommitted; if it is, it defaults to
   * JAnntoation.SINGLE_MEMBER_VALUE.
   *
   * @param valueId
   * @return
   */
  public JAnnotationValue getAnnotationValue(String valueId);

  /**
   * <p>Returns the comment associated with this abstraction.
   * Returns null if it has no comment.</p>
   */
  public JComment getComment();

  /**
   * Returns JAnnotations representing all of the javadoc tags on this
   * element (including multiple tags with the same name).
   */
  public JAnnotation[] getAllJavadocTags();


}

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

import java.util.Properties;

/**
 * Represents a javadoc tag.  Includes helper methods for parsing tags
 * containing complex name-value data.  Different folks have different
 * styles of encoding the pairs, so we provide different helper methods.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface JTag extends JElement {

  /**
   * Returns the name of the tag.  The value will be the same as
   * getSimpleName();
   */
  public String getName();

  /**
   * Returns the raw contents of the tag, as return by javadoc's
   * Tag.text() method.
   */
  public String getText();

  /**
   * <p>Convenience method which parses the tag's contents as a series of
   * line-delimited name=value pairs and returns them as a properties
   * object.  A line break is the only delimter between pairs, and
   * the first '=' is taken as the delimeter between name and value.</p>
   *
   * For example, a javadoc tag 'my tag' with the following
   * value:
   *
   * (at)mytag  foo = this
   *            bar = "and" that
   *            baz = the other thing = true
   *
   * <p>would be returned as a Properties object containing three properties
   * named foo, bar, and baz with values of "this", ""and"" that, and "the
   * other thing = true", respectively.</p>
   */
  public Properties getProperties_lineDelimited();

  /**
   * <p>Convenience method which parses the tag's contents as a series of
   * whitespace-delimited name=value pairs and returns them as a properties
   * object.  Values containing whitespace must be quoted.  Keys may not
   * contain whitespace</p>
   *
   * For example, a javadoc tag 'my tag' with the following contents:
   *
   * (at)mytag  foo = this  bar = that this text is
   *           ignored baz = "but not this"
   *
   * <p>would be returned as a Properties object containing three properties
   * named foo, bar, and baz with values of "this", "that", and
   * "but not this".
   */
  public Properties getProperties_whitespaceDelimited();
}
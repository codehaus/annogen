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
package org.codehaus.jam.test.samples;

import org.codehaus.jam.JClass;
import javax.xml.stream.XMLStreamException;
import java.util.Properties;

import java.lang.*;
import java.lang.reflect.*;
import java.util.*;
import javax.xml.stream.*;
import org.codehaus.jam.*;

/**
 * A test case class for checking that imports function correctly.
 *
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public interface ImportsGalore {

  public Properties justAMethodToEnsureIdesDontCleanupImports(JClass clazz,
                                                              String str,
                                                              Method meth)
    throws XMLStreamException;

}

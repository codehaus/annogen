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

import org.codehaus.jam.JClass;
import org.codehaus.jam.JamService;
import org.codehaus.jam.JamServiceFactory;
import org.codehaus.jam.JamServiceParams;
import org.codehaus.jam.internal.CachedClassBuilder;
import org.codehaus.jam.internal.JamServiceImpl;
import org.codehaus.jam.internal.elements.ElementContext;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

/**
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public class JamXmlUtils {

  // ========================================================================
  // Singleton

  public static final JamXmlUtils getInstance() { return INSTANCE; }

  private static final JamXmlUtils INSTANCE = new JamXmlUtils();

  private JamXmlUtils() {}

  // ========================================================================
  // Public methods

  public JamService createService(InputStream in)
    throws IOException, XMLStreamException
  {
    if (in == null) throw new IllegalArgumentException("null stream");
    JamServiceFactory jsf = JamServiceFactory.getInstance();
    JamServiceParams params = jsf.createServiceParams();
    CachedClassBuilder cache = new CachedClassBuilder();
    // finish initalizing the params and create the service
    params.addClassBuilder(cache);
    JamService out = jsf.createService(params);
    // now go view the xml.  we have to do this afterwards so that the
    // classloader has been created and is available for linking.
    JamXmlReader reader = new JamXmlReader(cache,in,(ElementContext)params);
    reader.read();
    {
      // slightly gross hack to get the class names into the service
      List classNames = Arrays.asList(cache.getClassNames());
      classNames.addAll(Arrays.asList(out.getClassNames()));
      String[] nameArray = new String[classNames.size()];
      classNames.toArray(nameArray);
      ((JamServiceImpl)out).setClassNames(nameArray);
    }
    return out;
  }

  public void toXml(JClass[] clazzes, Writer writer)
    throws IOException, XMLStreamException
  {
    if (clazzes == null) throw new IllegalArgumentException("null classes");
    if (writer == null) throw new IllegalArgumentException("null writer");
    JamXmlWriter out = new JamXmlWriter(writer);
    out.begin();
    for(int i=0; i<clazzes.length; i++) out.write(clazzes[i]);
    out.end();
  }

}

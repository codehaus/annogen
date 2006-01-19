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

import java.io.StringWriter;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
/*package*/ class LinebreakTagPropertyParser {

  // ========================================================================
  // Singleton

  public static LinebreakTagPropertyParser getInstance() { return INSTANCE; }

  private static LinebreakTagPropertyParser INSTANCE =
      new LinebreakTagPropertyParser();

  private LinebreakTagPropertyParser() {}

  // ========================================================================
  // Constants

  private static final String VALUE_QUOTE = "\"";
  private static final String LINE_DELIMS = "\n\f\r";


  // ========================================================================
  // JavadocTagParser implementation

  public void parse(Properties out, String tagText) {
    if (out == null) throw new IllegalArgumentException("null out");
    if (tagText == null) throw new IllegalArgumentException("null text");
    StringTokenizer st = new StringTokenizer(tagText, LINE_DELIMS);
    while (st.hasMoreTokens()) {
      String pair = st.nextToken();
      int eq = pair.indexOf('=');
      if (eq <= 0) continue; // if absent or is first character
      String name = pair.substring(0, eq).trim();
      if (eq < pair.length() - 1) {
        String value = pair.substring(eq + 1).trim();
        if (value.startsWith(VALUE_QUOTE)) {
          value = parseQuotedValue(value.substring(1),st);
        }
        out.setProperty(name,value);
      }
    }
  }

  // ========================================================================
  // Private methods

  private String parseQuotedValue(String line,
                                  StringTokenizer st) {
    StringWriter out = new StringWriter();
    while(true) {
      int endQuote = line.indexOf(VALUE_QUOTE);
      if (endQuote == -1) {
        out.write(line);
        if (!st.hasMoreTokens()) return out.toString();
        out.write('\n');
        line = st.nextToken().trim();
        continue;
      } else {
        out.write(line.substring(0,endQuote).trim());
        return out.toString();
      }
    }
  }

}

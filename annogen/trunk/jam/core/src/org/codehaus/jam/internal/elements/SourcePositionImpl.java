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

import org.codehaus.jam.mutable.MSourcePosition;

import java.net.URI;

/**
 * <p>Implementation of JSourcePosition and MSourcePosition.</p>
 *
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public final class SourcePositionImpl implements MSourcePosition {

  // ========================================================================
  // Variables

  private int mColumn = -1;
  private int mLine = -1;
  private URI mURI = null;

  // ========================================================================
  // Constructors

  /*package*/ SourcePositionImpl() {}

  // ========================================================================
  // MSourcePosition implementation

  public void setColumn(int col) { mColumn = col; }

  public void setLine(int line) { mLine = line; }

  public void setSourceURI(URI uri) { mURI = uri; }

  // ========================================================================
  // JSourcePosition implementation

  public int getColumn() { return mColumn; }

  public int getLine() { return mLine; }

  public URI getSourceURI() { return mURI; }
}
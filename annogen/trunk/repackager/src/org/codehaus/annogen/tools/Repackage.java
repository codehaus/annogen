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

package org.codehaus.annogen.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Repackage
{
    public static void main ( String[] args ) throws Exception
    {
        new Repackage(args).repackage();
    }
    
    private Repackage(String[] args)
    {
        String sourceDir = null;
        String targetDir = null;
        String repackageSpec = null;
        boolean failure = false;
        
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equals("-repackage") && i + 1 < args.length)
                repackageSpec = args[++i];
            else if (args[i].equals("-f") && i + 1 < args.length)
                sourceDir = args[++i];
            else if (args[i].equals("-t") && i + 1 < args.length)
                targetDir = args[++i];
            else
                failure = true;
        }
        
        if (failure || sourceDir == null || targetDir == null || repackageSpec == null)
            throw new RuntimeException("Usage: repackage -repackage [spec] -f [sourcedir] -t [targetdir]");
        
        _repackager = new Repackager(repackageSpec);
        
        _sourceBase = new File(sourceDir);
        _targetBase = new File(targetDir);
    }
  
    
    public void repackage () throws Exception
    {
        _fromPackages = _repackager.getFromPackages();
        _toPackages = _repackager.getToPackages();
        
        _packagePattern =
            Pattern.compile( "^\\s*package\\s+((?:\\w|\\.)*)\\s*;", Pattern.MULTILINE );
        
        _moveAlongFiles = new ArrayList();
        _movedDirs = new HashMap();
        
//        System.out.println( "Deleting repackage dir ..." );
//        recursiveDelete( _targetBase );
        
        _targetBase.mkdirs();
        
        ArrayList files = new ArrayList();

        fillFiles( files, _sourceBase );
        
        System.out.println( "Repackaging " + files.size() + " files ..." );

        int prefixLength = _sourceBase.getCanonicalPath().length();

        for ( int i = 0 ; i < files.size() ; i++ )
        {
            File from = (File) files.get( i );

            String name = from.getCanonicalPath().substring( prefixLength + 1 );

            repackageFile( name );
        }
        
        finishMovingFiles();
        
        if (_skippedFiles > 0)
            System.out.println("Skipped " + _skippedFiles + " unmodified files.");
    }
    
    private boolean fileIsUnchanged(String name)
    {
        File sourceFile = new File( _sourceBase, name );
        File targetFile = new File( _targetBase, name );
        return (sourceFile.lastModified() < targetFile.lastModified());
    }

    public void repackageFile ( String name )
        throws IOException
    {
        if (name.endsWith( ".java" ))
            repackageJavaFile( name );
        else if (name.endsWith( ".xsdconfig" ) ||
                (name.endsWith(".xml") &&
                //REVIEW skip repackaging the main build file because it
                //does some complicated things already and we don't want
                //to mess with it right now.  This feels a little gross,
                //though.  codehaus 2/18/04
                !name.equals("build.xml")) ||
                name.endsWith(".g") )
            repackageNonJavaFile( name );
        else if (name.startsWith( "bin" + File.separatorChar ))
            repackageNonJavaFile( name );
        else
            moveAlongWithJavaFiles( name );
    }

    public void moveAlongWithJavaFiles( String name )
    {
        _moveAlongFiles.add(name);
    }
    
    public void finishMovingFiles ( )
        throws IOException
    {
        for ( Iterator i = _moveAlongFiles.iterator(); i.hasNext(); )
        {
            String name = (String) i.next();
            String toName = name;
            
            String srcDir = Repackager.dirForPath( name );
            String toDir = (String) _movedDirs.get( srcDir );
            
            if (toDir != null)
                toName = new File( toDir, new File( name ).getName() ).toString(); 

            if (name.endsWith( ".html" ))
                repackageNonJavaFile(name, toName);
            else
                justMoveNonJavaFile(name, toName);
        }
    }

    public void repackageNonJavaFile(String name)
        throws IOException
    {
        File sourceFile = new File( _sourceBase, name );
        File targetFile = new File( _targetBase, name );
        
        if (sourceFile.lastModified() < targetFile.lastModified())
            _skippedFiles += 1;
        else
            writeFile( targetFile, _repackager.repackage( readFile( sourceFile ) ) );
    }
    
    public void repackageNonJavaFile ( String sourceName, String targetName )
        throws IOException
    {
        File sourceFile = new File( _sourceBase, sourceName );
        File targetFile = new File( _targetBase, targetName );
        
        if (sourceFile.lastModified() < targetFile.lastModified())
            _skippedFiles += 1;
        else
            writeFile( targetFile, _repackager.repackage( readFile( sourceFile ) ) );
    }
    
    public void justMoveNonJavaFile ( String sourceName, String targetName )
        throws IOException
    {
        File sourceFile = new File( _sourceBase, sourceName );
        File targetFile = new File( _targetBase, targetName );
        
        if (sourceFile.lastModified() < targetFile.lastModified())
            _skippedFiles += 1;
        else
            copyFile( sourceFile, targetFile );
    }
    
    public void repackageJavaFile ( String name )
        throws IOException
    {
        File sourceFile = new File(_sourceBase, name);
        StringBuffer sb = readFile(sourceFile);

        Matcher packageMatcher = _packagePattern.matcher( sb );

        if (packageMatcher.find())
        {
            String pkg = packageMatcher.group( 1 );
            int pkgStart = packageMatcher.start( 1 );
            int pkgEnd = packageMatcher.end( 1 );
            
            if (packageMatcher.find())
                throw new RuntimeException( "Two package specifications found: " + name );
            
            List filePath = Repackager.splitPath( name, File.separatorChar );
            String srcDir = Repackager.dirForPath( name );
            
            // Sort the repackage spec so that longer from's are first to match
            // longest package first

            for ( ; ; )
            {
                boolean swapped = false;

                for ( int i = 1 ; i < filePath.size() ; i++ )
                {
                    String spec1 = (String) filePath.get( i - 1 );
                    String spec2 = (String) filePath.get( i );

                    if (spec1.indexOf( ':' ) < spec2.indexOf( ':' ))
                    {
                        filePath.set( i - 1, spec2 );
                        filePath.set( i, spec1 );

                        swapped = true;
                    }
                }

                if (!swapped)
                    break;
            }

            List pkgPath = Repackager.splitPath( pkg, '.' );

            int f = filePath.size() - 2;

            for ( int i = pkgPath.size() - 1 ; i >= 0 ; i-- )
            {
                if (!pkgPath.get( i ).equals( filePath.get( f-- ) ))
                    throw new RuntimeException( "Package spec differs from file path: " + name );
            }

            List changeTo = null;
            List changeFrom = null;
            
            from:
            for ( int i = 0 ; i < _fromPackages.size() ; i ++ )
            {
                List from = (List) _fromPackages.get( i );

                if (from.size() <= pkgPath.size())
                {
                    for ( int j = 0 ; j < from.size() ; j++ )
                        if (!from.get( j ).equals( pkgPath.get( j )))
                            continue from;

                    changeFrom = from;
                    changeTo = (List) _toPackages.get( i );

                    break;
                }
            }

            if (changeTo != null)
            {
                String newPkg = "";
                String newName = "";

                for ( int i = 0 ; i < changeTo.size() ; i++ )
                {
                    if (i > 0)
                    {
                        newPkg += ".";
                        newName += File.separatorChar;
                    }
                    
                    newPkg += changeTo.get( i );
                    newName += changeTo.get( i );
                }
                
                for ( int i = filePath.size() - pkgPath.size() - 2 ; i >= 0 ; i-- )
                    newName = (String) filePath.get( i ) + File.separatorChar + newName;

                for ( int i = changeFrom.size() ; i < pkgPath.size() ; i++ )
                {
                    newName += File.separatorChar + (String) pkgPath.get( i );
                    newPkg += '.' + (String) pkgPath.get( i );
                }

                newName += File.separatorChar + (String) filePath.get( filePath.size() - 1 );

                sb.replace( pkgStart, pkgEnd, newPkg );

                name = newName;
                String newDir = Repackager.dirForPath( name );
                
                if (!srcDir.equals(newDir))
                {
                    _movedDirs.put(srcDir, newDir);
                }
            }
        }
        
        File targetFile = new File(_targetBase, name); // new name
        
        if (sourceFile.lastModified() < targetFile.lastModified())
        {
            _skippedFiles += 1;
            return;
        }

        writeFile( new File( _targetBase, name ), _repackager.repackage( sb ) );
    }

    void writeFile ( File f, StringBuffer chars )
        throws IOException
    {
        f.getParentFile().mkdirs();
        
        OutputStream out = new FileOutputStream( f );
        Writer w = new OutputStreamWriter( out );
        Reader r = new StringReader( chars.toString() );

        copy( r, w );

        r.close();
        w.close();
        out.close();
    }
    
    StringBuffer readFile ( File f )
        throws IOException
    {
        InputStream in = new FileInputStream( f );
        Reader r = new InputStreamReader( in );
        StringWriter w = new StringWriter();

        copy( r, w );

        w.close();
        r.close();
        in.close();

        return w.getBuffer();
    }
    
    public void copyFile ( File from, File to ) throws IOException
    {
        to.getParentFile().mkdirs();
        
        FileInputStream in = new FileInputStream( from );
        FileOutputStream out = new FileOutputStream( to );

        copy( in, out );
        
        out.close();
        in.close();
    }
    
    public void copy ( InputStream in, OutputStream out ) throws IOException
    {
        byte[] buffer = new byte [ 1024 * 16 ];

        for ( ; ; )
        {
            int n = in.read( buffer, 0, buffer.length );

            if (n < 0)
                break;

            out.write( buffer, 0, n );
        }
    }
    
    public void copy ( Reader r, Writer w ) throws IOException
    {
        char[] buffer = new char [ 1024 * 16 ];

        for ( ; ; )
        {
            int n = r.read( buffer, 0, buffer.length );

            if (n < 0)
                break;

            w.write( buffer, 0, n );
        }
    }
    
    public void fillFiles ( ArrayList files, File file ) throws IOException
    {
        if (!file.isDirectory())
        {
            files.add( file );
            return;
        }

        // Exclude the build directory

        if (file.getName().equals( "build" ))
            return;
        
        // Exclude CVS directories
        if (file.getName().equals( "CVS" ))
            return;

        String[] entries = file.list();

        for ( int i = 0 ; i < entries.length ; i++ )
            fillFiles( files, new File( file, entries[ i ] ) );
    }

    public void recursiveDelete ( File file ) throws IOException
    {
        if (!file.exists())
            return;

        if (file.isDirectory())
        {
            String[] entries = file.list();

            for ( int i = 0 ; i < entries.length ; i++ )
                recursiveDelete( new File( file, entries[ i ] ) );
        }

        file.delete();
    }

    private File _sourceBase;
    private File _targetBase;

    private List _fromPackages;
    private List _toPackages;
    
    private Pattern _packagePattern;

    private Repackager _repackager;
    
    private Map _movedDirs;
    private List _moveAlongFiles;
    private int _skippedFiles;
}

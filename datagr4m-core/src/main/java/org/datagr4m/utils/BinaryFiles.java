package org.datagr4m.utils;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ProgressMonitorInputStream;

public class BinaryFiles {
    public static <O> void save(String filename, O o) throws IOException {
        File parent = new File(filename).getParentFile();
        if (parent != null) {
            if (!parent.exists())
                parent.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(o);
        out.close();
    }
    
    /*public static <O> void save(String filename, O o, Component parentComponent, String message) throws IOException {
        File parent = new File(filename).getParentFile();
        if (parent != null) {
            if (!parent.exists())
                parent.mkdirs();
        }
        
        FileOutputStream fos = new FileOutputStream(filename);
        
        ObjectOutputStream out;
        if(parentComponent!=null){
            final ProgressMonitorOutputStream pm = new ProgressMonitorOutputStream(parentComponent, message, fos);
            out = new ObjectOutputStream(pm);
        }
        else{
            out = new ObjectOutputStream(fos);
        }
        
        out.writeObject(o);
        out.close();
    }*/

    @SuppressWarnings("unchecked")
    public static <O> O load(String filename) throws IOException, ClassNotFoundException {
        return (O) load(filename, null, null);
    }

    /** Performs loading a serialized java object by poping a progress monitor.*/
    @SuppressWarnings("unchecked")
    public static <O> O load(String filename, Component parentComponent, String message) throws IOException, ClassNotFoundException {
        final FileInputStream fis = new FileInputStream(filename);
        final ObjectInputStream in;
        
        if(parentComponent!=null){
            final ProgressMonitorInputStream pm = new ProgressMonitorInputStream(parentComponent, message, fis);
            in = new ObjectInputStream(pm);
        }
        else{
            in = new ObjectInputStream(fis);
        }
        
        O r = (O) in.readObject();
        in.close();
        fis.close();
        return r;
    }

    public static boolean exists(String filename) {
        return new File(filename).exists();
    }
}

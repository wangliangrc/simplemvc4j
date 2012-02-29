package com.clark.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;

/**
 * A special ObjectInputStream that loads a class based on a specified
 * <code>ClassLoader</code> rather than the system default.
 * <p>
 * This is useful in dynamic container environments.
 * 
 * @author Paul Hammant
 * @version $Id: ClassLoaderObjectInputStream.java 736890 2009-01-23 02:02:22Z
 *          niallp $
 * @since Commons IO 1.1
 */
public class ClassLoaderObjectInputStream extends ObjectInputStream {

    /** The class loader to use. */
    private final ClassLoader classLoader;

    /**
     * Constructs a new ClassLoaderObjectInputStream.
     * 
     * @param classLoader
     *            the ClassLoader from which classes should be loaded
     * @param inputStream
     *            the InputStream to work on
     * @throws IOException
     *             in case of an I/O error
     * @throws StreamCorruptedException
     *             if the stream is corrupted
     */
    public ClassLoaderObjectInputStream(ClassLoader classLoader,
            InputStream inputStream) throws IOException,
            StreamCorruptedException {
        super(inputStream);
        this.classLoader = classLoader;
    }

    /**
     * Resolve a class specified by the descriptor using the specified
     * ClassLoader or the super ClassLoader.
     * 
     * @param objectStreamClass
     *            descriptor of the class
     * @return the Class object described by the ObjectStreamClass
     * @throws IOException
     *             in case of an I/O error
     * @throws ClassNotFoundException
     *             if the Class cannot be found
     */
    @Override
    protected Class<?> resolveClass(ObjectStreamClass objectStreamClass)
            throws IOException, ClassNotFoundException {

        Class<?> clazz = Class.forName(objectStreamClass.getName(), false,
                classLoader);

        if (clazz != null) {
            // the classloader knows of the class
            return clazz;
        } else {
            // classloader knows not of class, let the super classloader do it
            return super.resolveClass(objectStreamClass);
        }
    }
}

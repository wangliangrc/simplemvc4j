package com.baidu.aiu;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipFile;

import android.os.Build;
import android.util.Log;
import dalvik.system.PathClassLoader;

/**
 * Created with IntelliJ IDEA. User: baidu Date: 3/31/13 Time: 9:38 PM To change
 * this template use File | Settings | File Templates.
 */
final class ClassLoaderImpl4 implements FixedClassLoader {
    static {
        // PathClassLoader 可以同时接受 zip 文件盒 dex 文件
        System.setProperty("android.vm.dexfile", "true");
    }

    public static final ClassLoaderImpl4 INSTANCE = new ClassLoaderImpl4();

    private Object pathClassLoader;
    private AbstractPathClassLoader mPathClassLoader;

    private ClassLoaderImpl4() {
        final ClassLoader classLoader = getClass().getClassLoader();
        if (!(classLoader instanceof PathClassLoader)) {
            throw new IllegalStateException("ClassLoader context isn't right!");
        }
        pathClassLoader = classLoader;
        if (Build.VERSION.SDK_INT < 9) {
            mPathClassLoader = new PathClassLoaderApi4(pathClassLoader);
        } else {
            mPathClassLoader = new PathClassLoaderApi9(pathClassLoader);
        }
    }

    @Override
    public String getPackageName() {
        // com.teleca.jamendo-1.apk or com.teleca.jamendo.apk
        final String name = getDexFilePaths()[0].getName();
        Log.d("baidu", "apk -> " + name);
        int index = name.indexOf('-');
        if (index > 0) {
            return name.substring(0, index);
        } else {
            index = name.lastIndexOf('.');
            if(index > 0) {
                return name.substring(0, index);
            }
        }
        return null;
    }

    @Override
    public File[] getNativeDirectoryPaths() {
        final List<String> list = PathClassLoaderApi9
                        .getLibraryPathElements(pathClassLoader);
        final File[] files = new File[list.size()];
        for (int i = 0, len = files.length; i < len; ++i) {
            files[i] = new File(list.get(i));
        }
        return files;
    }

    @Override
    public File[] getDexFilePaths() {
        final List<Object> dexFileList = mPathClassLoader.getMDexs();
        final List<File> files = new LinkedList<File>();
        for (int i = 0, len = dexFileList.size(); i < len; ++i) {
            files.add(DexFileWrapper.getMFile(dexFileList.get(i)));
        }
        return files.toArray(new File[0]);
    }

    @Override
    public int addDexFilePaths(File optimizedDirectory, File... apks) {
        List<String> paths = mPathClassLoader.getMPaths();
        List<File> files = mPathClassLoader.getMFiles();
        List<ZipFile> zipFiles = mPathClassLoader.getMZips();
        List<Object> dexFiles = mPathClassLoader.getMDexs();

        for (int i = 0; i < apks.length; ++i) {
            addFile(paths, files, zipFiles, dexFiles, apks[i],
                            optimizedDirectory);
        }

        mPathClassLoader.setMPaths(paths);
        mPathClassLoader.setMFiles(files);
        mPathClassLoader.setMZips(zipFiles);
        mPathClassLoader.setMDexs(dexFiles);

        return 0;
    }

    private void addFile(List<String> paths, List<File> files,
                    List<ZipFile> zipFiles, List<Object> dexFiles,
                    File targetFile, File optimizedDirectory) {
        final boolean isZip = isZipFile(targetFile);
        if (isZip) {
            // final File dex = loadDexFromZip(targetFile, optimizedDirectory);
            // if (dex != null) {
            dexFiles.add(DexFileWrapper.newDexFile(targetFile,
                            optimizedDirectory));
            // } else {
            // dexFiles.add(null);
            // }
            try {
                zipFiles.add(new ZipFile(targetFile));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            paths.add(targetFile.getPath());
            files.add(targetFile);
        }
        // else {
        // zipFiles.add(null);
        // dexFiles.add(DexFileWrapper.newDexFile(targetFile));
        // }
    }

    @Override
    public int addNativeDirectoryPaths(File... nativeLibraryPaths) {
        final ArrayList<File> oldPaths = new ArrayList<File>();
        oldPaths.addAll(Arrays.asList(getNativeDirectoryPaths()));
        oldPaths.addAll(Arrays.asList(nativeLibraryPaths));
        final ArrayList<String> newPaths = new ArrayList<String>();
        File f = null;
        for (int i = 0, len = oldPaths.size(); i < len; ++i) {
            f = oldPaths.get(i);
            if (f != null) {
                newPaths.add(f.getAbsolutePath());
            }
        }
        PathClassLoaderApi9.setLibraryPathElements(pathClassLoader, newPaths);
        return 0;
    }

    // private static File loadDexFromZip(File zip, File optimizedDirectory) {
    // final String name = zip.getName();
    // File targetDexFile = new File(optimizedDirectory, name + ".dex");
    // int i = 1;
    // while (targetDexFile.exists()) {
    // targetDexFile = new File(optimizedDirectory, name + "-" + (i++)
    // + ".dex");
    // }
    // try {
    // final ZipFile zipFile = new ZipFile(zip);
    // final ZipEntry entry = zipFile.getEntry("classes.dex");
    // if (entry != null) {
    // IOUtils.copy(zipFile.getInputStream(entry),
    // new FileOutputStream(targetDexFile));
    // }
    // return targetDexFile;
    // } catch (Exception e) {
    // }
    // return null;
    // }

    private static boolean isZipFile(File file) {
        try {
            new ZipFile(file);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    private static abstract class AbstractPathClassLoader {
        protected static final Class<?> clazz;
        protected static final Field mPathsField;
        protected static final Field mFilesField;
        protected static final Field mZipsField;
        protected static final Field mDexsField;

        static {
            try {
                clazz = Class.forName("dalvik.system.PathClassLoader");

                mPathsField = clazz.getDeclaredField("mPaths");
                mPathsField.setAccessible(true);
                mFilesField = clazz.getDeclaredField("mFiles");
                mFilesField.setAccessible(true);
                mZipsField = clazz.getDeclaredField("mZips");
                mZipsField.setAccessible(true);
                mDexsField = clazz.getDeclaredField("mDexs");
                mDexsField.setAccessible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        protected Object mPathClassLoader;

        public AbstractPathClassLoader(Object mPathClassLoader) {
            super();
            this.mPathClassLoader = mPathClassLoader;
        }

        public abstract List<String> getLibraryPathElements();

        public abstract void setLibraryPathElements(List<String> list);

        public List<String> getMPaths() {
            final LinkedList<String> list = new LinkedList<String>();
            try {
                final String[] strings = (String[]) mPathsField
                                .get(mPathClassLoader);
                list.addAll(Arrays.asList(strings));
                return list;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void setMPaths(List<String> list) {
            try {
                mPathsField.set(mPathClassLoader, list.toArray(new String[0]));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public List<File> getMFiles() {
            final LinkedList<File> list = new LinkedList<File>();
            try {
                final File[] files = (File[]) mFilesField.get(mPathClassLoader);
                list.addAll(Arrays.asList(files));
                return list;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void setMFiles(List<File> list) {
            try {
                mFilesField.set(mPathClassLoader, list.toArray(new File[0]));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public List<ZipFile> getMZips() {
            final LinkedList<ZipFile> list = new LinkedList<ZipFile>();
            try {
                final ZipFile[] zipFiles = (ZipFile[]) mZipsField
                                .get(mPathClassLoader);
                list.addAll(Arrays.asList(zipFiles));
                return list;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void setMZips(List<ZipFile> list) {
            try {
                mZipsField.set(mPathClassLoader, list.toArray(new ZipFile[0]));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public List<Object> getMDexs() {
            final LinkedList<Object> list = new LinkedList<Object>();
            try {
                final Object[] dexFiles = (Object[]) mDexsField
                                .get(mPathClassLoader);
                list.addAll(Arrays.asList(dexFiles));
                return list;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void setMDexs(List<Object> list) {
            Object[] array = (Object[]) Array.newInstance(DexFileWrapper.clazz,
                            list.size());
            for (int i = 0, len = array.length; i < len; ++i) {
                array[i] = list.get(i);
            }
            try {
                mDexsField.set(mPathClassLoader, array);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class PathClassLoaderApi4 extends AbstractPathClassLoader {
        private static final Field mLibPathsField;

        static {
            try {
                mLibPathsField = clazz.getDeclaredField("mLibPaths");
                mLibPathsField.setAccessible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public PathClassLoaderApi4(Object pathClassLoader) {
            super(pathClassLoader);
        }

        @Override
        public List<String> getLibraryPathElements() {
            try {
                final String[] paths = (String[]) mLibPathsField
                                .get(mPathClassLoader);
                return new ArrayList<String>(Arrays.asList(paths));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void setLibraryPathElements(List<String> list) {
            final String[] value = new String[list.size()];
            for (int i = 0, len = value.length; i < len; ++i) {
                value[i] = list.get(i);
            }
            try {
                mLibPathsField.set(mPathClassLoader, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static class PathClassLoaderApi9 extends AbstractPathClassLoader {
        private static final Field libraryPathElementsField;

        static {
            try {
                libraryPathElementsField = clazz
                                .getDeclaredField("libraryPathElements");
                libraryPathElementsField.setAccessible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public PathClassLoaderApi9(Object pathClassLoader) {
            super(pathClassLoader);
        }

        @Override
        public List<String> getLibraryPathElements() {
            return getLibraryPathElements(mPathClassLoader);
        }

        @Override
        public void setLibraryPathElements(List<String> list) {
            setLibraryPathElements(mPathClassLoader, list);
        }

        @SuppressWarnings("unchecked")
        private static List<String> getLibraryPathElements(
                        Object pathClassLoader) {
            try {
                return (List<String>) libraryPathElementsField
                                .get(pathClassLoader);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static void setLibraryPathElements(Object pathClassLoader,
                        List<String> list) {
            final ArrayList<String> value = new ArrayList<String>();
            for (int i = 0, len = list.size(); i < len; ++i) {
                value.add(makesureLibraryPathInvalidate(list.get(i)));
            }
            try {
                libraryPathElementsField.set(pathClassLoader, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static String makesureLibraryPathInvalidate(String in) {
            return in.endsWith(File.separator) ? in : (in + File.separator);
        }

    }

    private static class DexFileWrapper {
        private static final Class<?> clazz;
        private static final Constructor<?> constructor;
        private static final Field mFileNameField;
        private static final Method loadDexMethod;

        static {
            try {
                clazz = Class.forName("dalvik.system.DexFile");

                constructor = clazz.getDeclaredConstructor(File.class);
                constructor.setAccessible(true);

                mFileNameField = clazz.getDeclaredField("mFileName");
                mFileNameField.setAccessible(true);

                loadDexMethod = clazz.getDeclaredMethod("loadDex",
                                String.class, String.class, int.class);
                loadDexMethod.setAccessible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static Object newDexFile(File dexFile, File optimizedDirectory) {
            final String sourcePathName = dexFile.getAbsolutePath();
            final String outputName = generateOutputName(sourcePathName,
                            optimizedDirectory.getAbsolutePath());
            return newDexFileInternal(sourcePathName, outputName);
        }

        private static Object newDexFileInternal(String sourcePathName,
                        String outputPathName) {
            try {
                return loadDexMethod.invoke(null, sourcePathName,
                                outputPathName, 0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static String generateOutputName(String sourcePathName,
                        String outputDir) {

            StringBuilder newStr = new StringBuilder(80);

            /* start with the output directory */
            newStr.append(outputDir);
            if (!outputDir.endsWith("/"))
                newStr.append("/");

            /* get the filename component of the path */
            String sourceFileName;
            int lastSlash = sourcePathName.lastIndexOf("/");
            if (lastSlash < 0)
                sourceFileName = sourcePathName;
            else
                sourceFileName = sourcePathName.substring(lastSlash + 1);

            /*
             * Replace ".jar", ".zip", whatever with ".dex". We don't want to
             * use ".odex", because the build system uses that for files that
             * are paired with resource-only jar files. If the VM can assume
             * that there's no classes.dex in the matching jar, it doesn't need
             * to open the jar to check for updated dependencies, providing a
             * slight performance boost at startup. The use of ".dex" here
             * matches the use on files in /data/dalvik-cache.
             */
            int lastDot = sourceFileName.lastIndexOf(".");
            if (lastDot < 0)
                newStr.append(sourceFileName);
            else
                newStr.append(sourceFileName, 0, lastDot);
            newStr.append(".dex");

            return newStr.toString();
        }

        // public static Object newDexFile(File file) {
        // try {
        // return constructor.newInstance(file);
        // } catch (Exception e) {
        // throw new RuntimeException(e);
        // }
        // }
        //
        // @SuppressWarnings("unused")
        // public static Object newDexFile(String path) {
        // return newDexFile(new File(path));
        // }
        //
        // @SuppressWarnings("unused")
        // public static List<Object> newDexFiles(File... files) {
        // final List<Object> list = new LinkedList<Object>();
        // for (int i = 0, len = files.length; i < len; ++i) {
        // list.add(newDexFile(files[i]));
        // }
        // return list;
        // }

        public static String getMFileName(Object dexFile) {
            try {
                return (String) mFileNameField.get(dexFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static File getMFile(Object dexFile) {
            return new File(getMFileName(dexFile));
        }
    }
}

package com.baidu.aiu;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

import android.text.TextUtils;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

/**
 * Created with IntelliJ IDEA. User: baidu Date: 3/31/13 Time: 9:37 PM To change
 * this template use File | Settings | File Templates.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class ClassLoaderImpl14 implements FixedClassLoader {
    public static final ClassLoaderImpl14 INSTANCE = new ClassLoaderImpl14();

    private Object dexPathList;

    private ClassLoaderImpl14() {
        final ClassLoader classLoader = getClass().getClassLoader();
        if (!(classLoader instanceof PathClassLoader)) {
            throw new IllegalStateException("ClassLoader context isn't right!");
        }
        dexPathList = BaseDexClassLoaderWrapper.getPathList(classLoader);
    }

    @Override
    public String getPackageName() {
        String name = System.getProperty("java.io.tmpdir");
        if (!TextUtils.isEmpty(name)) {
            final Matcher matcher = sPathPattern1.matcher(name);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }

        name = getDexFilePaths()[0].getName();
        if (!TextUtils.isEmpty(name)) {
            final Matcher matcher = sPathPattern2.matcher(name);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    @Override
    public File[] getNativeDirectoryPaths() {
        return DexPathListWrapper.getNativeLibraryDirectoriesField(dexPathList);
    }

    @Override
    public File[] getDexFilePaths() {
        final Object[] lists = DexPathListWrapper.getDexElements(dexPathList);
        final File[] res = new File[lists.length];
        for (int i = 0; i < res.length; ++i) {
            res[i] = DexPathListElementWrapper.getFile(lists[i]);
        }
        return res;
    }

    @Override
    public int addDexFilePaths(File optimizedDirectory, File... apks) {
        if (apks == null || apks.length == 0)
            return 1;
        final Object[] oriDexPathListElements = DexPathListWrapper
                        .getDexElements(dexPathList);
        final Object[] newDexPathListElements = DexPathListWrapper
                        .makeDexElements(dexPathList, optimizedDirectory, apks);
        // TODO 这里我们将后来加入的Class放到前面
        final Object[] array = (Object[]) Array.newInstance(
                        DexPathListElementWrapper.clazz,
                        newDexPathListElements.length
                                        + oriDexPathListElements.length);
        int index = 0;
        for (int i = 0; i < newDexPathListElements.length; ++i) {
            array[index++] = newDexPathListElements[i];
        }
        for (int i = 0; i < oriDexPathListElements.length; ++i) {
            array[index++] = oriDexPathListElements[i];
        }
        DexPathListWrapper.setDexElements(dexPathList, array);
        return 0;
    }

    @Override
    public int addNativeDirectoryPaths(File... nativeLibraryPaths) {
        final File[] oldNativeDirs = getNativeDirectoryPaths();
        final List<File> newNativeDirsList = new LinkedList<File>();
        newNativeDirsList.addAll(Arrays.asList(oldNativeDirs));
        newNativeDirsList.addAll(Arrays.asList(nativeLibraryPaths));
        DexPathListWrapper.setNativeLibraryDirectoriesField(dexPathList, newNativeDirsList.toArray(new File[0]));
        return 0;
    }

    private static Pattern sPathPattern1 = Pattern
                    .compile("/data/data/(\\S+?)[-/]\\S+");
    private static Pattern sPathPattern2 = Pattern
                    .compile("(\\S+?)[-/]\\S+");

    private static class BaseDexClassLoaderWrapper {
        private static final Class clazz;
        private static final Field pathListField;

        static {
            try {
                clazz = Class.forName("dalvik.system.BaseDexClassLoader");

                pathListField = clazz.getDeclaredField("pathList");
                pathListField.setAccessible(true);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        public static Object getPathList(Object baseDexClassLoader) {
            try {
                return pathListField.get(baseDexClassLoader);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class DexPathListWrapper {
        private static final Class clazz;
        private static final Method makeDexElementsMethod;
        private static final Field dexElementsFiled;
        private static final Field nativeLibraryDirectoriesField;

        static {
            try {
                clazz = Class.forName("dalvik.system.DexPathList");
                makeDexElementsMethod = clazz.getDeclaredMethod(
                                "makeDexElements", ArrayList.class, File.class);
                makeDexElementsMethod.setAccessible(true);

                dexElementsFiled = clazz.getDeclaredField("dexElements");
                dexElementsFiled.setAccessible(true);

                nativeLibraryDirectoriesField = clazz
                                .getDeclaredField("nativeLibraryDirectories");
                nativeLibraryDirectoriesField.setAccessible(true);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        public static Object[] getDexElements(Object dexPathList) {
            try {
                return (Object[]) dexElementsFiled.get(dexPathList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static void setDexElements(Object dexPathList, Object newValues) {
            try {
                dexElementsFiled.set(dexPathList, newValues);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        public static File[] getNativeLibraryDirectoriesField(Object dexPathList) {
            try {
                return (File[]) nativeLibraryDirectoriesField.get(dexPathList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static void setNativeLibraryDirectoriesField(Object dexPathList,
                        File[] files) {
            try {
                nativeLibraryDirectoriesField.set(dexPathList, files);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static Object[] makeDexElements(Object dexPathList,
                        File optimizedDirectory, File... files) {
            return makeDexElements(dexPathList, Arrays.asList(files),
                            optimizedDirectory);
        }

        public static Object[] makeDexElements(Object dexPathList,
                        List<File> files, File optimizedDirectory) {
            try {
                return (Object[]) makeDexElementsMethod.invoke(dexPathList,
                                new ArrayList<File>(files), optimizedDirectory);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static String toString(Object dexPathList) {
            final Object[] elements = getDexElements(dexPathList);
            final File[] libraries = getNativeLibraryDirectoriesField(dexPathList);
            StringBuilder buf = new StringBuilder();
            buf.append("[dalvik.system.DexPathList]");
            if (elements != null) {
                buf.append("\nelements: ");
                for (int i = 0; i < elements.length; ++i) {
                    buf.append("\n\t").append(
                                    DexPathListElementWrapper
                                                    .toString(elements[i]));
                }
            }
            if (libraries != null) {
                buf.append("\nlibraries: ");
                for (int i = 0; i < libraries.length; ++i) {
                    buf.append("\n\t").append(libraries[i].getAbsolutePath());
                }
            }
            return buf.toString();
        }
    }

    private static class DexPathListElementWrapper {
        private static final Class clazz;
        private static final Field mFileField;
        private static final Field mZipFile;
        private static final Field mDexFile;

        static {
            try {
                clazz = Class.forName("dalvik.system.DexPathList$Element");
                mFileField = clazz.getDeclaredField("file");
                mFileField.setAccessible(true);
                mZipFile = clazz.getDeclaredField("zipFile");
                mZipFile.setAccessible(true);
                mDexFile = clazz.getDeclaredField("dexFile");
                mDexFile.setAccessible(true);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        public static File getFile(Object dexListPathElement) {
            try {
                return (File) mFileField.get(dexListPathElement);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static ZipFile getZipFile(Object dexListPathElement) {
            try {
                return (ZipFile) mZipFile.get(dexListPathElement);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static DexFile getDexFile(Object dexListPathElement) {
            try {
                return (DexFile) mDexFile.get(dexListPathElement);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static String toString(Object element) {
            final File srcFile = getFile(element);
            final DexFile dexFile = getDexFile(element);
            return String.format(
                            "[dalvik.system.DexPathList.Element]\tfile: %s\tdexFile: %s",
                            srcFile.getAbsolutePath(),
                            DexFileWrapper.toString(dexFile));
        }
    }

    private static class DexFileWrapper {
        private static final Class<DexFile> clazz = DexFile.class;
        private static final Method openDexFileMethod;
        private static final Field mCookieField;
        private static final Field mFileNameField;

        static {
            try {
                mCookieField = clazz.getDeclaredField("mCookie");
                mCookieField.setAccessible(true);

                mFileNameField = clazz.getDeclaredField("mFileName");
                mFileNameField.setAccessible(true);

                openDexFileMethod = clazz.getDeclaredMethod("openDexFile",
                                byte[].class);
                openDexFileMethod.setAccessible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 获取某个 DexFile 的 cookie
         * 
         * @param dexFile
         * @return
         */
        public static int getCookie(DexFile dexFile) {
            try {
                return mCookieField.getInt(dexFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static String getFileName(DexFile dexFile) {
            try {
                return (String) mFileNameField.get(dexFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 加载 dex 文件的内容，返回一个 DexFile 的 Cookie。
         * 
         * @param dexContent
         *            dex 文件的内容
         * @return DexFile 的 Cookie
         */
        public static int openDexFile(byte[] dexContent) {
            try {
                return (Integer) openDexFileMethod.invoke(null, dexContent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static String toString(DexFile dexFile) {
            final int mCookie = getCookie(dexFile);
            final String fileName = getFileName(dexFile);
            return String.format(
                            "[dalvik.system.DexFile] cookie: %d, filename: %s",
                            mCookie, fileName);
        }
    }
}

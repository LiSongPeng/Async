package com.light.utils;

import com.light.common.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class scanner
 *
 * @author lihb
 */
public class ClassScanner {
    private static final Logger logger = LoggerFactory.getLogger(ClassScanner.class);

    /**
     * scan all class under packageName
     *
     * @param packageName
     * @param recursive
     * @return
     */
    public static Set<Class<?>> scan(String packageName, boolean recursive) {
        Set<Class<?>> classes = new LinkedHashSet<>();
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> urls;
        try {
            urls = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        } catch (IOException e) {
            logger.error("", e);
            throw new RuntimeException(String.format("can not scan %s", packageName));
        }
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String protocol = url.getProtocol();
            if (Constant.FILE_PROTOCOL.equals(protocol)) {
                String filePath = null;
                try {
                    filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    logger.error("", e);
                }
                findAndAddClassesInPackageByFile(packageName,
                        filePath, recursive, classes);
            }
        }

        return classes;
    }

    /**
     * obtain classes under packagePath
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static void findAndAddClassesInPackageByFile(String packageName,
                                                        String packagePath, boolean recursive, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles(file -> (recursive && file.isDirectory()) ||
                (file.getName().endsWith(Constant.CLASS_FILE_SUFFIX)));
        for (File file : files) {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(String.format("%s.%s", packageName, file.getName()), file.getAbsolutePath(), recursive, classes);
            } else {
                String className = file.getName().substring(0,
                        file.getName().length() - Constant.CLASS_FILE_SUFFIX.length() - 1);
                try {
                    classes.add(Thread.currentThread()
                            .getContextClassLoader()
                            .loadClass(String.format("%s.%s", packageName, className)));
                } catch (ClassNotFoundException e) {
                    logger.error("", e);
                }
            }
        }
    }

}

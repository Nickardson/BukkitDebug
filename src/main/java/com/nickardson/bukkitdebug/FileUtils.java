package com.nickardson.bukkitdebug;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtils {

    public static List<String> dir(File jarFile, final String path) {
        final List<String> ls = new ArrayList<String>();

        try {
            JarFile jar = new JarFile(jarFile);

            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    String name = entry.getName();
                    if (name.startsWith(path + "/")) {
                        ls.add(name);
                    }
                }
            }

            jar.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ls;
    }

    public static boolean copyResourceToFile(String loc, File dest) throws IOException {
        InputStream stream = FileUtils.class.getResourceAsStream(loc);

        if (stream == null) {
            throw new IOException("Stream is null for opening " + loc);
        }

        OutputStream resStreamOut;
        int readBytes;
        byte[] buffer = new byte[4096];
        try {
            resStreamOut = new FileOutputStream(dest);

            while ((readBytes = stream.read(buffer)) > 0)
                resStreamOut.write(buffer, 0, readBytes);

            resStreamOut.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}

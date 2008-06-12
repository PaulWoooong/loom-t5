package org.digitalsoul.loom.core;

import org.eclipse.jdt.core.IJavaElement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class ResourceUtil {

    public static File writeFile(File folder, String filename, String content) throws IOException {
        File file = new File(folder, filename);
        FileWriter writer = new FileWriter(file);
        writer.append(content);
        writer.close();
        return file;
    }
    
    
}

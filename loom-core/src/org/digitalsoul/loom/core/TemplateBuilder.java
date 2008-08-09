package org.digitalsoul.loom.core;

import org.eclipse.core.resources.IFile;

import org.digitalsoul.loom.core.prefs.Preferences;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class TemplateBuilder
 */
public class TemplateBuilder {

    /**
     * @param unit
     */
    public void createTemplate(ICompilationUnit unit) {

        IProject project = unit.getJavaProject().getProject();
        IFolder templateFolder = getTemplateFolder(project);
        IFolder packageFolder = createTemplatePackage(unit, templateFolder);
        String filename = constructTemplateFilename(unit);
        String content = "Hello World";
        try {
            IFile fileToCreate = packageFolder.getFile(filename);
            if (!fileToCreate.exists()) {
                fileToCreate.create(new ByteArrayInputStream(content.getBytes()), true, null);
            }
        }
        catch (CoreException e) {
            e.printStackTrace();
        }
    }
    
    protected String constructTemplateFilename(ICompilationUnit unit) {
        return unit.getElementName().replace(".java", Preferences.getTemplateFileExtension());
    }

    /**
     * @param project
     * @return
     */
    protected IFolder getTemplateFolder(IProject project) {
        return project.getFolder(Preferences.getTemplateFragmentRootPath());
    }

    /**
     * @param unit
     * @return
     */
    protected String[] getPackageNames(ICompilationUnit unit) {

        List<String> packageNames = new ArrayList<String>();
        IJavaElement parent = unit.getParent();
        while (parent != null && parent.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
            String elementName = parent.getElementName();
            for (String packageName : elementName.split("\\.")) {
                packageNames.add(packageName);
            }
            parent = parent.getParent();
        }
        return packageNames.toArray(new String[packageNames.size()]);
    }

    /**
     * @param unit
     * @param templateFolder
     * @return
     */
    protected IFolder createTemplatePackage(ICompilationUnit unit, IFolder templateFolder) {

        String[] packageNames = getPackageNames(unit);
        IFolder folder = templateFolder;
        for (String packageName : packageNames) {
            folder = folder.getFolder(new Path(packageName));
            try {
                if (!folder.exists()) {
                    folder.create(true, true, null);
                }
            }
            catch (CoreException e) {
                e.printStackTrace();
            }
        }
        return folder;
    }
}

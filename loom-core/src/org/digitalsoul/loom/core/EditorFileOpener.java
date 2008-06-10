/**
 * Copyright 2008 Loom Developers. This file is part of the loom eclipse plugin for eclipse
 * and is licensed under the GPL version 3. 
 * Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */
package org.digitalsoul.loom.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import java.io.File;
import java.util.HashMap;

public class EditorFileOpener {

    private static EditorFileOpener editorFileOpener;

    private HashMap<String, IFile> fileMap;

    private String templateFileExtension;

    public EditorFileOpener() {
        templateFileExtension = LoomCorePlugin.getDefault().getPreferenceStore().getString(LoomConstants.TEMPLATE_FILE_EXTENSION);
        fileMap = new HashMap<String, IFile>();
    }

    private void openFileInEditor(IFile theFile) {

        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
            IDE.openEditor(activePage, theFile, true);
        }
        catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    private IFile findCachedFile(String filename) {

        IFile file = fileMap.get(filename);
        if (file != null) {
            synchronizeFile(file, true);
            if (file.exists() && file.isAccessible()) {
                //                System.err.println("oh my god it's cached!");
                return file;
            }
            else {
                fileMap.remove(filename);
                file = null;
            }
        }
        return file;
    }

    private void synchronizeFile(IFile file, boolean force) {

        if (force || !file.isSynchronized(IResource.DEPTH_ONE)) {
            try {
                file.refreshLocal(IResource.DEPTH_ONE, null);
            }
            catch (CoreException e) {
                e.printStackTrace();
            }
        }
    }

    public void switchToTemplateOrJavaFile(IResource contextMenuResource) {

        IFile foundFile = null;

        if (contextMenuResource instanceof IFile) {

            String searchFilename = contextMenuResource.getName();
            IJavaProject project = convertToJavaProject(contextMenuResource.getProject());

            if (searchFilename.endsWith(LoomConstants.JAVA_FILE_EXTENSION)) {
                searchFilename = searchFilename.replace(LoomConstants.JAVA_FILE_EXTENSION, templateFileExtension);
                foundFile = switchToTemplate(searchFilename, project);
            }
            else if (searchFilename.endsWith(templateFileExtension)) {
                searchFilename = searchFilename.replace(templateFileExtension, LoomConstants.JAVA_FILE_EXTENSION);
                foundFile = switchToJavaFile(searchFilename, project);
            }

            if (foundFile != null) {
                synchronizeFile(foundFile, false);
                addToCache(searchFilename, foundFile);
                openFileInEditor(foundFile);
            }
            else {
                System.err.println("Couldn't find file: " + searchFilename);
            }
        }
    }

    private IFile switchToJavaFile(String searchFilename, IJavaProject project) {

        IFile foundFile = null;
        foundFile = findCachedFile(searchFilename);

        if (foundFile == null) {
            foundFile = findJavaFile(searchFilename, project);
        }
        return foundFile;
    }

    private IFile switchToTemplate(String searchFilename, IJavaProject project) {

        IFile foundFile = null;

        foundFile = findCachedFile(searchFilename);
        if (foundFile == null) {

            try {
                IPath projectPath = project.getCorrespondingResource().getLocation();
                File searchFolder = new File(projectPath.toPortableString());
                String pathURL = findFile(searchFilename, searchFolder);
                Path path = new Path(pathURL);
                foundFile = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(path)[0];
            }
            catch (JavaModelException e) {
                e.printStackTrace();
            }
        }
        return foundFile;
    }

    private void addToCache(String searchFilename, IFile foundFile) {

        if (foundFile != null) {
            synchronizeFile(foundFile, false);
            fileMap.put(searchFilename, foundFile);
        }
    }

    private String findFile(String searchFilename, File searchFolder) {

        String path = null;
        for (File file : searchFolder.listFiles()) {

            // ignore hidden and target directories
            if (file.isDirectory() && !file.getName().startsWith(".") && !file.getName().equals("target")) {

                String aPath = findFile(searchFilename, file);
                if (aPath != null) {
                    path = aPath;
                }
            }
            else if (file.getName().equals(searchFilename)) {
                return file.getPath();
            }
        }
        return path;
    }

    //    @Test
    //    public void testFindFile() {
    //
    //        File tmpFoo = new File("/tmp/foo");
    //        Assert.assertEquals(tmpFoo.exists(), true);
    //
    //        String filePath = findFile("file", tmpFoo);
    //        File file = new File(filePath);
    //        Assert.assertEquals(file.exists(), true);
    //    }

    private IFile findJavaFile(String filename, IJavaProject project) {

        IFile foundFile = null;
        try {
            IPackageFragment[] fragments = project.getPackageFragments();

            for (IPackageFragment fragment : fragments) {

                if (foundFile != null) break;

                for (ICompilationUnit unit : fragment.getCompilationUnits()) {

                    if (unit.getElementName().equals(filename)) {
                        foundFile = (IFile) unit.getResource();
                        break;
                    }
                }
            }
        }
        catch (JavaModelException e) {
            e.printStackTrace();
        }

        return foundFile;
    }

    private IJavaProject convertToJavaProject(IProject project) {

        return new JavaProject(project, null);
    }

    public void switchToTemplateOrJavaFile() {

        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorPart editor = activePage.getActiveEditor();
        if (editor != null) {
            IEditorInput input = editor.getEditorInput();
            if (input != null) {
                IFile currentFile = (input instanceof IFileEditorInput) ? ((IFileEditorInput) input).getFile() : null;
                switchToTemplateOrJavaFile(currentFile);
            }
        }
    }

    public static EditorFileOpener getInstance() {

        if (editorFileOpener == null) {
            editorFileOpener = new EditorFileOpener();
        }
        return editorFileOpener;
    }

    public void setTemplateFileExtension(String selectedItem) {
        this.templateFileExtension = selectedItem;        
    }
}

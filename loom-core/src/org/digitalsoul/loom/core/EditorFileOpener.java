/**
 * Copyright 2008 Loom Developers. This file is part of the loom eclipse plugin for eclipse and is licensed under the
 * GPL version 3. Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */
package org.digitalsoul.loom.core;

import org.digitalsoul.loom.core.prefs.Preferences;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
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

/**
 * Class EditorFileOpener
 */
public class EditorFileOpener {

    /**
     * 
     */
    private static EditorFileOpener editorFileOpener;

    /**
     * 
     */
    private HashMap<String, IFile> fileMap;

    private ProjectBuilder builder;

    /**
     * 
     */
    public EditorFileOpener() {
        fileMap = new HashMap<String, IFile>();
    }

    /**
     * @param theFile
     */
    public void openFileInEditor(IFile theFile) {

        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
            IDE.openEditor(activePage, theFile, true);
        }
        catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param filename
     * @return
     */
    private IFile findCachedFile(String filename) {
        IFile file = fileMap.get(filename);
        if (file != null) {
            synchronizeFile(file, true);
            if (file.exists() && file.isAccessible()) {
                return file;
            }
            else {
                fileMap.remove(filename);
                file = null;
            }
        }
        return file;
    }

    /**
     * @param file
     * @param force
     */
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

    /**
     * @param contextMenuResource
     */
    public void switchToTemplateOrJavaFile(IResource contextMenuResource) {

        IFile foundFile = null;
        String templateFileExtension = Preferences.getTemplateFileExtension();
        
        if (contextMenuResource instanceof IFile) {

            String searchFilename = contextMenuResource.getName();
            IJavaProject project = convertToJavaProject(contextMenuResource.getProject());
            builder = new ProjectBuilder(project);

            if (searchFilename.endsWith(LoomConstants.JAVA_FILE_EXTENSION)) {
                searchFilename = searchFilename.replace(LoomConstants.JAVA_FILE_EXTENSION, templateFileExtension);
            }
            else if (searchFilename.endsWith(templateFileExtension)) {
                searchFilename = searchFilename.replace(templateFileExtension, LoomConstants.JAVA_FILE_EXTENSION);
            }
            foundFile = findFile(contextMenuResource, searchFilename, project);

            if (foundFile != null && foundFile.exists()) {
                synchronizeFile(foundFile, false);
                addToCache(searchFilename, foundFile);
                openFileInEditor(foundFile);
            }
            else {
                System.err.println("Couldn't find file: " + searchFilename);
            }
        }
    }

    /**
     * @param contextMenuResource
     * @param searchFilename
     * @param project
     * @return
     */
    private IFile findFile(IResource contextMenuResource, String searchFilename, IJavaProject project) {

        IFile foundFile = null;

        foundFile = findCachedFile(searchFilename);
        if (foundFile == null) {
            if (Preferences.isCreateTemplateInJavaFolder()) {
                IFolder folder = (IFolder) contextMenuResource.getParent();
                foundFile = folder.getFile(searchFilename);
            }
            else {
                if (searchFilename.endsWith(Preferences.getTemplateFileExtension())) {
                    IFolder folder = builder.getFolder(getTemplateFilePath(contextMenuResource));
                    foundFile = folder.getFile(searchFilename);
                }
                else {
                    foundFile = switchToFile(searchFilename, project);
                }
            }
        }
        return foundFile;
    }
    
    /**
     * @param selectedJavaFile
     * @return
     */
    protected IPath getTemplateFilePath(IResource selectedJavaFile) {
        String path = selectedJavaFile.getParent().getProjectRelativePath().toPortableString();
        path = path.replaceAll(Preferences.getJavaFragmentRootPath(), Preferences.getTemplateFragmentRootPath());
        return new Path(path);
    }

    /**
     * @param searchFilename
     * @param project
     * @return
     */
    private IFile switchToFile(String searchFilename, IJavaProject project) {

        IFile foundFile = null;

        foundFile = findCachedFile(searchFilename);
        if (foundFile == null) {

            try {
                IPath projectPath = project.getCorrespondingResource().getLocation();
                File searchFolder = new File(projectPath.toPortableString());
                String pathURL = findFile(searchFilename, searchFolder, project);
                Path path = new Path(pathURL);
                foundFile = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(path)[0];
            }
            catch (JavaModelException e) {
                e.printStackTrace();
            }
        }
        return foundFile;
    }

    /**
     * @param searchFilename
     * @param foundFile
     */
    private void addToCache(String searchFilename, IFile foundFile) {

        if (foundFile != null) {
            synchronizeFile(foundFile, false);
            fileMap.put(searchFilename, foundFile);
        }
    }

    private String findFile(String searchFilename, File searchFolder, IJavaProject project) {

        String path = null;
        for (File file : searchFolder.listFiles()) {

            // ignore hidden and target directories
            if (file.isDirectory() && !file.getName().startsWith(".") && !file.getName().equals("target")) {

                String aPath = findFile(searchFilename, file, project);
                if (aPath != null) {
                    path = aPath;
                }
            }
            else if (file.getName().equals(searchFilename)) {
                if (!isDerived(file, project)) {
                    return file.getPath();
                }
            }
        }
        return path;
    }

    /**
     * @param file
     * @param project
     * @return
     */
    private boolean isDerived(File file, IJavaProject project) {

        Path path = new Path(file.getAbsolutePath());
        IFile ifile = project.getProject().getFile(path);
        if (ifile != null) {
            return ifile.isDerived();
        }
        return false;
    }

    /**
     * @param project
     * @return
     */
    private IJavaProject convertToJavaProject(IProject project) {

        return new JavaProject(project, null);
    }

    /**
     * 
     */
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

    /**
     * @return
     */
    public static EditorFileOpener getInstance() {

        if (editorFileOpener == null) {
            editorFileOpener = new EditorFileOpener();
        }
        return editorFileOpener;
    }
}

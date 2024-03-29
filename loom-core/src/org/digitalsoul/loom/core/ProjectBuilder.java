package org.digitalsoul.loom.core;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class ProjectBuilder
 */
public class ProjectBuilder {

    /**
     * 
     */
    IJavaProject javaProject;

    /**
     * 
     */
    IProject project;

    /**
     * @param javaProject
     */
    public ProjectBuilder(IJavaProject javaProject) {

        this.javaProject = javaProject;
        this.project = javaProject.getProject();
    }

    /**
     * @param projectName
     */
    public ProjectBuilder(String projectName) {
        createJavaProject(projectName);
    }

    /**
     * @param folder
     */
    public static void createFolder(IFolder folder) {
        try {
            if (!folder.exists()) {
                folder.create(true, true, null);
            }
        }
        catch (CoreException e) {
            e.printStackTrace();
        }
    }

    public IFolder getFolder(IPath path) {
        return project.getFolder(path);
    }

    /**
     * @param folder
     * @param filename
     * @param fileContent
     * @return
     * @throws CoreException
     */
    public IFile createFile(IContainer folder, String filename, String fileContent) throws CoreException {
        IFile createdFile = folder.getFile(new Path(filename));
        createdFile.create(new ByteArrayInputStream(fileContent.getBytes()), true, null);
        return createdFile;
    }

    /**
     * @param folderName
     * @return
     * @throws CoreException
     */
    public IFolder createFolder(String folderName) throws CoreException {
        return createFolder(project, folderName);
    }

    /**
     * @param parentFolder
     * @param foldername
     * @return
     * @throws CoreException
     */
    public IFolder createFolder(IContainer parentFolder, String foldername) throws CoreException {
        IFolder createdFolder = parentFolder.getFolder(new Path(foldername));
        if (!createdFolder.exists()) {
            createdFolder.create(true, true, null);
        }
        return createdFolder;
    }

    /**
     * @param projectName
     */
    public void createJavaProject(String projectName) {
        try {
            String binFolderName = "bin";
            IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
            this.project = workspaceRoot.getProject(projectName);
            if (project.exists()) {
                project.refreshLocal(IResource.DEPTH_INFINITE, null);
            }
            else {
                project.create(null);
            }
            if (!project.isOpen()) {
                project.open(null);
            }
            IFolder binFolder = project.getFolder(binFolderName);
            if (!binFolder.exists()) {
                createFolder(binFolder);
            }
            IPath outputPath = binFolder.getFullPath();

            if (!project.hasNature(JavaCore.NATURE_ID)) {
                addJavaNature();
            }
            this.javaProject = JavaCore.create(project);
            this.javaProject.setOutputLocation(outputPath, null);
            this.javaProject.setRawClasspath(new IClasspathEntry[0], null);
        }
        catch (CoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    private void addJavaNature() {
        try {
            IProjectDescription projectDescription = project.getDescription();
            String[] currentNatures = projectDescription.getNatureIds();
            List<String> newNatures = new ArrayList<String>();
            for (String nature : currentNatures) {
                newNatures.add(nature);
            }
            newNatures.add(JavaCore.NATURE_ID);
            projectDescription.setNatureIds(newNatures.toArray(new String[newNatures.size()]));
            project.setDescription(projectDescription, null);
        }
        catch (CoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    public void deleteProject() {
        try {
            javaProject.getProject().delete(true, true, null);
        }
        catch (CoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param file
     * @return
     */
    public static String readFileContents(File file) {
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}

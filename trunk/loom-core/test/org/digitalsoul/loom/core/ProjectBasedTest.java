package org.digitalsoul.loom.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Class ProjectBasedTest
 */
public class ProjectBasedTest extends BaseTester {

    protected IFolder javaFolder;
    protected IFolder templateFolder;
    protected IFile javaFile;
    protected IJavaProject javaProject;
    protected ICompilationUnit compilationUnit;
    protected ProjectBuilder builder;

    /**
     * 
     */
    @Test
    public void testJavaFileCorrectEnvironment() {
        Assert.assertEquals("org.loom", compilationUnit.getParent().getElementName());
        Assert.assertEquals("java", compilationUnit.getParent().getParent().getElementName());
        Assert.assertEquals(IJavaElement.PACKAGE_FRAGMENT, compilationUnit.getParent().getElementType());
        Assert.assertEquals(IJavaElement.PACKAGE_FRAGMENT_ROOT, compilationUnit.getParent().getParent().getElementType());
    }
    
    /**
     * 
     */
    @Before
    public void setupProject () {
        try {
            builder = new ProjectBuilder("testProject");
            javaProject = builder.javaProject;
            IFolder srcFolder = builder.createFolder("src");
            IFolder mainFolder = builder.createFolder(srcFolder, "main");
            javaFolder = builder.createFolder(mainFolder, "java");

            IClasspathEntry sourceEntry = JavaCore.newSourceEntry(javaFolder.getFullPath());
            javaProject.setRawClasspath(new IClasspathEntry[] {sourceEntry}, null);

            templateFolder = builder.createFolder(mainFolder, "webapp");
            Assert.assertEquals(1, javaProject.getAllPackageFragmentRoots().length);

            IPackageFragmentRoot javaRoot = javaProject.getAllPackageFragmentRoots()[0];
            IPackageFragment orgFragment = javaRoot.createPackageFragment("org.loom", true, null);
            compilationUnit = orgFragment.createCompilationUnit("Wizard.java", "public class Wizard{}", true, null);
            
            javaFile = (IFile) compilationUnit.getResource();
            Assert.assertTrue(javaFile.exists());
            Assert.assertTrue(javaFolder.exists());
            Assert.assertTrue(templateFolder.exists());
        }
        catch (CoreException e) {
            e.printStackTrace();
        }    
    }

    /**
     * 
     */
    @After
    public void deleteProject() {
        builder.deleteProject();
    }
}

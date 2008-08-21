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
    protected ICompilationUnit javaCompilationUnit;
    protected ProjectBuilder builder;
    private ICompilationUnit kawaCompilationUnit;
    private IFile kawaFile;

    /**
     * 
     */
    @Test
    public void testJavaFileCorrectEnvironment() {
        Assert.assertEquals("org.loom", javaCompilationUnit.getParent().getElementName());
        Assert.assertEquals("javaxx", javaCompilationUnit.getParent().getParent().getElementName());
        Assert.assertEquals(IJavaElement.PACKAGE_FRAGMENT, javaCompilationUnit.getParent().getElementType());
        Assert.assertEquals(IJavaElement.PACKAGE_FRAGMENT_ROOT, javaCompilationUnit.getParent().getParent().getElementType());
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
            javaFolder = builder.createFolder(mainFolder, "javaxx");
            
            IClasspathEntry javaSourceEntry = JavaCore.newSourceEntry(javaFolder.getFullPath());
            
            javaProject.setRawClasspath(new IClasspathEntry[] {javaSourceEntry}, null);

            templateFolder = builder.createFolder(mainFolder, "webapp");
            Assert.assertEquals(1, javaProject.getAllPackageFragmentRoots().length);

            javaCompilationUnit = buildJavaFile(0); 
            
            javaFile = (IFile) javaCompilationUnit.getResource();
            
            Assert.assertTrue(javaFile.exists());
            Assert.assertTrue(javaFolder.exists());
            Assert.assertTrue(templateFolder.exists());
        }
        catch (CoreException e) {
            e.printStackTrace();
        }    
    }

    /**
     * @param i
     * @return
     */
    private ICompilationUnit buildJavaFile(int i) {
        ICompilationUnit unit = null;
        try {
            IPackageFragmentRoot javaRoot = javaProject.getAllPackageFragmentRoots()[i];
            IPackageFragment orgFragment = javaRoot.createPackageFragment("org.loom", true, null);
            unit = orgFragment.createCompilationUnit("Wizard.java", "public class Wizard{}", true, null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return unit;
    }

    /**
     * 
     */
    @After
    public void deleteProject() {
        builder.deleteProject();
    }
}

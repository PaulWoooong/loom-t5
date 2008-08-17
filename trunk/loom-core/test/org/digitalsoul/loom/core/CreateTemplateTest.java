package org.digitalsoul.loom.core;

import java.io.File;

import org.digitalsoul.loom.core.prefs.Preferences;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.junit.Assert;
import org.junit.Test;


/**
 * Class CreateTemplateTest
 */
public class CreateTemplateTest extends ProjectBasedTest {

    /**
     * 
     */
    @Test
    public void testGetTemplateFolder() {
        IFolder templateFolder = new TemplateBuilder().getTemplateFragmentRoot(builder.project);
        Assert.assertTrue(templateFolder.exists());
        Assert.assertEquals("F/testProject/src/main/webapp", templateFolder.toString());
    }
    
    /**
     * 
     */
    @Test
    public void testGetPackageNames() {
        String[] packageNames = new TemplateBuilder().getPackageNames(compilationUnit);
        Assert.assertEquals(2, packageNames.length);
        Assert.assertEquals("org", packageNames[0]);
        Assert.assertEquals("loom", packageNames[1]); 
    }
    
    /**
     * 
     */
    @Test
    public void testConstructFilename() {
        String filename = new TemplateBuilder().constructTemplateFilename(compilationUnit);
        Assert.assertEquals("Wizard.tml", filename);
    }
    
    /**
     * 
     */
    @Test
    public void testCreateTemplatePackage() {
        TemplateBuilder templateBuilder = new TemplateBuilder();
        IFolder templateFolder = templateBuilder.getTemplateFragmentRoot(builder.project);
        IFolder packageFolder = templateFolder.getFolder("org/loom");
        Assert.assertFalse(packageFolder.exists());
        templateBuilder.createTemplatePackage(compilationUnit, templateFolder);
        Assert.assertTrue(packageFolder.exists());
    }
    
    /**
     * 
     */
    @Test
    public void testTemplateIsCreatedInJavaFilesDirectory() {
        Preferences.setCreateTemplateInJavaFolder(true);
        IFile templateFile = new TemplateBuilder().createTemplate(compilationUnit);
        Assert.assertTrue(templateFile.getParent().getFullPath().equals(javaFile.getParent().getFullPath()));
        Preferences.getStore().setValue(LoomConstants.CREATE_TEMPLATE_IN_JAVA_FOLDER_KEY, false);
    }
    
    /**
     * 
     */
    @Test
    public void testCreateTemplate() {
        TemplateBuilder temlateBuilder = new TemplateBuilder();
        IFolder templateFolder = temlateBuilder.getTemplateFragmentRoot(builder.project);
        IFolder packageFolder = templateFolder.getFolder("org/loom");
        IFile templateFile = packageFolder.getFile("Wizard" + Preferences.getTemplateFileExtension());
        Assert.assertFalse(templateFile.exists());
        temlateBuilder.createTemplate(compilationUnit);
        Assert.assertTrue(templateFile.exists());
    }
    
    /**
     * 
     */
    @Test
    public void testNewTemplateUsesMarkup() {
        String markup = "xyz";
        Preferences.setTemplateMarkup(markup);
        IFile templateFile = new TemplateBuilder().createTemplate(compilationUnit);
        File file = templateFile.getRawLocation().toFile();
        String readMarkup = ProjectBuilder.readFileContents(file);
        Assert.assertEquals(markup, readMarkup);
    }
}

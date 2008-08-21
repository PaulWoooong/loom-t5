package org.digitalsoul.loom.core;

import org.digitalsoul.loom.core.prefs.Preferences;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Class SwitchFilesTest
 */
public class EditorFileOpenerTest extends ProjectBasedTest {

    /**
     * 
     */
    private IFile templateFile;
    
    /**
     * 
     */
    private IFile localTemplateFile;

    /**
     * 
     */
    @Before
    public void setupTemplateFiles() {

        try {
            ProjectBuilder builder = new ProjectBuilder(javaProject);
            Assert.assertTrue(templateFolder.exists());
            IFolder orgFolder = builder.createFolder(templateFolder, "org");
            Assert.assertTrue(orgFolder.exists());
            IFolder loomFolder = builder.createFolder(orgFolder, "loom");
            Assert.assertTrue(loomFolder.exists());
            templateFile = builder.createFile(loomFolder, "Wizard.html", "<html></html>");
            Assert.assertTrue(templateFile.exists());
            
            IFolder localLoomFolder = builder.createFolder(builder.createFolder(javaFolder, "org"), "loom");
            Assert.assertTrue(localLoomFolder.exists());
            localTemplateFile = builder.createFile(localLoomFolder, "Wizard.tml", "bla");
            Assert.assertTrue(localTemplateFile.exists());
        }
        catch (CoreException coreException) {
            coreException.printStackTrace();
        }
    }

    /**
     * 
     */
    @Test
    public void testSwitchToTemplate() {
        Preferences.setJavaPackageFragmentRoot("src/main/javaxx");
        Preferences.setTemplateFileExtension(LoomConstants.HTML_FILE_EXTENSION);
        EditorFileOpener.getInstance().switchToTemplateOrJavaFile(javaFile);
        IDE.setDefaultEditor(templateFile, "org.eclipse.ui.DefaultTextEditor");
        TextEditor currentEditor = (TextEditor) getActiveEditor();
        IFileEditorInput input = (IFileEditorInput) currentEditor.getEditorInput();
        Assert.assertEquals("Wizard.html", input.getFile().getName());
    }
    
    /**
     * 
     */
    @Test
    public void testGetTemplateFilePath() {
        Preferences.setJavaPackageFragmentRoot("src/main/javaxx");
        Assert.assertEquals("src/main/javaxx/org/loom/Wizard.java", javaFile.getProjectRelativePath().toPortableString());
        IPath path = EditorFileOpener.getInstance().getTemplateFilePath(javaFile);
        Assert.assertEquals("src/main/webapp/org/loom", path.toPortableString());
    }
    
    /**
     * 
     */
    @Test
    public void testSwitchToLocalTemplate() {
        Preferences.setCreateTemplateInJavaFolder(true);
        Preferences.setTemplateFileExtension(LoomConstants.TML_FILE_EXTENSION);
        EditorFileOpener.getInstance().switchToTemplateOrJavaFile(javaFile);
        IDE.setDefaultEditor(localTemplateFile, "org.eclipse.ui.DefaultTextEditor");
        TextEditor currentEditor = (TextEditor) getActiveEditor();
        IFileEditorInput input = (IFileEditorInput) currentEditor.getEditorInput();
        Assert.assertEquals("Wizard.tml", input.getFile().getName());
    }

    /**
     * 
     */
    @Test
    public void testSwitchToJavaFile() {
        Preferences.setTemplateFileExtension(LoomConstants.HTML_FILE_EXTENSION);
        EditorFileOpener.getInstance().switchToTemplateOrJavaFile(templateFile);
        JavaEditor currentEditor = (JavaEditor) getActiveEditor();
        IFileEditorInput input = (IFileEditorInput) currentEditor.getEditorInput();
        Assert.assertEquals("Wizard.java", input.getFile().getName());
    }
}

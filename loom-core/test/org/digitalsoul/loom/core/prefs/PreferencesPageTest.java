package org.digitalsoul.loom.core.prefs;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.eclipse.swt.widgets.Event;

import org.eclipse.swt.SWT;

import org.digitalsoul.loom.core.LoomConstants;
import org.junit.Assert;
import org.junit.Test;

/**
 * Class PreferencesTest
 */
public class PreferencesPageTest extends BasePreferencesTest {
    
    /**
     * 
     */
    @Test
    public void testTemplatePackageFragmentRootLoadsFromModel() {
        String root = "src/main";
        page.templateFragmentRootTextField.setText(root);
        page.performOk();
        page = createPreferencesPage();
        Assert.assertEquals(root, page.templateFragmentRootTextField.getText());
    }
    
    /**
     * 
     */
    @Test
    public void testJavaPackageFragmentRootLoadsFromModel() {
        String root = "src/main";
        page.javaFragmentRootTextField.setText(root);
        page.performOk();
        page = createPreferencesPage();
        Assert.assertEquals(root, page.javaFragmentRootTextField.getText());
    }
    
    /**
     * 
     */
    @Test
    public void testTemplateExtensionLoadsFromModel() {
        page.fileExtensionCombo.select(1);
        page.performOk();
        page = createPreferencesPage();
        Assert.assertEquals(LoomConstants.HTML_FILE_EXTENSION, page.fileExtensionCombo.getItem(page.fileExtensionCombo.getSelectionIndex()));
    }
    
    /**
     * 
     */
    @Test
    public void testCreateTemplateInJavaFolderLoadsFromModel() {
        store.setValue(LoomConstants.CREATE_TEMPLATE_IN_JAVA_FOLDER_KEY, true);
        page.setupLoadedPreferences();
        Assert.assertTrue(page.createTemplateInJavaFolderCheckbox.getSelection());
    }

    /**
     * 
     */
    @Test
    public void testCreateTemplateInJavaFolderDisabledByDefault() {
        Assert.assertFalse(page.createTemplateInJavaFolderCheckbox.getSelection());
    }
    
    /**
     * 
     */
    @Test
    public void testRootsAreDisabledIfCreateTemplateInJavaFolder() {
        Preferences.setCreateTemplateInJavaFolder(true);
        page.setupLoadedPreferences();
        Assert.assertFalse(page.templateFragmentRootTextField.isEnabled());
        Assert.assertFalse(page.javaFragmentRootTextField.isEnabled());
    }
    
    /**
     * 
     */
    @Test
    public void testTextFieldsShowRoots() {
        Preferences.setJavaPackageFragmentRoot("The Roots");
        Preferences.setTemplatePackageFragmentRoot("w00t");
        page.setupLoadedPreferences();
        Assert.assertEquals("The Roots", page.javaFragmentRootTextField.getText());
        Assert.assertEquals("w00t", page.templateFragmentRootTextField.getText());
    }
    
    /**
     * 
     */
    @Test
    public void testCreateTemplateInJavaFolderChangesModel() {
        Assert.assertFalse(page.createTemplateInJavaFolderCheckbox.getSelection());
        page.createTemplateInJavaFolderCheckbox.setSelection(true);
        page.performOk();
        Assert.assertTrue(Preferences.isCreateTemplateInJavaFolder());
    }
    
    /**
     * 
     */
    @Test
    public void testCreateTemplateInJavaFolderDisablesRoots() {
        page.createTemplateInJavaFolderCheckbox.setSelection(true);
        page.createTemplateInJavaFolderCheckbox.notifyListeners(SWT.Selection, createCheckboxEvent());
        Assert.assertFalse(page.templateFragmentRootTextField.isEnabled());
        Assert.assertFalse(page.javaFragmentRootTextField.isEnabled());
    }
    
    /**
     * @return
     */
    private Event createCheckboxEvent() {
        Event event = new Event();
        event.widget = page.createTemplateInJavaFolderCheckbox;
        return event;
    }

    /**
     * 
     */
    @Test
    public void testCreateTemplateInJavaFolderEnablesTemplateRoot() {
        page.createTemplateInJavaFolderCheckbox.setSelection(false);
        page.createTemplateInJavaFolderCheckbox.notifyListeners(SWT.Selection, createCheckboxEvent());
        Assert.assertTrue(page.templateFragmentRootTextField.isEnabled());
        Assert.assertTrue(page.javaFragmentRootTextField.isEnabled());
    }
    
    /**
     * 
     */
    @Test
    public void testChangingTemplatePackageFragmentRootChangesModel() {
        page.templateFragmentRootTextField.setText("/src/main");
        page.performOk();
        Assert.assertEquals("/src/main", Preferences.getTemplateFragmentRootPath());
    }
    
    /**
     * 
     */
    @Test
    public void testChangingJavaPackageFragmentRootChangesModel() {
        page.javaFragmentRootTextField.setText("/src/main");
        page.performOk();
        Assert.assertEquals("/src/main", Preferences.getJavaFragmentRootPath());
    }
    
    /**
     * 
     */
    @Test
    public void testChangingTemplateFileExtensionChangesModel() {
        page.fileExtensionCombo.select(1);
        page.performOk();
        Assert.assertEquals(LoomConstants.HTML_FILE_EXTENSION, Preferences.getTemplateFileExtension());
    }

    /**
     * 
     */
    @Test
    public void testMarkupIsShownInTextAreaOnLoad() {
       Preferences.setTemplateMarkup("fooBar");
       page.setupLoadedPreferences();
       Assert.assertEquals("fooBar", page.markupTextArea.getText());
    }
    
    /**
     * 
     */
    @Test
    public void testMarkupIsShownInTextAreaByDefault() {
        Assert.assertEquals("<html></html>", page.markupTextArea.getText());
    }
}
package org.digitalsoul.loom.core.prefs;


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
    public void testCreateTemplateInJavaFolderDisablesTemplateRoot() {
        page.createTemplateInJavaFolderCheckbox.setSelection(true);
        Assert.assertTrue(page.templateFragmentRootTextField.isEnabled());
    }
    
    /**
     * 
     */
    @Test
    public void testCreateTemplateInJavaFolderEnablesTemplateRoot() {
        page.createTemplateInJavaFolderCheckbox.setSelection(false);
        Assert.assertTrue(page.templateFragmentRootTextField.isEnabled());
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
    public void testChangingTemplateFileExtensionChangesModel() {
        page.fileExtensionCombo.select(1);
        page.performOk();
        Assert.assertEquals(LoomConstants.HTML_FILE_EXTENSION, Preferences.getTemplateFileExtension());
    }
}
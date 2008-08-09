package org.digitalsoul.loom.core.prefs;


import org.junit.After;

import org.junit.Before;

import org.digitalsoul.loom.core.BaseTester;
import org.digitalsoul.loom.core.LoomConstants;
import org.digitalsoul.loom.core.LoomCorePlugin;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Class PreferencesTest
 */
public class PreferencesTest extends BaseTester {
    
    /**
     * 
     */
    private IPreferenceStore store = LoomCorePlugin.getDefault().getPreferenceStore();
    
    /**
     * 
     */
    @After
    @Before
    public void setup() {
        PreferencesPage page = getPreferencesPage();
        page.performDefaults();
        page.performOk();
    }
    
    /**
     * 
     */
    @Test
    public void testTemplateFileExtensionDefaultIsCorrect() {
        String templateFileExtensionKey = LoomConstants.TEMPLATE_FILE_EXTENSION_KEY;
        Assert.assertEquals(LoomConstants.DEFAULT_TEMPLATE_FILE_EXTENSION, store.getDefaultString(templateFileExtensionKey)); 
    }
    
    /**
     * 
     */
    @Test
    public void testTemplatePackageFragmentRootDefaultIsCorrect() {
        String templatePackageFragmentRootKey = LoomConstants.TEMPLATE_PACKAGE_FRAGMENT_ROOT_KEY;
        Assert.assertEquals(LoomConstants.DEFAULT_TEMPLATE_PACKAGE_FRAGMENT_ROOT, store.getDefaultString(templatePackageFragmentRootKey));
    }
    
    /**
     * 
     */
    @Test
    public void testIgnoredFoldersDefaultIsCorrect() {
        String ignoredFoldersKey = LoomConstants.IGNORED_FOLDERS_KEY;
        Assert.assertEquals(LoomConstants.DEFAULT_IGNORED_FOLDERS, store.getDefaultString(ignoredFoldersKey));
    }
    
    @Test
    public void testTemplatePackageFragmentRootUsesSavedModel() {
        PreferencesPage page = getPreferencesPage();
        String root = "src/main";
        page.templateFragmentRootTextField.setText(root);
        page.performOk();
        page = getPreferencesPage();
        Assert.assertEquals(root, page.templateFragmentRootTextField.getText());
    }
    
    @Test
    public void testTemplateExtensionUsesSavedModel() {
        PreferencesPage page = getPreferencesPage();
        page.fileExtensionCombo.select(1);
        page.performOk();
        page = getPreferencesPage();
        Assert.assertEquals(LoomConstants.HTML_FILE_EXTENSION, page.fileExtensionCombo.getItem(page.fileExtensionCombo.getSelectionIndex()));
    }
    
    /**
     * 
     */
    @Test
    public void testChangingTemplatePackageFragmentRootChangesModel() {
        PreferencesPage page = getPreferencesPage();
        page.templateFragmentRootTextField.setText("/src/main");
        page.performOk();
        Assert.assertEquals("/src/main", Preferences.getTemplateFragmentRootPath());
    }
    
    /**
     * 
     */
    @Test
    public void testChangingTemplateFileExtensionChangesModel() {
        PreferencesPage page = getPreferencesPage();
        page.fileExtensionCombo.select(1);
        page.performOk();
        Assert.assertEquals(LoomConstants.HTML_FILE_EXTENSION, Preferences.getTemplateFileExtension());
    }

    /**
     * @return
     */
    private PreferencesPage getPreferencesPage() {
        PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(getShell(), "org.digitalsoul.loom.core.prefs.PreferencesPage", null, null);
        PreferencesPage page = (PreferencesPage) dialog.getSelectedPage();
        return page;
    }
}
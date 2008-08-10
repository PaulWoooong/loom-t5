package org.digitalsoul.loom.core.prefs;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.dialogs.PreferencesUtil;

import org.digitalsoul.loom.core.LoomCorePlugin;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.After;
import org.junit.Before;

import org.digitalsoul.loom.core.BaseTester;


public class BasePreferencesTest extends BaseTester {

    /**
     * 
     */
    protected IPreferenceStore store;
    
    /**
     * 
     */
    protected PreferencesPage page;
    
    /**
     * 
     */
    @Before
    public void setup() {
        this.page = createPreferencesPage();
        page.performDefaults();
        store = LoomCorePlugin.getDefault().getPreferenceStore();
    }
    
    /**
     * 
     */
    @After
    public void cleanup() {
        page = createPreferencesPage();
        page.performDefaults();
        page.performOk();
    }
    
    /**
     * @return
     */
    protected PreferencesPage createPreferencesPage() {
        PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(getShell(), "org.digitalsoul.loom.core.prefs.PreferencesPage", null, null);
        PreferencesPage page = (PreferencesPage) dialog.getSelectedPage();
        return page;
    }
}

package org.digitalsoul.loom.core;

import org.digitalsoul.loom.core.prefs.Preferences;

import org.digitalsoul.loom.core.prefs.PreferencesPage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.dialogs.PreferencesUtil;

import org.junit.Before;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;


/**
 * Class BaseTester
 */
public class BaseTester {

    
    /**
     * 
     */
    @Before
    public void setupDefaults() {
        Preferences.resetDefaults();
    }
    
    /**
     * @return
     */
    protected IEditorPart getActiveEditor() {
        IWorkbenchPage activePage = getActivePage();
        return activePage.getActiveEditor();
    }

    /**
     * @return
     */
    protected static Shell getShell() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    }
    
    /**
     * @return
     */
    protected IWorkbenchPage getActivePage() {
        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        return activePage;
    }
}

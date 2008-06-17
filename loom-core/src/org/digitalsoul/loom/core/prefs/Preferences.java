package org.digitalsoul.loom.core.prefs;

import org.digitalsoul.loom.core.LoomConstants;
import org.digitalsoul.loom.core.LoomCorePlugin;
import org.eclipse.jface.preference.IPreferenceStore;


public class Preferences {

    private static String get (String key) {
        return LoomCorePlugin.getDefault().getPreferenceStore().getString(key);
    }

    public static String getTemplateFileExtension() {
        return get(LoomConstants.TEMPLATE_FILE_EXTENSION_KEY);
    }

    public static String getTemplateFragmentRootPath() {
        return get(LoomConstants.TEMPLATE_PACKAGE_FRAGMENT_ROOT_KEY);        
    }

    public static void setupDefaults(IPreferenceStore store) {
        store.setDefault(LoomConstants.TEMPLATE_FILE_EXTENSION_KEY, LoomConstants.TML_FILE_EXTENSION);
        store.setDefault(LoomConstants.TEMPLATE_PACKAGE_FRAGMENT_ROOT_KEY, LoomConstants.DEFAULT_TEMPLATE_PACKAGE_FRAGMENT_ROOT);
    }
}

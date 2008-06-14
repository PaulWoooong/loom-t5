package org.digitalsoul.loom.core.prefs;

import org.digitalsoul.loom.core.LoomConstants;
import org.digitalsoul.loom.core.LoomCorePlugin;


public class Preferences {

    private static String get (String key) {
        return LoomCorePlugin.getDefault().getPreferenceStore().getString(key);
    }

    public static String getTemplateFileExtension() {
        return get(LoomConstants.TEMPLATE_FILE_EXTENSION);
    }
}

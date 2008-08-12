package org.digitalsoul.loom.core.prefs;

import org.digitalsoul.loom.core.LoomConstants;
import org.digitalsoul.loom.core.LoomCorePlugin;
import org.eclipse.jface.preference.IPreferenceStore;


/**
 * Class Preferences
 */
public class Preferences {

    /**
     * @param key
     * @return
     */
    private static String get (String key) {
        return getStore().getString(key);
    }
    
    /**
     * @return
     */
    public static IPreferenceStore getStore() {
        return LoomCorePlugin.getDefault().getPreferenceStore();    
    }

    /**
     * @return
     */
    public static String getTemplateFileExtension() {
        return get(LoomConstants.TEMPLATE_FILE_EXTENSION_KEY);
    }

    /**
     * @return
     */
    public static String getTemplateFragmentRootPath() {
        return get(LoomConstants.TEMPLATE_PACKAGE_FRAGMENT_ROOT_KEY);        
    }
    
    /**
     * @return
     */
    public static String getSwitchFileIgnoredFolderNames () {
        return get(LoomConstants.IGNORED_FOLDERS_KEY);
    }
    
    /**
     * @return
     */
    public static boolean isCreateTemplateInJavaFolder() {
        return LoomCorePlugin.getDefault().getPreferenceStore().getBoolean(LoomConstants.CREATE_TEMPLATE_IN_JAVA_FOLDER_KEY);
    }
    
    /**
     * @param value
     */
    public static void setCreateTemplateInJavaFolder(boolean value) {
        getStore().setValue(LoomConstants.CREATE_TEMPLATE_IN_JAVA_FOLDER_KEY, value);
    }
    
    public static void setTemplateFileExtension(String extension) {
        getStore().setValue(LoomConstants.TEMPLATE_FILE_EXTENSION_KEY, extension);
    }

    /**
     */
    public static void setupDefaults() {
        getStore().setDefault(LoomConstants.TEMPLATE_FILE_EXTENSION_KEY, LoomConstants.DEFAULT_TEMPLATE_FILE_EXTENSION);
        getStore().setDefault(LoomConstants.TEMPLATE_PACKAGE_FRAGMENT_ROOT_KEY, LoomConstants.DEFAULT_TEMPLATE_PACKAGE_FRAGMENT_ROOT);
        getStore().setDefault(LoomConstants.CREATE_TEMPLATE_IN_JAVA_FOLDER_KEY, LoomConstants.DEFAULT_CREATE_TEMPLATE_IN_JAVA_FOLDER);
        getStore().setDefault(LoomConstants.IGNORED_FOLDERS_KEY, LoomConstants.DEFAULT_IGNORED_FOLDERS);
    }

    /**
     * 
     */
    public static void resetDefaults() {
        setupDefaults();
        setCreateTemplateInJavaFolder(getStore().getDefaultBoolean(LoomConstants.CREATE_TEMPLATE_IN_JAVA_FOLDER_KEY));
        setTemplateFileExtension(getStore().getDefaultString(LoomConstants.TEMPLATE_FILE_EXTENSION_KEY));
    }
}

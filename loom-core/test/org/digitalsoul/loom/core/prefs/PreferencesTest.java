/**
 * Copyright 2008 Loom Developers. This file is part of the loom eclipse plugin for eclipse
 * and is licensed under the GPL version 3. 
 * Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */
package org.digitalsoul.loom.core.prefs;

import org.digitalsoul.loom.core.LoomConstants;
import org.junit.Assert;
import org.junit.Test;


/**
 * Class PreferencesTest
 */
public class PreferencesTest extends BasePreferencesTest {

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
    public void testTemplateFileInJavaFolderDefaultIsCorrect() {
        String isCreateTemplateInJavaFolderKey = LoomConstants.CREATE_TEMPLATE_IN_JAVA_FOLDER_KEY;
        Assert.assertEquals(LoomConstants.DEFAULT_CREATE_TEMPLATE_IN_JAVA_FOLDER, store.getDefaultBoolean(isCreateTemplateInJavaFolderKey)); 
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
    public void testJavaPackageFragmentRootDefaultIsCorrect() {
        String javaPackageFragmentRootKey = LoomConstants.JAVA_PACKAGE_FRAGMENT_ROOT_KEY;
        Assert.assertEquals(LoomConstants.DEFAULT_JAVA_PACKAGE_FRAGMENT_ROOT, store.getDefaultString(javaPackageFragmentRootKey));
    }
    
    /**
     * 
     */
    @Test
    public void testTemplateMarkupDefaultIsCorrect() {
        String markupKey = LoomConstants.TEMPLATE_MARKUP_KEY;
        Assert.assertEquals(LoomConstants.DEFAULT_TEMPLATE_MARKUP, store.getDefaultString(markupKey));
    }
    
    /**
     * 
     */
    @Test
    public void testIgnoredFoldersDefaultIsCorrect() {
        String ignoredFoldersKey = LoomConstants.IGNORED_FOLDERS_KEY;
        Assert.assertEquals(LoomConstants.DEFAULT_IGNORED_FOLDERS, store.getDefaultString(ignoredFoldersKey));
    }
}

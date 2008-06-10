/**
 * Copyright 2008 Loom Developers. This file is part of the loom eclipse plugin for eclipse
 * and is licensed under the GPL version 3. 
 * Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */
package org.digitalsoul.loom.core;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class LoomCorePlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "loom=core";

	private static LoomCorePlugin plugin;

	public LoomCorePlugin() {
		plugin = this;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static LoomCorePlugin getDefault() {
		return plugin;
	}

	
	
    @Override
    protected void initializeDefaultPreferences(IPreferenceStore store) {
        super.initializeDefaultPreferences(store);
        store.setDefault(LoomConstants.TEMPLATE_FILE_EXTENSION, LoomConstants.TML_FILE_EXTENSION);
    }
}
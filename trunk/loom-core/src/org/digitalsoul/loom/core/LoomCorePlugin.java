/**
 * Copyright 2008 Loom Developers. This file is part of the loom eclipse plug-in for eclipse
 * and is licensed under the GPL version 3. 
 * Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */
package org.digitalsoul.loom.core;

import org.digitalsoul.loom.core.prefs.Preferences;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class LoomCorePlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "loom=core";

	private static LoomCorePlugin plugin;

	public LoomCorePlugin() {
	    super();
	    if (plugin == null) {
	        plugin = this;
	    }
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static LoomCorePlugin getDefault() {
	    if (plugin == null) {
	        plugin = new LoomCorePlugin();
	    }
		return plugin;
	}
	
    @Override
    protected void initializeDefaultPluginPreferences () {
        Preferences.setupDefaults(getDefault().getPreferenceStore());       
    }
}
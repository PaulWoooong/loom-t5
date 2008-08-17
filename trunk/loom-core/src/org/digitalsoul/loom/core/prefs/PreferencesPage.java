/**
 * Copyright 2008 Loom Developers. This file is part of the loom eclipse plugin for eclipse and is licensed under the
 * GPL version 3. Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */
package org.digitalsoul.loom.core.prefs;

import org.digitalsoul.loom.core.LoomConstants;
import org.digitalsoul.loom.core.LoomCorePlugin;
import org.digitalsoul.loom.core.ProjectBuilder;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import java.io.File;

/**
 * Class PreferencesPage
 */
public class PreferencesPage extends PreferencePage implements IWorkbenchPreferencePage {

    /**
     * 
     */
    protected Combo fileExtensionCombo;

    /**
     * 
     */
    protected Text templateFragmentRootTextField;

    /**
     * 
     */
    protected Text javaFragmentRootTextField;

    /**
     * 
     */
    protected Button createTemplateInJavaFolderCheckbox;

    /**
     * 
     */
    private String[] fileExtensions = { LoomConstants.TML_FILE_EXTENSION, LoomConstants.HTML_FILE_EXTENSION };

    /**
     * 
     */
    protected Listener checkboxSelectionListener;

    /**
     * 
     */
    protected Text markupTextArea; 
    
    /**
     * 
     */
    public PreferencesPage() {
        setPreferenceStore(LoomCorePlugin.getDefault().getPreferenceStore());
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {
        final Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(1, false);
        composite.setLayout(layout);
        Label label = new Label(composite, SWT.NONE);
        label.setText("Template File Ending");
        fileExtensionCombo = new Combo(composite, SWT.READ_ONLY);
        fileExtensionCombo.setItems(fileExtensions);
        fileExtensionCombo.setVisibleItemCount(fileExtensions.length);

        label = new Label(composite, SWT.NONE);
        label.setText("");
        
        templateFragmentRootTextField = createLabelAndTextField(composite, "Template Package Fragment Root");
        javaFragmentRootTextField = createLabelAndTextField(composite, "Java Package Fragment Root");

        createTemplateInJavaFolderCheckbox = new Button(composite, SWT.CHECK);
        createTemplateInJavaFolderCheckbox.setText("Always create a template in the java file's folder");
        checkboxSelectionListener = new Listener() {

            public void handleEvent(Event event) {
                if (event.widget == createTemplateInJavaFolderCheckbox) {
                    boolean selected = createTemplateInJavaFolderCheckbox.getSelection();
                    templateFragmentRootTextField.setEnabled(!selected);
                    javaFragmentRootTextField.setEnabled(!selected);
                }
            }
        };
        createTemplateInJavaFolderCheckbox.addListener(SWT.Selection, checkboxSelectionListener);
        
        label = new Label(composite, SWT.NONE);
        label.setText("");

        label = new Label(composite, SWT.NONE);
        label.setText("Template Markup");
        markupTextArea = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        GridData data = new GridData();
        data.minimumWidth = 350;
        data.minimumHeight = 100;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        markupTextArea.setLayoutData(data);
        setupLoadedPreferences();
        return parent;
    }
    
    /**
     * @param readFileContents
     */
    private void setMarkup(String markup) {
        markupTextArea.setText(markup);        
    }

    /**
     * @param composite
     * @param text
     * @return
     */
    private Text createLabelAndTextField(Composite composite, String text) {
        Label label = new Label(composite, SWT.NONE);
        label.setText(text);
        Text textField = new Text(composite, SWT.BORDER);
        GridData data = new GridData();
        data.minimumWidth = 350;
        data.grabExcessHorizontalSpace = true;
        textField.setLayoutData(data);
        return textField;
    }

    /**
     * 
     */
    protected void setupLoadedPreferences() {
        Boolean isCreateTemplateInJavaFolder = Preferences.isCreateTemplateInJavaFolder();
        selectComboFileExtension(Preferences.getTemplateFileExtension());
        templateFragmentRootTextField.setText(Preferences.getTemplateFragmentRootPath());
        javaFragmentRootTextField.setText(Preferences.getJavaFragmentRootPath());
        createTemplateInJavaFolderCheckbox.setSelection(isCreateTemplateInJavaFolder);
        templateFragmentRootTextField.setEnabled(!isCreateTemplateInJavaFolder);
        javaFragmentRootTextField.setEnabled(!isCreateTemplateInJavaFolder);
        setMarkup(Preferences.getTemplateMarkup());
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults() {
        super.performDefaults();
        String defaultFileExtension = getPreferenceStore().getDefaultString(LoomConstants.TEMPLATE_FILE_EXTENSION_KEY);
        String defaultTemplateFragmentRootPath = getPreferenceStore()
        .getDefaultString(LoomConstants.TEMPLATE_PACKAGE_FRAGMENT_ROOT_KEY);
        String defaultJavaFragmentRootPath = getPreferenceStore()
        .getDefaultString(LoomConstants.JAVA_PACKAGE_FRAGMENT_ROOT_KEY);
        boolean isCreateTemplateInJavaFolder = getPreferenceStore()
        .getDefaultBoolean(LoomConstants.CREATE_TEMPLATE_IN_JAVA_FOLDER_KEY);
        String defaultMarkup = getPreferenceStore().getDefaultString(LoomConstants.TEMPLATE_MARKUP_KEY);
        
        selectComboFileExtension(defaultFileExtension);
        templateFragmentRootTextField.setText(defaultTemplateFragmentRootPath);
        javaFragmentRootTextField.setText(defaultJavaFragmentRootPath);
        createTemplateInJavaFolderCheckbox.setSelection(isCreateTemplateInJavaFolder);
        setMarkup(defaultMarkup);
    }

    /**
     * @param selectedFileExtension
     */
    private void selectComboFileExtension(String selectedFileExtension) {
        for (int i = 0; i < fileExtensions.length; i++) {
            if (fileExtensions[i].equals(selectedFileExtension)) {
                fileExtensionCombo.select(i);
                break;
            }
        }
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        String selectedItem = fileExtensionCombo.getItem(fileExtensionCombo.getSelectionIndex());
        Preferences.setCreateTemplateInJavaFolder(createTemplateInJavaFolderCheckbox.getSelection());
        Preferences.setTemplateFileExtension(selectedItem);
        Preferences.setTemplatePackageFragmentRoot(templateFragmentRootTextField.getText());
        Preferences.setJavaPackageFragmentRoot(javaFragmentRootTextField.getText());
        Preferences.setTemplateMarkup(markupTextArea.getText());
        return super.performOk();
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }
}

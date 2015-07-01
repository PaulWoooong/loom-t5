## News: Issues vs. new features ##
I hadn't checked the wiki pages for some time and didn't see all your comments until now.
I will look into these issues soon and will try to provide fixes.

**When it comes to new features: as i'm not working with wicket or tapestry in the near future i won't work on any new features for Loom. However if someone is keen to take over the codebase and add new features to it, feel free to drop me a mail.**

## News: Beta Release for Version 1.0.4 ##

I made a new beta for 1.0.4 available. Thanks to Martijn there is one bug less in this release. This version can now be installed via the usual update-site (see below)

The main three features of this release are the following:

1) Creation of a template file for a java file by using a context menu (see CreateTemplateFeature). The markup can be specified in the preferences.

2) Using of paths to find templates/java files (e.g. /src/main/java and /src/main/webapps). Beforehand the whole tree was searched which took longer and could bring up template files from wrong folders.
The paths can be specified in the preferences.

3) Wicket-Compatibility: As i use also wicket at work i added the possibility to switch to a java/template file in the same folder.


Checkout the new PreferencesPage

## News: Created google group for discussions ##
~~I created a google group for further discussions: [http://groups.google.com/group/loom-t5](http://groups.google.com/group/loom-t5)~~
Due to the inactivity of the project the group is closed.

## News: Version 1.0.2 ##

I updated the shortcut for the SwitchFilesFeature which _should_ now be Ctrl+Alt+X on all platfroms.

### About Loom ###

Loom is a plugin for eclipse aiming to simplify the usage of tapestry5 and wicket in eclipse. The focus lies on version 5 of tapestry but the current feature (and future features) may work for earlier versions as well.

The plugin has two main features, one which allows the user to switch between a template and a java file by using a keyboard shortcut. Information on how to use this feature can be found [here](SwitchFilesFeature.md).
The other feature (only in 1.0.4 beta) is a context-menu entry which allows to create a template for a given javafile. See CreateTemplateFeature.


### Installation ###

The plugin can be installed by using the following update-site:
http://2drockers.com/loom/update-site

### Requirements ###

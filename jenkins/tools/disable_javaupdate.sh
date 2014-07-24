#!/bin/bash

####################################################################################################
# Creates pref file for Java 7 that has setting which turns off the auto update check feature
# Created by AS (3-2-13)
####################################################################################################
####################################################################################################

/bin/echo "Beginning running disable_java_updates script"

####################################################################################################
# Get number variable needed to set suppression of update reminder
####################################################################################################

NUMBER=`/bin/cat /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Enabled.plist |grep ';deploy=' |cut -d"=" -f2 |cut -d"<" -f1`
echo The number for suppression of this version of Java is "$NUMBER"

# Verify that it received a numeric value
case "$NUMBER" in
[0-9]*)
echo "Entry is a numeric value. Continuing..."
;;
* )
echo "Error: This entry is not a number. Will fail to properly suppress update pop up."
;;
esac

####################################################################################################
# Remove Updater Launch Agent Sym Link that gets created during updates
####################################################################################################

/bin/echo "Checking to see if Launch Agent sym link exists..."

if [ -f /Library/LaunchAgents/com.oracle.java.Java-Updater.plist ]; then
/bin/echo "Launch Agent exists. Removing."

sudo rm /Library/LaunchAgents/com.oracle.java.Java-Updater.plist
/bin/echo "Removed Update Launch Agent Sym Link"

else
/bin/echo "Launch Agent does not exist."
fi

####################################################################################################
# Remove Updater Launch Daemon Sym Link that gets created during updates
####################################################################################################

/bin/echo "Checking to see if Launch Daemon sym link exists..."

if [ -f /Library/LaunchDaemons/com.oracle.java.Helper-Tool.plist ]; then
/bin/echo "Launch Daemon exists. Removing."

sudo rm /Library/LaunchDaemons/com.oracle.java.Helper-Tool.plist
/bin/echo "Removed Update Launch Daemon Sym Link"

else
/bin/echo "Launch Daemon does not exist."
fi

####################################################################################################
####################################################################################################

# Check to see if Java Plugin exists
if [ -d /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home ]; then
echo "Java Plugin is installed, continuing..."

if [ ! -f /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties ]; then
/bin/echo "The deployment.properties file does not yet exist. Will create..."

# Create deployment.properties file
sudo touch /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo "Created deployment.properties file"

# Create deployment.config file
sudo touch /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.config
/bin/echo "Created deployment.config file"


# Change ownership on this new file
sudo chown root:wheel /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo "Changed ownership on deployment.properties file"

# Change ownership on this new file
sudo chown root:wheel /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.config
/bin/echo "Changed ownership on deployment.config file"

# Change permissions on this file
sudo chmod 755 /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo "Changed permissions on deployment.properties file"

sudo chmod 755 /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.config
/bin/echo "Changed permissions on deployment.config file"


# Write contents of this file
/bin/echo '#deployment.properties' > /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo deployment.macosx.check.update.locked >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo deployment.macosx.check.update=false >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo deployment.expiration.decision.suppression."$NUMBER".locked >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo deployment.expiration.decision.suppression."$NUMBER"=true >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo deployment.expiration.decision."$NUMBER".locked >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo deployment.expiration.decision."$NUMBER"=later >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo deployment.expiration.check.enabled.locked >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo deployment.expiration.check.enabled=false >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo "Wrote content to deployment.properties file. Have a wonderful day."\

/bin/echo deployment.system.config.mandatory=false >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.config
/bin/echo deployment.system.config=/Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.config
/bin/echo "Wrote content to deployment.config file. Have a wonderful day."\


### Addition from java.com
# Create deployment.properties file
sudo touch /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo "Created deployment.properties file"

# Create deployment.config file
sudo touch /Library/Application\ Support/Oracle/Java/Deployment/deployment.config
/bin/echo "Created deployment.config file"


# Change ownership on this new file
sudo chown root:wheel /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo "Changed ownership on deployment.properties file"

# Change ownership on this new file
sudo chown root:wheel /Library/Application\ Support/Oracle/Java/Deployment/deployment.config
/bin/echo "Changed ownership on deployment.config file"

# Change permissions on this file
sudo chmod 755 /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo "Changed permissions on deployment.properties file"

sudo chmod 755 /Library/Application\ Support/Oracle/Java/Deployment/deployment.config
/bin/echo "Changed permissions on deployment.config file"


# Write contents of this file
/bin/echo '#deployment.properties' > /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo deployment.macosx.check.update.locked >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo deployment.macosx.check.update=false >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo deployment.expiration.decision.suppression."$NUMBER".locked >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo deployment.expiration.decision.suppression."$NUMBER"=true >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo deployment.expiration.decision."$NUMBER".locked >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo deployment.expiration.decision."$NUMBER"=later >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo deployment.expiration.check.enabled.locked >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo deployment.expiration.check.enabled=false >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo "Wrote content to deployment.properties file. Have a wonderful day."\

/bin/echo deployment.system.config.mandatory=false >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.config
/bin/echo deployment.system.config=/Library/Application\ Support/Oracle/Java/Deployment/deployment.properties >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.config
/bin/echo "Wrote content to deployment.config file. Have a wonderful day."\


else
/bin/echo "deployment.properties file already exists. Removing and building new version..."


# Delete existing version of the file
sudo rm -f /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo "Deleted previous deployment.properties file"

sudo rm -f /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.config
/bin/echo "Deleted previous deployment.config file"


# Create deployment.properties file
sudo touch /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo "Created deployment.properties file"

# Create deployment.config file
sudo touch /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.config
/bin/echo "Created deployment.config file"


# Change ownership on this new file
sudo chown root:wheel /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo "Changed ownership on deployment.properties file"

# Change ownership on this new file
sudo chown root:wheel /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.config
/bin/echo "Changed ownership on deployment.config file"


# Change permissions on this file
sudo chmod 755 /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo "Changed permissions on deployment.properties file"

# Change permissions on this file
sudo chmod 755 /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.config
/bin/echo "Changed permissions on deployment.config file"


# Write contents of this file
/bin/echo '#deployment.properties' > /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo deployment.macosx.check.update.locked >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo deployment.macosx.check.update=false >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo deployment.expiration.decision.suppression."$NUMBER".locked >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo deployment.expiration.decision.suppression."$NUMBER"=true >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo deployment.expiration.decision."$NUMBER".locked >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo deployment.expiration.decision."$NUMBER"=later >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo deployment.expiration.check.enabled.locked >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo deployment.expiration.check.enabled=false >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties
/bin/echo "Wrote content to deployment.properties file. Have a wonderful day."\

/bin/echo deployment.system.config.mandatory=false >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.config
/bin/echo deployment.system.config=/Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.properties >> /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/deployment.config
/bin/echo "Wrote content to deployment.config file. Have a wonderful day."\

###Addition from Java.com
# Delete existing version of the file
sudo rm -f /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo "Deleted previous deployment.properties file"

sudo rm -f /Library/Application\ Support/Oracle/Java/Deployment/deployment.config
/bin/echo "Deleted previous deployment.config file"


# Create deployment.properties file
sudo touch /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo "Created deployment.properties file"

# Create deployment.config file
sudo touch /Library/Application\ Support/Oracle/Java/Deployment/deployment.config
/bin/echo "Created deployment.config file"


# Change ownership on this new file
sudo chown root:wheel /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo "Changed ownership on deployment.properties file"

# Change ownership on this new file
sudo chown root:wheel /Library/Application\ Support/Oracle/Java/Deployment/deployment.config
/bin/echo "Changed ownership on deployment.config file"


# Change permissions on this file
sudo chmod 755 /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo "Changed permissions on deployment.properties file"

# Change permissions on this file
sudo chmod 755 /Library/Application\ Support/Oracle/Java/Deployment/deployment.config
/bin/echo "Changed permissions on deployment.config file"


# Write contents of this file
/bin/echo '#deployment.properties' > /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo deployment.macosx.check.update.locked >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo deployment.macosx.check.update=false >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo deployment.expiration.decision.suppression."$NUMBER".locked >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo deployment.expiration.decision.suppression."$NUMBER"=true >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo deployment.expiration.decision."$NUMBER".locked >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo deployment.expiration.decision."$NUMBER"=later >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo deployment.expiration.check.enabled.locked >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo deployment.expiration.check.enabled=false >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.properties
/bin/echo "Wrote content to deployment.properties file. Have a wonderful day."\

/bin/echo deployment.system.config.mandatory=false >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.config
/bin/echo deployment.system.config=/Library/Application\ Support/Oracle/Java/Deployment/deployment.properties >> /Library/Application\ Support/Oracle/Java/Deployment/deployment.config
/bin/echo "Wrote content to deployment.config file. Have a wonderful day."\

fi

else
echo "Error: Failure to find Java Plugin path. Either Java is not installed, or the path within the plugin has changed. Exiting"

fi

/bin/echo "Finished running disable_java_updates script"

####################################################################################################
####################################################################################################
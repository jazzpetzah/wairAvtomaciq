#!/bin/bash -e

if [ "$(id -u)" != "0" ]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi

# Java Plugin Location
JAVA_PLUGIN="/Library/Internet Plug-Ins/JavaAppletPlugin.plugin"
# Folder Location
FOLDER_LOC="/Library/Application Support/Oracle/Java/Deployment"
# Config
CONFIG_FILE_NAME="deployment.config"
CONFIG_FILE="$FOLDER_LOC/$CONFIG_FILE_NAME"
# Properties
PROP_FILE_NAME="deployment.properties"
PROP_FILE="$FOLDER_LOC/$PROP_FILE_NAME"

if [ -e "$JAVA_PLUGIN" ]; then
	echo "Java Plugin is installed..."
    echo "Processing the $CONFIG_FILE_NAME file..."
    mkdir -p "$FOLDER_LOC"
	echo "deployment.system.config=file\://$PROP_FILE" > "$CONFIG_FILE"
	echo "deployment.system.config.mandatory=false" >> "$CONFIG_FILE"
	echo "Wrote content to $CONFIG_FILE_NAME file."
	chown root:wheel "$CONFIG_FILE"
	
	echo "Processing the $PROP_FILE_NAME file..."
	echo '#deployment.properties' > "$PROP_FILE"
	echo deployment.security.validation.ocsp=false >> "$PROP_FILE"
	echo deployment.security.validation.ocsp.locked >> "$PROP_FILE"
	echo deployment.macosx.check.update=false >> "$PROP_FILE"
	echo deployment.macosx.check.update.locked >> "$PROP_FILE"
	echo deployment.expiration.check.enabled=false >> "$PROP_FILE"
	echo deployment.expiration.check.enabled.locked >> "$PROP_FILE"
	echo deployment.console.startup.mode=HIDE >> "$PROP_FILE"
	echo "Wrote content to $PROP_FILE_NAME file."
	chown root:wheel "$PROP_FILE"

	echo "Fixing the auto updater preference file..."
	defaults write /Library/Preferences/com.oracle.java.Java-Updater JavaAutoUpdateEnabled -bool false
	echo "Changed the auto updater preference file."
	echo "Java settings have been deployed. Exiting"
else
	echo "Error: Failure to find Java Plugin path. Either Java is not installed, or the path within the plugin has changed."
	exit 1
fi

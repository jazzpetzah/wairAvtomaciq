#!/bin/sh

# Java Plugin Location
javaPlugin="/Library/Internet Plug-Ins/JavaAppletPlugin.plugin"

# Config File Location
configFile="/Library/Application Support/Oracle/Java/Deployment/deployment.config"

# Properties File Location
propFile="/Library/Application Support/Oracle/Java/Deployment/deployment.properties"

# Folder Location
folderLoc="/Library/Application Support/Oracle/Java/Deployment/"

# Checks if Java Plugin is installed
if [ -e "$javaPlugin" ]; then

	/bin/echo "Java Plugin is installed..."

	# Checks if config file is present
	if [ ! -f "$configFile" ]; then

		/bin/echo "The deployment.config file does not yet exist. Will create..."

			# Create path
			/bin/mkdir -p "$folderLoc"

			# Create deployment.config file
			/usr/bin/touch "$configFile"

			/bin/echo "Created deployment.config file"

			# Change ownership on this new file
			/usr/sbin/chown root:wheel "$configFile"

			/bin/echo "Changed ownership on deployment.config file"

			# Write contents of this file
			/bin/echo deployment.system.config=file\://$propFile >> "$configFile"
			/bin/echo deployment.system.config.mandatory=false >> "$configFile"

			/bin/echo "Wrote content to deployment.config file."

	else

		/bin/echo "deployment.config file already exists. Removing and building new version..."

			# Delete existing version of the file
			/bin/rm -f "$configFile"

			/bin/echo "Deleted previous deployment.config file"

			# Create deployment.config file
			/usr/bin/touch "$configFile"

			/bin/echo "Created deployment.config file"

			# Change ownership on this new file
			/usr/sbin/chown root:wheel "$configFile"

			/bin/echo "Changed ownership on deployment.config file"

			# Write contents of this file
			/bin/echo deployment.system.config=file\://$propFile >> "$configFile"
			/bin/echo deployment.system.config.mandatory=false >> "$configFile"

			/bin/echo "Wrote content to deployment.config file."

	fi

	# Checks if properties file is present
	if [ ! -f "$propFile" ]; then

		/bin/echo "The deployment.properties file does not yet exist. Will create..."

			# Create path
			/bin/mkdir -p "$folderLoc"
			
			# Create deployment.properties file
			/usr/bin/touch "$propFile"

			/bin/echo "Created deployment.properties file"

			# Change ownership on this new file
			/usr/sbin/chown root:wheel "$propFile"

			/bin/echo "Changed ownership on deployment.properties file"

			# Write contents of this file
			/bin/echo '#deployment.properties' > "$propFile"
			/bin/echo deployment.security.validation.ocsp=false >> "$propFile"
			/bin/echo deployment.security.validation.ocsp.locked >> "$propFile"
			/bin/echo deployment.macosx.check.update=false >> "$propFile"
			/bin/echo deployment.macosx.check.update.locked >> "$propFile"
			/bin/echo deployment.expiration.check.enabled=false >> "$propFile"
			/bin/echo deployment.expiration.check.enabled.locked >> "$propFile"
			/bin/echo deployment.console.startup.mode=HIDE >> "$propFile"

			/bin/echo "Wrote content to deployment.properties file."

	else

		/bin/echo "deployment.properties file already exists. Removing and building new version..."

			# Delete existing version of the file
			/bin/rm -f "$propFile"

			# Create deployment.properties file
			/usr/bin/touch "$propFile"

			/bin/echo "Created deployment.properties file"

			# Change ownership on this new file
			/usr/sbin/chown root:wheel "$propFile"

			/bin/echo "Changed ownership on deployment.properties file"

			# Write contents of this file
			/bin/echo '#deployment.properties' > "$propFile"
			/bin/echo deployment.security.validation.ocsp=false >> "$propFile"
			/bin/echo deployment.security.validation.ocsp.locked >> "$propFile"
			/bin/echo deployment.macosx.check.update=false >> "$propFile"
			/bin/echo deployment.macosx.check.update.locked >> "$propFile"
			/bin/echo deployment.expiration.check.enabled=false >> "$propFile"
			/bin/echo deployment.expiration.check.enabled.locked >> "$propFile"
			/bin/echo deployment.console.startup.mode=HIDE >> "$propFile"

			/bin/echo "Wrote content to deployment.properties file."

	fi

	# Change the auto updater preference
	/usr/bin/defaults write /Library/Preferences/com.oracle.java.Java-Updater JavaAutoUpdateEnabled -bool false

	/bin/echo "Changed the auto updater preference file."
	/bin/echo "Java settings have been deployed. Exiting"

else

	echo "Error: Failure to find Java Plugin path. Either Java is not installed, or the path within the plugin has changed. Exiting"
	exit 1

fi
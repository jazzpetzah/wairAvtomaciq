package com.wearezeta.auto.common.calling;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.ComputeScopes;
import com.google.api.services.compute.model.AccessConfig;
import com.google.api.services.compute.model.AttachedDisk;
import com.google.api.services.compute.model.Disk;
import com.google.api.services.compute.model.Instance;
import com.google.api.services.compute.model.InstanceList;
import com.google.api.services.compute.model.Metadata;
import com.google.api.services.compute.model.Metadata.Items;
import com.google.api.services.compute.model.NetworkInterface;
import com.google.api.services.compute.model.Operation;
import com.wearezeta.auto.common.log.ZetaLogger;

public class GoogleComputeEngine {
	private static final Logger log = ZetaLogger
			.getLog(GoogleComputeEngine.class.getSimpleName());
	private static final String APPLICATION_NAME = "blender";
	private static final String ZONE = "europe-west1-c";
	private static final String PROJECT = "wire-app";

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();

	static {
		log.setLevel(Level.DEBUG);
	}

	private static GoogleCredential authorize() throws IOException,
			GeneralSecurityException {
		Set<String> scopes = new HashSet<String>();
		scopes.add(ComputeScopes.COMPUTE);
		return GoogleCredential.fromStream(
				GoogleComputeEngine.class
						.getResourceAsStream("/client_secrets.json"))
				.createScoped(scopes);
	}

	public static void createInstanceAndStartBlender(String instanceName,
			String username, String password) throws Exception {
		// Create compute engine object for listing instances
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		Compute compute = new Compute.Builder(httpTransport, JSON_FACTORY,
				authorize()).setApplicationName(APPLICATION_NAME).build();

		// Add startup-script, username and password as metadata into instance
		Metadata metadata = new Metadata();
		List<Items> items = new ArrayList<>();
		Items startUpScript = new Items();
		startUpScript.setKey("startup-script");
		startUpScript.setValue(new Scanner(GoogleComputeEngine.class
				.getResourceAsStream("/startblender.sh"), "UTF-8")
				.useDelimiter("\\A").next());
		items.add(startUpScript);
		items.add(new Items().setKey("username").setValue(username));
		items.add(new Items().setKey("password").setValue(password));
		metadata.setItems(items);
		createInstance(compute, PROJECT, instanceName, metadata);
	}

	/**
	 * Create machine instances.
	 * 
	 * @param instanceName
	 * @throws Exception
	 */
	private static void createInstance(Compute compute, String projectId,
			String instanceName, Metadata metadata) throws Exception {
		// gcloud compute --project ${PROJECT} instances create ${INSTANCE}
		// --zone ${ZONE} --machine-type "f1-micro" --network "default"
		// --metadata "username=${USERNAME}" "password=${PASSWORD}"
		// --maintenance-policy "MIGRATE" --scopes
		// "https://www.googleapis.com/auth/devstorage.read_only"
		// "https://www.googleapis.com/auth/logging.write"
		// --disk "name=${INSTANCE}" "device-name=${INSTANCE}" "mode=rw"
		// "boot=yes" "auto-delete=yes"
		// --metadata-from-file startup-script=startblender.sh
		Instance instance = new Instance();
		instance.setName(instanceName);

		// set metadata
		instance.setMetadata(metadata);

		// create disk
		// gcloud compute --project ${PROJECT} disks create ${INSTANCE} --zone
		// ${ZONE} --source-snapshot ${SNAPSHOT} --type "pd-standard"
		Disk disk = new Disk();
		disk.setName(instanceName);
		disk.setSourceSnapshot("https://www.googleapis.com/compute/v1/projects/"
				+ PROJECT + "/global/snapshots/blender-boot");
		disk.setType("https://www.googleapis.com/compute/v1/projects/"
				+ PROJECT + "/zones/" + ZONE + "/diskTypes/pd-standard");
		disk.setZone("https://www.googleapis.com/compute/v1/projects/"
				+ PROJECT + "/zones/" + ZONE);
		log.debug("Create disk in Google Compute Engine");
		Operation diskCreation = compute.disks().insert(PROJECT, ZONE, disk)
				.execute();
		log.debug(diskCreation.toPrettyString());

		// attach disk to instance
		AttachedDisk attachedDisk = new AttachedDisk();
		attachedDisk
				.setSource("https://www.googleapis.com/compute/v1/projects/"
						+ PROJECT + "/zones/" + ZONE + "/disks/" + instanceName);
		attachedDisk.setBoot(true);
		attachedDisk.setType("PERSISTENT");
		attachedDisk.setMode("READ_WRITE");
		attachedDisk.setAutoDelete(true);
		instance.setDisks(Collections.singletonList(attachedDisk));

		// Select a machine type.
		String machine = "https://www.googleapis.com/compute/v1/projects/"
				+ PROJECT + "/zones/" + ZONE + "/machineTypes/f1-micro";
		instance.setMachineType(machine);

		// Use the default network. Could select here if needed.
		AccessConfig accessConfig = new AccessConfig();
		accessConfig.setName("External NAT");
		accessConfig.setType("ONE_TO_ONE_NAT");
		NetworkInterface networkInterface = new NetworkInterface();
		networkInterface.setAccessConfigs(Collections
				.singletonList(accessConfig));
		networkInterface
				.setNetwork("https://www.googleapis.com/compute/v1/projects/"
						+ PROJECT + "/global/networks/default");
		instance.setNetworkInterfaces(Collections
				.singletonList(networkInterface));

		// Select a zone.
		instance.setZone("https://www.googleapis.com/compute/v1/projects/"
				+ PROJECT + "/zones/" + ZONE);
		log.debug("Create new instance of blender in Google Compute Engine");
		Compute.Instances.Insert ins = compute.instances().insert(PROJECT,
				ZONE, instance);

		// Finally, let's run it.
		log.debug(instance.toPrettyString());
		waitForDisk(compute, instanceName, ZONE, PROJECT);
		Operation op = ins.execute();
		log.debug(op.toPrettyString());
		waitForInstance(compute, instanceName, ZONE, PROJECT);
	}

	private static void waitForDisk(Compute compute, String diskName,
			String zone, String projectId) throws Exception {
		long timeout = System.currentTimeMillis() + 2 * 60000L;
		while (true) {
			try {
				Disk disk = compute.disks().get(projectId, zone, diskName)
						.execute();
				if ("READY".equalsIgnoreCase(disk.getStatus())) {
					log.info("Disk is ready.");
					break;
				} else {
					log.info("Disk not ready : " + disk.getStatus());
				}
			} catch (Throwable t) {
				log.info("Disk not ready:" + t.getMessage());
			}
			if (System.currentTimeMillis() < timeout) {
				log.info("Disk is not ready. Sleeping for ten seconds ...");
				Thread.sleep(10000);
			} else {
				log.debug("Timed out. Giving up.");
				throw new Exception(
						"Could not create disk on GCE for calling automation.");
			}
		}
	}

	private static void waitForInstance(Compute compute, String instanceName,
			String zone, String projectId) throws Exception {
		long timeout = System.currentTimeMillis() + 2 * 60000L;
		while (true) {
			try {
				Instance instance = compute.instances()
						.get(projectId, zone, instanceName).execute();
				if ("RUNNING".equalsIgnoreCase(instance.getStatus())) {
					log.info("Instance is ready.");
					break;
				} else {
					log.info("Instance not ready : " + instance.getStatus());
				}
			} catch (Throwable t) {
				log.info("Instance not ready:" + t.getMessage());
			}
			if (System.currentTimeMillis() < timeout) {
				log.info("Instance is not ready. Sleeping for ten seconds ...");
				Thread.sleep(10000);
			} else {
				log.debug("Timed out. Giving up.");
				throw new Exception(
						"Could not create Instance on GCE for calling automation.");
			}
		}
	}

	private static void deleteInstance(Compute compute, String instance)
			throws IOException {
		// no need to delete disk, because we set it to auto-delete
		compute.instances().delete(PROJECT, ZONE, instance).execute();
	}

	public static void deleteAllInstancesWhereNameContains(String pattern) throws IOException, GeneralSecurityException {
		log.info("Delete blender instances on Google Compute Engine...");
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		Compute compute = new Compute.Builder(httpTransport, JSON_FACTORY,
				authorize()).setApplicationName(APPLICATION_NAME).build();
		InstanceList instances = compute.instances().list(PROJECT, ZONE).execute();
		for (Instance instance : instances.getItems()) {
			if(instance.getName().contains(pattern)) {
				log.info("Delete blender instance: " + instance.getName());
				deleteInstance(compute, instance.getName());	
			}
		}
		log.info("All blender instances deleted.");
	}
}

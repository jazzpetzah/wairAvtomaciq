package com.wearezeta.auto.common.calling;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.ComputeScopes;
import com.google.api.services.compute.model.AccessConfig;
import com.google.api.services.compute.model.AttachedDisk;
import com.google.api.services.compute.model.Disk;
import com.google.api.services.compute.model.Instance;
import com.google.api.services.compute.model.Metadata;
import com.google.api.services.compute.model.Metadata.Items;
import com.google.api.services.compute.model.NetworkInterface;
import com.google.api.services.compute.model.Operation;
import com.wearezeta.auto.common.log.ZetaLogger;

public class GoogleComputeEngine {
	private static final Logger log = ZetaLogger
			.getLog(GoogleComputeEngine.class.getSimpleName());

	static {
		log.setLevel(Level.DEBUG);
	}

	final static String APPLICATION_NAME = "blender";
	final static String ZONE = "europe-west1-c";
	final static String PROJECT = "wire-app";

	/** Directory to store user credentials. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(
			System.getProperty("user.home"), ".store/compute_engine_sample");

	/**
	 * Global instance of the {@link DataStoreFactory}. The best practice is to
	 * make it a single globally shared instance across your application.
	 */
	private static FileDataStoreFactory dataStoreFactory;

	/** Global instance of the HTTP transport. */
	private static HttpTransport httpTransport;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();

	/** OAuth 2.0 scopes */
	private static final List<String> SCOPES = Arrays
			.asList(ComputeScopes.COMPUTE_READONLY);

	public static void createInstanceAndStartBlender(String username,
			String password) throws Exception {
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			// Authorization
			Set<String> scopes = new HashSet<String>();
			scopes.add(ComputeScopes.COMPUTE);
			GoogleCredential credential = GoogleCredential.fromStream(
					GoogleComputeEngine.class
							.getResourceAsStream("/client_secrets.json"))
					.createScoped(scopes);
			// Create compute engine object for listing instances
			Compute compute = new Compute.Builder(httpTransport, JSON_FACTORY,
					credential).setApplicationName(APPLICATION_NAME).build();
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
			createInstance(compute, PROJECT, metadata);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * Create machine instances.
	 * 
	 * @throws InterruptedException
	 */
	private static void createInstance(Compute compute, String projectId,
			Metadata metadata) throws IOException, InterruptedException {
		// gcloud compute --project ${PROJECT} instances create ${INSTANCE}
		// --zone ${ZONE} --machine-type "f1-micro" --network "default"
		// --metadata "username=${USERNAME}" "password=${PASSWORD}"
		// --maintenance-policy "MIGRATE" --scopes
		// "https://www.googleapis.com/auth/devstorage.read_only"
		// "https://www.googleapis.com/auth/logging.write"
		// --disk "name=${INSTANCE}" "device-name=${INSTANCE}" "mode=rw"
		// "boot=yes" "auto-delete=yes"
		// --metadata-from-file startup-script=startblender.sh
		String instanceName = "blender" + System.currentTimeMillis();

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
		networkInterface.setAccessConfigs(Collections.singletonList(accessConfig));
		networkInterface.setNetwork("https://www.googleapis.com/compute/v1/projects/" + PROJECT + "/global/networks/default");
		instance.setNetworkInterfaces(Collections.singletonList(networkInterface));

		// Select a zone.
		instance.setZone("https://www.googleapis.com/compute/v1/projects/"
				+ PROJECT + "/zones/" + ZONE);
		log.debug("Create new instance of blender in Google Compute Engine");
		Compute.Instances.Insert ins = compute.instances().insert(PROJECT,
				ZONE, instance);

		// Finally, let's run it.
		log.debug(instance.toPrettyString());
		if (waitForDisk(compute, instanceName, ZONE, PROJECT)) {
			Operation op = ins.execute();
			log.debug(op.toPrettyString());
		}
		waitForInstance(compute, instanceName, ZONE, PROJECT);
	}

	private static boolean waitForDisk(Compute compute, String diskName,
			String zone, String projectId) throws InterruptedException,
			IOException {
		long timeout = System.currentTimeMillis() + 2 * 60000L;
		boolean created = false;
		while (!created && System.currentTimeMillis() < timeout) {
			boolean diskCreated = false;
			try {
				Disk disk = compute.disks().get(projectId, zone, diskName)
						.execute();
				if ("READY".equalsIgnoreCase(disk.getStatus())) {
					diskCreated = true;
				} else {
					log.info("Disk status : " + disk.getStatus());
					diskCreated = false;
				}
			} catch (Throwable t) {
				log.log(Level.INFO, "Error when fetching disk info", t);
				diskCreated = false;
			}
			if (diskCreated) {
				created = true;
				log.info("Disk is ready.");
			} else {
				log.info("Disk is not ready. Sleeping for ten seconds ...");
				Thread.sleep(10000);
			}
		}
		if (!created) {
			log.debug("Timed out. Giving up.");
			return false;
		} else {
			return true;
		}
	}

	private static boolean waitForInstance(Compute compute,
			String instanceName, String zone, String projectId)
			throws InterruptedException {
		long timeout = System.currentTimeMillis() + 2 * 60000L;
		boolean created = false;
		while (!created && System.currentTimeMillis() < timeout) {
			boolean instanceCreated = false;
			try {
				Instance instance = compute.instances()
						.get(projectId, zone, instanceName).execute();
				if ("RUNNING".equalsIgnoreCase(instance.getStatus())) {
					instanceCreated = true;
				} else {
					log.info("Instance status : " + instance.getStatus());
					instanceCreated = false;
				}
			} catch (Throwable t) {
				log.log(Level.INFO, "Error when fetching instance info", t);
				instanceCreated = false;
			}
			if (instanceCreated) {
				created = true;
				log.info("Instance is ready.");
			} else {
				log.info("Instance is not ready. Sleeping for ten seconds ...");
				Thread.sleep(10000);
			}
		}
		if (!created) {
			log.debug("Timed out. Giving up.");
			return false;
		} else {
			return true;
		}
	}

	public void deleteInstance(Compute compute, String instance, String zone,
			String projectId) throws IOException {
		compute.instances().delete(projectId, zone, instance).execute();
	}

	public void deleteDisk(Compute compute, String disk, String zone,
			String projectId) throws IOException {
		compute.disks().delete(projectId, zone, disk).execute();
	}
}

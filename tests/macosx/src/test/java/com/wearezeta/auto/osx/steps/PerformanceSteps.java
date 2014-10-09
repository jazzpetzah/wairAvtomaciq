package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;
import java.time.LocalDateTime;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.osx.pages.LoginPage;
import com.wearezeta.auto.osx.pages.MainMenuPage;
import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.en.When;

public class PerformanceSteps {
	
	private static final int MIN_WAIT_VALUE_IN_MIN = 1;
	private static final int MAX_WAIT_VALUE_IN_MIN = 5;
	private static final int BACK_END_MESSAGE_COUNT = 5;
	
	@When("Start testing cycle")
	 public void StartTestingCycle() throws Exception{	
		
		ScrollAndReadConversationForTimes(5);
		MinimizeZClient();
		SetRandomSleepInterval();
		RestoreZClient();
	 }
	
	@When("^I (.*) start testing cycle for (\\d+) minutes$")
	public void WhenIStartTestingCycleForMinutes(String user, int time) throws Throwable {
		LocalDateTime startDateTime = LocalDateTime.now();
		long diffInMinutes = 0;
	
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		while (diffInMinutes < time) {
			
			CommonUtils.sendRandomMessagesToUser(BACK_END_MESSAGE_COUNT);
			CommonUtils.sendDefaultImageToUser((int) Math
			.floor(BACK_END_MESSAGE_COUNT / 5));
		
			ScrollAndReadConversationForTimes(5);
			MinimizeZClient();
			SetRandomSleepInterval();
			RestoreZClient();
			
			LocalDateTime currentDateTime = LocalDateTime.now();
			diffInMinutes = java.time.Duration.between(startDateTime,
					currentDateTime).toMinutes();
		}
	}
	
	@When("Scroll and read conversations for (.*) times")
	 public void ScrollAndReadConversationForTimes(int n) throws MalformedURLException, IOException{	
		for(int i = 1; i <=n; ++i){
			System.out.print("Loop scroll and read");
			//ContactListPageSteps clSteps = new ContactListPageSteps();
			//String contact = CommonSteps.senderPages.getConversationInfoPage().getCurrentConversationName();
			//clSteps.GivenIOpenConversationWith(contact);
		}
	 }
	
	@When("Minimize ZClient")
	 public void MinimizeZClient() throws Exception{
		CommonSteps.senderPages.getContactListPage().waitUntilMainWindowAppears();
		CommonSteps.senderPages.getContactListPage().minimizeZClient();
	 }
	
	@When("Set random sleep interval")
	 public void SetRandomSleepInterval() throws InterruptedException{	
		Random random = new Random();
		int sleepTimer = ((random.nextInt( MAX_WAIT_VALUE_IN_MIN) + MIN_WAIT_VALUE_IN_MIN) * 60 * 1000);
		System.out.print(sleepTimer);
		//Thread.sleep(sleepTimer);
	 }
	
	@When("Restore ZClient")
	 public void RestoreZClient() throws Exception{
		//CommonSteps.senderPages.getContactListPage().restoreZClient();
	 }

}

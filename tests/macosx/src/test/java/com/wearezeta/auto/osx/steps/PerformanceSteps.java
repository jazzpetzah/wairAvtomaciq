package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.osx.pages.LoginPage;
import com.wearezeta.auto.osx.pages.MainMenuPage;
import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.en.When;

public class PerformanceSteps {
	
	@When("Start testing cycle")
	 public void StartTestingCycle() throws Exception{	
		ScrollAndReadConversationForTimes(5);
		MinimizeZClient();
		SetRandomSleepInterval();
		RestoreZClient();
	 }
	
	@When("Scroll and read conversations for (.*) times")
	 public void ScrollAndReadConversationForTimes(int n) throws MalformedURLException, IOException{	
		for(int i = 1; i <=n; ++i){
			//System.out.print("Loop scroll and read");
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
		int[] sleepArray = new int[] {60000, 120000, 180000, 240000, 300000};
		int sleepTimer = sleepArray[new Random().nextInt(sleepArray.length)];
		//System.out.print(sleepTimer);
		Thread.sleep(sleepTimer);
	 }
	
	@When("Restore ZClient")
	 public void RestoreZClient() throws Exception{
		
	 }

}

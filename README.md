#zautomation


Incredible automation for zeta

##Source
All source code and the test architecture can you find in Github:

* [wearezeta/zautomation](https://github.com/wearezeta/zautomation) 


##Preperations
You have to have node.js and homebrew installed to run Appium properly.
Install Homebrew:

* [homebrew](http://brew.sh)
run this command :  ruby -e "$(curl -fsSL https://raw.github.com/Homebrew/homebrew/go/install)"

afterwards install node.js with command:
brew install node


##Appium
Download Appium from here:

* [Appium](https://bitbucket.org/appium/appium.app/downloads/) 
* UPDATE [Appium newst version](https://github.com/appium/appium/releases/tag/v1.3.3)

When Appium is opened, configure it by clicking in the Apple symbole. If you want to run the test on the simulator add the AppPath to ZClient.app (this needs to be a simulator build).
Set simulator device as iPhone 6
Set Platform version manually to 8.1 

Press 'Launch' to connect Appium to the simulator to run tests on it.

You can run the tests as well on the Device, therefore provide AppPath to ipa file of the debug-iphone build and enable BundleID with com.wearezeta.zclient-alpha and your UDID of your device and change platform version to the version that is running on your device.

#####Run Appium for Mac Wire automation
Open in the zautomation folder the AppiumForMac Folder and open the project file with xcode. Run the code and leave it running. A Appium icon with the osx X should appear in your dock.

##Eclipse
Download Eclipse.
Configure Eclipse for testing by adding Maven and Cucumber Add-Ons.
Maven: go to Help -> Install New Software, search for m2e
For better reading of Cucumber files, Cucumber Plug-In: go to Help -> Install New Software, search for cucumber

All needed project structure, base classes and utilities are already ready to use. Just open the zautomation/tests folder in Eclipse (File -> Import -> existing Maven Project -> select the /tests folder). There the test architecture for every platform is in.

You have to configure settings.xml file: An example of this file is in zautomation/tests/settings.xml Create ~/.m2 folder and create settings.xml file. Change root and appPath (same like in Appium) and all other needed values of this file according to your local values.
For Mac the wire app just needs to be placed into the Applicationsfolder and in settings.xml and in pom.xml (inside eclipse) <appPath>tag needs to have the name of the App. (eg. <apppath>WireBuild1880<apppath>).

Configure Maven builds:
In Eclipse create new Maven Debug configuration (Run -> Debug Configurations…).Pictures of the configurations are found in the Wiki (Setting up Eclipse project https://github.com/wearezeta/zautomation/wiki/Setting-up-Eclipse-project ).
 For the Base directory – select a project you want to debug (iOS, android, macosx). Profile – according to the selected project. Profile name should be the same as set in settings.xml file.
 Create another debug configuration, this time for Remote Java Application.
 
Now, to run your tests, do the following: run your Maven configuration and wait for condole output 
"Listening for transport dt_socket at address: 5005"
Then run your Remote Java Configuration.



##Test writing architecture
####Cucumber feature file
Write clear and understandable Cucumber parameterized feature file, like below (Conversations.feature - all scenarios that are conversation related are in this file, put a new scenario outline to add e.g. hello and hey a contact) :

Feature: Send Message
  
  @torun
  Scenario Outline: Send Message to user from my contact list
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message and send it
    Then I see my message in the dialog

    Examples: 
    
    |	Login						|	Password	|	Name			|	Contact	|
    |	piotr.iazadji@wearezeta.com	|	asdfer123	|	Piotr Iazadji	|	Maxim	|


@torun - tag: put that over the scenario in your cucumber.feature file to just run this test on the DevRun.java file while your still developing.
TestRun.java runs all test cases.

####Cucumber steps test file
Create a cucumber test in java. Usually called “steps” file (see DialogPageSteps.java).
In the Send Message Scenario something like this would be possible:

@When("^I type the message and send it$")
	public void I_type_the_message() throws Throwable {
		PagesCollection.dialogPage.waitForTextMessageInputVisible();
	    message = CommonUtils.generateGUID();
	    PagesCollection.dialogPage.typeMessage(message + "\n");
	}

	@Then("^I see my message in the dialog$")
	public void I_see_my_message_in_the_dialog() throws Throwable {
	    String dialogLastMessage = PagesCollection.dialogPage.getLastMessageFromDialog();
	    Assert.assertEquals(message, dialogLastMessage);
	}
####Page Objects
All WebDriver related logic should use PageObject pattern. More on it here:

* [Page Pattern] (https://code.google.com/p/selenium/wiki/PageFactory) 
Most of the work is already done, you just need to follow the example and remember the following rule: all pages for iOS testing should extend IOSPage class, all pages for Android testing should extend AndroidPage class.
Create PageObject(s) which will be used by “steps” file. Basically – meat of the test, which actually uses Appium. As a reference take a look at LoginPage.java
#zautomation


Incredible automation for zeta


##Preperations
You have to have node.js and homebrew installed to run Appium properly.
Install Homebrew:

* [homebrew](http://brew.sh)
run this command :  ruby -e "$(curl -fsSL https://raw.github.com/Homebrew/homebrew/go/install)"

afterwards install node.js with command:
brew install node


##Appium
Download Appium from here:

* [Appium for Mac](https://bitbucket.org/appium/appium.app/downloads/) 

When Appium is opened, configure it by clicking in the Apple symbole. If you want to run the test on the simulator add the AppPath to ZClient.app like from here or where ever the simulator build is: (~/Library/Developer/Xcode/DerivedData/ZClient-fxsmvhgghphimdebxcatdyrafqyx/Build/Products/Debug-iphonesimulator/ZClient.app)

Press 'Launch' to connect Appium to the simulator to run tests on it.

You can run the tests as well on the Device, therefore leave AppPath empty and enable BundleID with com.wearezeta.zclient-alpha and your UDID of your device.

##Source
All source code and the test architecture can you find in Github:

* [wearezeta/zautomation](https://github.com/wearezeta/zautomation) 

All needed project structure, base classes and utilities are already ready to use. Just open the /test folder in Eclipse (File -> Import -> existing Maven Project -> select the test folder). There the test architecture for every platform is in.
Follow the existing tests to write your own test cases.
You just have to configure one more file: Configuration.cnf (platform-you-want-to-work-on/src/main/resources)like this:

e.g. URL to local and like in Appium Path to your simulator build .app file
Url=http://127.0.0.1:4723/wd/hub
appPath=/Users/Library/Developer/Xcode/DerivedData/ZClient-fxsmvhgghphimdebxcatdyrafqyx/Build/Products/Debug-iphonesimulator/ZClient.app

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
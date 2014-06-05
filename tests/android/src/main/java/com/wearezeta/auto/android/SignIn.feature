Feature: Sign In
@torun
Scenario Outline: Sign in to ZClient
	Given I see sign in screen
	When I press Sign in button
	And I have entered login <Login>
	And I have entered password <Password>
	And I press Sign in button 
	Then Contact list appears with my name <Name>
	
Examples:
    |  Login                             | Password     | Name              | 
    |  sergeii.khyzhniak@wearezeta.com   | 123456       | Sergey Hizhnyak   |
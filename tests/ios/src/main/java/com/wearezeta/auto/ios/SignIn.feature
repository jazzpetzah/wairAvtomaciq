Feature: Sign In

Scenario: Sing in to ZClient
	Given I see sign in screen
	When I press Sign in button
	And I have entered login "sergeii.khyzhniak@wearezeta.com"
	And I have entered password "123456"
	And I press Sign in button 
	Then Contact list appears with "Sergey Hizhnyak" element
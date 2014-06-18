Feature: Sign In

Scenario Outline: Sign in ZClient
	Given I am signed out from ZClient
	And I see Sign In screen
	When I have entered login <Login>
	And I have entered password <Password>
	And I press Sign In button 
	Then Contact list appears with my name <Name>
	
Examples:
    |  Login							| Password	| Name		|
    |  smoketester+aqa31@wearezeta.com	| aqa123456	| aqa31		|

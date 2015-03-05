Feature: Registration

@torun
Scenario Outline: Registration
    Given I see invitation page
    Given I enter invitation code
    Given I see authorization page
	Given I click create account button 
	When I input name <Name>
	And I input email <Email>
	And I input password <Password>
	And I submit registration
	Then I see email <Email> verification page
	When I verify registration email
	
    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |
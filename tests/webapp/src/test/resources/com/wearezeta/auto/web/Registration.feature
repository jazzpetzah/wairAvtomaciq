Feature: Registration

  @staging
  Scenario Outline: Registration
    Given I see invitation page
    Given I enter invitation code
    Given I switch to Registration page
    When I enter user name <Name> on Registration page 
    And I enter user email <Email> on Registration page 
    And I enter user password <Password> on Registration page 
    And I submit registration form
    Then I see email <Email> on Verification page
    When I verify registration email
	
    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |
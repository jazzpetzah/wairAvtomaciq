Feature: Registration

  @staging
  Scenario Outline: Registration
    Given I see invitation page
    Given I enter invitation code
    Given I switch to Registration page
    When I input user name <Name>
    And I input user email <Email>
    And I input user password <Password>
    And I submit registration
    Then I see email <Email> verification page
    When I verify registration email
	
    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |
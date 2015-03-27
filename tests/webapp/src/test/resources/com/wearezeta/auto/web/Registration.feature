Feature: Registration

  @staging @id1936
  Scenario Outline: Verify new user can be registered
    Given I see invitation page
    Given I enter invitation code
    Given I switch to Registration page
    When I enter user name <Name> on Registration page 
    And I enter user email <Email> on Registration page 
    And I enter user password <Password> on Registration page 
    And I submit registration form
    Then I see email <Email> on Verification page
    When I activate user by URL
    And User <Name> is Me
    # This has to be done automatically at some time
    And I Sign in using login <Email> and password <Password>
    Then I see my name <Name> in Contact list
    When I open self profile
    Then I see user name on self profile page <Name>
    Then I see user email on self profile page <Email>
	
    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |
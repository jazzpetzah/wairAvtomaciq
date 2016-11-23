Feature: Usernames

  @C343171 @usernames @staging
  Scenario Outline: Verify existing user has a take over screen with offered username
    Given There are 2 users where <NameAlias> is me without unique username
    Given User <NameAlias> changes name to <Name>
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I see take over screen
    And I see name <NameAlias> on take over screen
    And I see unique username starts with <NameAlias> on take over screen
    And I see ChooseYourOwn button on take over screen
    And I see TakeThisOne button on take over screen
    When I click TakeThisOne button on take over screen
    Then I see conversation with <Contact> is selected in conversations list
    When I open preferences by clicking the gear button
    Then I see unique username starts with <NameAlias> in account preferences

    Examples:
      | Email      | Password      | NameAlias | Name         | Contact   |
      | user1Email | user1Password | user1Name | Jack Johnson | user2Name |

  @C343172 @usernames @staging @useSpecialEmail
  Scenario Outline: Verify new user has a take over screen with offered username
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And I accept the Terms of Use
    And I start activation email monitoring
    And I submit registration form
    When I activate user by URL
    And I see take over screen
    And I see name <NameAlias> on take over screen
    And I see unique username starts with <NameAlias> on take over screen
    And I see ChooseYourOwn button on take over screen
    And I see TakeThisOne button on take over screen
    When I click TakeThisOne button on take over screen
    When I open preferences by clicking the gear button
    Then I see unique username starts with <NameAlias> in account preferences

    Examples:
      | Email      | Password      | NameAlias | Name             |
      | user1Email | user1Password | user1Name | Marie Antoinette |

  @C343174 @usernames @staging
  Scenario Outline: Verify take over screen doesn't go away on reload
    Given There are 2 users where <NameAlias> is me
    Given User <NameAlias> changes name to <Name>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I see take over screen
    And I see name <NameAlias> on take over screen
    And I see unique username starts with <NameAlias> on take over screen
    When I refresh page
    Then I see take over screen
    And I see name <NameAlias> on take over screen
    And I see unique username starts with <NameAlias> on take over screen

    Examples:
      | Email      | Password      | NameAlias | Name               |
      | user1Email | user1Password | user1Name | Hans-Peter Baxxter |

  @C343175 @usernames @staging @useSpecialEmail
  Scenario Outline: Verify Settings are opened on choosing generating your own username after registration
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And I accept the Terms of Use
    And I start activation email monitoring
    And I submit registration form
    When I activate user by URL
    And I see take over screen
    And I see name <NameAlias> on take over screen
    And I see unique username starts with <NameAlias> on take over screen
    When I click ChooseYourOwn button on take over screen
    Then I see unique username starts with <NameAlias> in account preferences

    Examples:
      | Email      | Password      | NameAlias | Name         |
      | user1Email | user1Password | user1Name | Jack Johnson |

  @C343176 @usernames @staging
  Scenario Outline: Verify Settings are opened on choosing generating your own username for existing user
    Given There are 2 users where <NameAlias> is me without unique username
    Given User <NameAlias> changes name to <Name>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I see take over screen
    And I see name <NameAlias> on take over screen
    And I see unique username starts with <NameAlias> on take over screen
    When I click ChooseYourOwn button on take over screen
    Then I see unique username starts with <NameAlias> in account preferences

    Examples:
      | Email      | Password      | NameAlias | Name         |
      | user1Email | user1Password | user1Name | Jack Johnson |

  @C343177 @usernames @staging
  Scenario Outline: Verifying impossibility of setting incorrect username
    Given There are 2 users where <NameAlias> is me without unique username
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    And I see take over screen
    And I see name <NameAlias> on take over screen
    When I click ChooseYourOwn button on take over screen
    Then I see unique username starts with username in account preferences
    When I change unique username to <IncorrectName>
    Then I see error message in account preferences

    Examples:
      | Email      | Password      | NameAlias | IncorrectName          |
      | user1Email | user1Password | user1Name |                        |
      | user1Email | user1Password | user1Name | a                      |
      | user1Email | user1Password | user1Name | 1234567890123456789012 |
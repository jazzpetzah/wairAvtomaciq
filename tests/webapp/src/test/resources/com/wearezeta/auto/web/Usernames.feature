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
    And I see unique username starts with <Username> on take over screen
    And I see ChooseYourOwn button on take over screen
    And I see TakeThisOne button on take over screen
    When I click TakeThisOne button on take over screen
    Then I see conversation with <Contact> is selected in conversations list
    When I open preferences by clicking the gear button
    Then I see unique username starts with <Username> in account preferences

    Examples:
      | Email      | Password      | NameAlias | Name         | Contact   | Username    |
      | user1Email | user1Password | user1Name | Jack Johnson | user2Name | jackjohnson |

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
    And I see unique username starts with <Username> on take over screen
    And I see ChooseYourOwn button on take over screen
    And I see TakeThisOne button on take over screen
    When I click TakeThisOne button on take over screen
    When I open preferences by clicking the gear button
    Then I see unique username starts with <Username> in account preferences

    Examples:
      | Email      | Password      | NameAlias | Name             | Username        |
      | user1Email | user1Password | user1Name | Marie Antoinette | marieantoinette |

  @C343174 @usernames @staging
  Scenario Outline: Verify take over screen doesn't go away on reload
    Given There are 2 users where <NameAlias> is me without unique username
    Given User <NameAlias> changes name to <Name>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I see take over screen
    And I see name <NameAlias> on take over screen
    And I see unique username starts with <Username> on take over screen
    When I refresh page
    Then I see take over screen
    And I see name <NameAlias> on take over screen
    And I see unique username starts with <Username> on take over screen

    Examples:
      | Email      | Password      | NameAlias | Name               | Username         |
      | user1Email | user1Password | user1Name | Hans-Peter Baxxter | hanspeterbaxxter |

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
    And I see unique username starts with <Username> on take over screen
    When I click ChooseYourOwn button on take over screen
    Then I see unique username starts with <Username> in account preferences

    Examples:
      | Email      | Password      | NameAlias | Name         | Username    |
      | user1Email | user1Password | user1Name | Jack Johnson | jackjohnson |

  @C343176 @usernames @staging
  Scenario Outline: Verify Settings are opened on choosing generating your own username for existing user
    Given There are 2 users where <NameAlias> is me without unique username
    Given User <NameAlias> changes name to <Name>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I see take over screen
    And I see name <NameAlias> on take over screen
    And I see unique username starts with <Username> on take over screen
    When I click ChooseYourOwn button on take over screen
    Then I see unique username starts with <Username> in account preferences

    Examples:
      | Email      | Password      | NameAlias | Name         | Username    |
      | user1Email | user1Password | user1Name | Jack Johnson | jackjohnson |

  @C343177 @usernames @staging
  Scenario Outline: Verifying impossibility of setting incorrect username
    Given There are 2 users where <NameAlias> is me without unique username
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    And I see take over screen
    And I see name <NameAlias> on take over screen
    When I click ChooseYourOwn button on take over screen
    Then I see unique username starts with <NameAlias> in account preferences
    When I change unique username to <IncorrectName>
    Then I see error message for unique username saying <Error>

    Examples:
      | Email      | Password      | NameAlias | IncorrectName          | Error            |
      | user1Email | user1Password | user1Name |                        | Invalid username |
      | user1Email | user1Password | user1Name | a                      | Invalid username |
      | user1Email | user1Password | user1Name | 1234567890123456789012 | Invalid username |

  @C352077 @usernames @staging
  Scenario Outline: Verify autogeneration of a username for a user (different scenarios)
      Given There are 2 users where <NameAlias> is me without unique username
      Given User <NameAlias> changes name to <Name>
      Given I switch to Sign In page
      When I Sign in using login <Email> and password <Password>
      And I am signed in properly
      Then I see take over screen
      And I see name <NameAlias> on take over screen
      And I see unique username starts with <Username> on take over screen
      When I click TakeThisOne button on take over screen
      When I open preferences by clicking the gear button
      Then I see unique username starts with <Username> in account preferences

    Examples:
      | Email      | Password      | NameAlias | Name         | Username      |
      | user1Email | user1Password | user1Name | Jack         | jack          |
      | user1Email | user1Password | user1Name | Jack.Wireson | jackwireson   |
      | user1Email | user1Password | user1Name | Æéÿüíøšłźçñ  | aeeyueioslzcn |
      | user1Email | user1Password | user1Name | Даша         | dasha         |
      | user1Email | user1Password | user1Name |   داريا      | darya         |
      | user1Email | user1Password | user1Name | 明麗         | mengli        |

  @C352080 @staging @useSpecialEmail
  Scenario Outline: Verify deleting an account release a username
    Given There are 2 users where <Name2> is me without unique username
    And User <Name> is me
    Given I switch to Sign In page
    Given I remember current page
    Given I Sign in using login <Email> and password <Password>
    And I see take over screen
    And I see name <Name> on take over screen
    When I click ChooseYourOwn button on take over screen
    When I change unique username to <UserName>
    And I click delete account button on settings page
    And I click send button to delete account
    And I delete account of user <Name> via email
    And I navigate to previously remembered page
    And I see Sign In page
    When I enter email "<Email>"
    And I enter password "<Password>"
    And I press Sign In button
    Then the sign in error message reads <Error>
    When I Sign in using login <Email2> and password <Password>
    And I am signed in properly
    And I see take over screen
    And I see name <Name2> on take over screen
    When I click ChooseYourOwn button on take over screen
    Then I see unique username starts with <Name2> in account preferences
    When I change unique username to <UserName>
    Then I see unique username starts with <UserName> in account preferences

    Examples:
      | Email      | Password      | Name      | Error                                     | UserName      | Name2     | Email2     |
      | user1Email | user1Password | user1Name | Please verify your details and try again. | torelease1086 | user2Name | user2Email |
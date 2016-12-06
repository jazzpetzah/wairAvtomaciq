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
  Scenario Outline: Verifying impossibility to set username with less than 2 characters
    Given There are 2 users where <NameAlias> is me without unique username
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    And I see take over screen
    And I see name <NameAlias> on take over screen
    When I click ChooseYourOwn button on take over screen
    Then I see unique username starts with <NameAlias> in account preferences
    When I type <IncorrectName> into unique username field
    Then I see hint message for unique username saying <Hint>
    When I change unique username to <IncorrectName>
    Then I see hint message for unique username saying <Hint>
    When I refresh page
    And I am signed in properly
    And I see take over screen
    And I see name <NameAlias> on take over screen

    Examples:
      | Email      | Password      | NameAlias | IncorrectName | Hint                                        |
      | user1Email | user1Password | user1Name |               | At least 2 characters. a—z, 0—9 and _ only. |
      | user1Email | user1Password | user1Name | a             | At least 2 characters. a—z, 0—9 and _ only. |

  @C352242 @usernames @staging
  Scenario Outline: Verify username is unique
    Given There are 2 users where <Name> is me without unique username
    Given User <SecondUser> changes unique username to <SecondUser>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    And I see take over screen
    And I see name <Name> on take over screen
    When I click ChooseYourOwn button on take over screen
    Then I see unique username starts with <Name> in account preferences
    When I type <SecondUser> into unique username field
    Then I see error message for unique username saying <Error>
    When I change unique username to <SecondUser>
    Then I see error message for unique username saying <Error>
    When I refresh page
    And I am signed in properly
    And I see take over screen
    And I see name <Name> on take over screen

    Examples:
      | Email      | Password      | Name      | SecondUser | Error         |
      | user1Email | user1Password | user1Name | user2Name  | Already taken |

  @C352077 @usernames @staging
  Scenario Outline: Verify autogeneration of a username for a user (different scenarios)
      Given There are 2 users where <NameAlias> is me without unique username
      Given User <NameAlias> changes name to <Name>
      Given I switch to Sign In page
      When I Sign in using login <Email> and password <Password>
      And I am signed in properly
      And I see take over screen
      And I see name <NameAlias> on take over screen
      When I click TakeThisOne button on take over screen
      When I open preferences by clicking the gear button
      Then I see unique username starts with <UserName> in account preferences

    Examples:
      | Email      | Password      | NameAlias | Name         | UserName     |
      | user1Email | user1Password | user1Name | Jack         | jack         |
      | user1Email | user1Password | user1Name | Jack.Wireson | jackwireson  |
      | user1Email | user1Password | user1Name | Æéÿüíøšłźçñ  | aeeyuioslzcn |
      | user1Email | user1Password | user1Name | Даша         | dasha        |
      | user1Email | user1Password | user1Name |   داريا      | darya        |
      | user1Email | user1Password | user1Name | 明麗          | mengli       |
    
  @C345365 @usernames @staging
  Scenario Outline: Verify new username is synched across the devices
    Given There are 2 users where <NameAlias> is me without unique username
    Given user <NameAlias> adds a new device Device1 with label Label1
    Given User <NameAlias> changes name to <Name>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    When I see the history info page
    Then I click confirm on history info page
    And I am signed in properly
    Then I see take over screen
    And I see name <NameAlias> on take over screen
    And I see unique username starts with <Username> on take over screen
    When I click TakeThisOne button on take over screen
    And I open preferences by clicking the gear button
    Then I see unique username starts with <Username> in account preferences
    When User <NameAlias> updates the unique user name to "<NewUsername>" via device Device1
    Then I see unique username starts with <NewUsername> in account preferences
    When User <NameAlias> updates the unique user name to "<Username>" via device Device1
    Then I see unique username starts with <Username> in account preferences

    Examples:
      | Email      | Password      | NameAlias | Username | NewUsername  | Name    |
      | user1Email | user1Password | user1Name | johnson  | s123aram5r1e | johnson |


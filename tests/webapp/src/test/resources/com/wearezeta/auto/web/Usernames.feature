Feature: Usernames

  @C343171 @usernames @regression
  Scenario Outline: Verify existing user has a take over screen with offered username
    Given There are 2 users where <NameAlias> is me without unique username
    Given User <NameAlias> changes name to <Name>
    Given Myself is connected to <Contact>
    Given I enable localytics via URL parameter
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    Then I see watermark
    And I see take over screen
    And I see name <NameAlias> on take over screen
    And I see unique username starts with <Username> on take over screen
    And I see localytics event <Event1> without attributes
    And I see ChooseYourOwn button on take over screen
    And I see TakeThisOne button on take over screen
    When I click TakeThisOne button on take over screen
    Then I see conversation with <Contact> is selected in conversations list
    And I see localytics event <Event2> with attributes <Attributes2>
    When I open preferences by clicking the gear button
    Then I see unique username starts with <Username> in account preferences

    Examples:
      | Email      | Password      | NameAlias | Name         | Contact   | Username    | Event1                          | Event2                             | Attributes2                |
      | user1Email | user1Password | user1Name | Jack Johnson | user2Name | jackjohnson | onboarding.seen_username_screen | onboarding.kept_generated_username | {\"outcome\":\"success\"}" |

  @C343172 @usernames @regression @useSpecialEmail
  Scenario Outline: Verify new user has a take over screen with offered username
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And I accept the Terms of Use
    And I start activation email monitoring
    And I submit registration form
    When I activate user by URL
    Then I see watermark
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

  @C343174 @usernames @regression
  Scenario Outline: Verify take over screen doesn't go away on reload
    Given There are 2 users where <NameAlias> is me without unique username
    Given User <NameAlias> changes name to <Name>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    Then I see watermark
    And I see take over screen
    And I see name <NameAlias> on take over screen
    And I see unique username starts with <Username> on take over screen
    When I refresh page
    Then I see watermark
    And I see take over screen
    And I see name <NameAlias> on take over screen
    And I see unique username starts with <Username> on take over screen

    Examples:
      | Email      | Password      | NameAlias | Name               | Username         |
      | user1Email | user1Password | user1Name | Hans-Peter Baxxter | hanspeterbaxxter |

  @C343175 @usernames @regression @useSpecialEmail
  Scenario Outline: Verify Settings are opened on choosing generating your own username after registration
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And I accept the Terms of Use
    And I start activation email monitoring
    And I submit registration form
    When I activate user by URL
    Then I see watermark
    And I see take over screen
    And I see name <NameAlias> on take over screen
    And I see unique username starts with <Username> on take over screen
    When I click ChooseYourOwn button on take over screen
    Then I see unique username starts with <Username> in account preferences

    Examples:
      | Email      | Password      | NameAlias | Name         | Username    |
      | user1Email | user1Password | user1Name | Jack Johnson | jackjohnson |

  @C352242 @usernames @mute
  Scenario Outline: Verify it's possible to take over previously released username
    Given There are 2 users where <Name> is me without unique username
    Given <Contact> has unique username
    Given I remember unique username of <Contact>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    Then I see watermark
    Then I see take over screen
    And I see name <Name> on take over screen
    When I click ChooseYourOwn button on take over screen
    Then I see unique username starts with <Name> in account preferences
    When I change my unique username to previously remembered unique username
    And I open account in preferences
    Then I do not see unique username is the remembered one in account preferences
    When User <Contact> changes his unique username to a random value
    And I change my unique username to previously remembered unique username
    When I open account in preferences
    Then I see unique username is the remembered one in account preferences

    Examples:
      | Email      | Password      | Contact   | Name      |
      | user1Email | user1Password | user2Name | user1Name |

  @C343176 @usernames @regression
  Scenario Outline: Verify Settings are opened on choosing generating your own username for existing user
    Given There are 2 users where <NameAlias> is me without unique username
    Given User <NameAlias> changes name to <Name>
    Given I enable localytics via URL parameter
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    Then I see watermark
    And I see take over screen
    And I see name <NameAlias> on take over screen
    And I see unique username starts with <Username> on take over screen
    When I click ChooseYourOwn button on take over screen
    Then I see unique username starts with <Username> in account preferences
    And I see localytics event <Event> without attributes

    Examples:
      | Email      | Password      | NameAlias | Name         | Username    | Event                               |
      | user1Email | user1Password | user1Name | Jack Johnson | jackjohnson | onboarding.opened_username_settings |

  @C343177 @usernames @regression
  Scenario Outline: Verifying impossibility to set username with less than 2 characters
    Given There are 2 users where <NameAlias> is me without unique username
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    Then I see watermark
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
    Then I see watermark
    And I see take over screen
    And I see name <NameAlias> on take over screen

    Examples:
      | Email      | Password      | NameAlias | IncorrectName | Hint                                        |
      | user1Email | user1Password | user1Name |               | At least 2 characters. a—z, 0—9 and _ only. |
      | user1Email | user1Password | user1Name | a             | At least 2 characters. a—z, 0—9 and _ only. |

  @C352078 @usernames @regression
  Scenario Outline: Verifying impossibility to set username with more than 21 characters or illegal characters
    Given There are 1 users where <NameAlias> is me
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    And I open preferences by clicking the gear button
    Then I see unique username is <UniqueUsername> in account preferences
    When I type <UniqueUsername> <IllegalCharacters> <LegalCharacters> into unique username field
    When I change unique username to <UniqueUsername> <IllegalCharacters> <LegalCharacters>
    And I see unique username is <UniqueUsername> <RemainingCharacters> in account preferences
    When I refresh page
    And I am signed in properly
    And I open preferences by clicking the gear button
    And I see unique username is <UniqueUsername> <RemainingCharacters> in account preferences

    Examples:
      | Email      | Password      | NameAlias | UniqueUsername      | IllegalCharacters    | LegalCharacters        | RemainingCharacters |
      | user1Email | user1Password | user1Name | user1UniqueUsername | ÆéÿüíøšłźçñДаша 明麗 | 901234567890abcdefghij | 901234567890a       |

  @C352079 @usernames @regression
  Scenario Outline: Verify username is unique
    Given There are 2 users where <Name> is me without unique username
    Given User <SecondUser> changes unique username to <SecondUser>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    Then I see watermark
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
    Then I see watermark
    And I see take over screen
    And I see name <Name> on take over screen

    Examples:
      | Email      | Password      | Name      | SecondUser | Error         |
      | user1Email | user1Password | user1Name | user2Name  | Already taken |

  @C352077 @usernames @regression
  Scenario Outline: Verify autogeneration of a username for a user (different scenarios)
    Given There are 2 users where <NameAlias> is me without unique username
    Given User <NameAlias> changes name to <Name>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    Then I see watermark
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
      | user1Email | user1Password | user1Name | Jack😼        | jack          |

  @C352080 @mute @useSpecialEmail @usernames
  Scenario Outline: Verify deleting an account release a username
    Given There are 2 users where <Name2> is me without unique username
    And User <Name> is me
    Given I switch to Sign In page
    Given I remember current page
    Given I Sign in using login <Email> and password <Password>
    Then I see watermark
    And I see take over screen
    And I see name <Name> on take over screen
    When I click ChooseYourOwn button on take over screen
    When I change unique username to <UserName>
    And I click delete account button on settings page
    And I click send button to delete account
    And I delete account of user <Name> via email
    And I navigate to previously remembered page
    And I see Sign In page
    When I enter "<Email>" in email field on Sign In page
    And I enter password "<Password>"
    And I press Sign In button
    Then the sign in error message reads <Error>
    When I Sign in using login <Email2> and password <Password>
    And I am signed in properly
    Then I see watermark
    And I see take over screen
    And I see name <Name2> on take over screen
    When I click ChooseYourOwn button on take over screen
    Then I see unique username starts with <Name2> in account preferences
    When I change unique username to <UserName>
    Then I see unique username starts with <UserName> in account preferences

    Examples:
      | Email      | Password      | Name      | Error                                     | UserName      | Name2     | Email2     |
      | user1Email | user1Password | user1Name | Please verify your details and try again. | torelease1086 | user2Name | user2Email |
    
  @C345365 @usernames @regression
  Scenario Outline: Verify new username is synched across the devices
    Given There are 2 users where <NameAlias> is me
    Given I remember unique username of Me
    Given user Me adds a new device Device1 with label Label1
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    When I see the history info page
    Then I click confirm on history info page
    And I am signed in properly
    And I open preferences by clicking the gear button
    Then I see unique username for <NameAlias> in account preferences
    When User Me updates the unique user name to random value via device Device1
    Then I do not see unique username is the remembered one in account preferences

    Examples:
      | Email      | Password      | NameAlias |
      | user1Email | user1Password | user1Name |

  @C352081 @usernames @regression
  Scenario Outline: Verify autogeneration of a username for a user with emoji in name
    Given There is 1 user where <NameAlias> is me without unique username
    Given User <NameAlias> changes name to <Name>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I see take over screen
    And I see name <NameAlias> on take over screen
    Then I remember unique user name on take over screen
    And I see remembered unique username contains a random adjective and noun
    When I click TakeThisOne button on take over screen
    And I wait for 3 seconds
    And I open preferences by clicking the gear button
    Then I see remembered unique username in account preferences

    Examples:
      | Email      | Password      | NameAlias | Name    |
      | user1Email | user1Password | user1Name | 😼      |
      | user1Email | user1Password | user1Name | 明麗    |

  @C352245 @usernames @regression
  Scenario Outline: Verify autogeneration of a username works for a user with a blacklisted name
    Given There are 1 users where <NameAlias> is me without unique username
    Given User <NameAlias> changes name to <Name>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    Then I see watermark
    And I see take over screen
    And I see name <NameAlias> on take over screen
    And I see unique username starts with <Username> on take over screen

    Examples:
      | Email      | Password      | NameAlias | Name  | Username |
      | user1Email | user1Password | user1Name | admin | admin    |

  @C352084 @usernames @regression
  Scenario Outline: Verify top people are shown without usernames
    Given There are 2 users where <NameAlias> is me
    Given User <Contact> changes unique username to <Contact>
    Given Myself is connected to <Contact>
    Given Contact Me sends message QUESTION to user <Contact>
    Given Contact <Contact> sends message ANSWER to user <NameAlias>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    When I am signed in properly
    And Myself waits until 1 people in backend top people results
    And I open search by clicking the people button
    And I wait till Top People list appears
    Then I see 1 people in Top people list
    And I see user <Contact> found in People Picker
    And I see user <Contact> found in Top People
    And I do not see username <Contact> of user <Contact> in Top People
    And I do not see user <Contact> with username <Contact> found in People Picker

    Examples:
      | Email      | Password      | NameAlias | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
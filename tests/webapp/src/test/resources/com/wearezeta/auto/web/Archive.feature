Feature: Archive

  @C1686 @regression
  Scenario Outline: Verify the conversation is unarchived when there are new messages in this conversation (simple message)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I am signed in properly
    When I archive conversation <Contact>
    Then I do not see Contact list with name <Contact>
    When Contact <Contact> sends message <Message> to group conversation <Contact>
    Then I see Contact list with name <Contact>
    Then I verify that <Contact> index in Contact list is 1

    Examples:
      | Email      | Password      | Name      | Contact   | Contact2  | Message |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Hello   |

  @C1685 @regression
  Scenario Outline: Verify archived list disappears if there are no more archived conversations
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I am signed in properly
    And I do not see Archive button at the bottom of my Contact list
    When I archive conversation <Contact>
    And I archive conversation <Contact2>
    Then I see Archive button at the bottom of my Contact list
    When Contact <Contact> sends message <Message> to group conversation <Contact>
    And I open archive
    And I unarchive conversation <Contact2>
    Then I do not see Archive button at the bottom of my Contact list

    Examples:
      | Email      | Password      | Name      | Contact   | Contact2  | Message |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Hello   |

  @C1689 @regression
  Scenario Outline: Verify that Ping event cannot unarchive muted conversation automatically
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I am signed in properly
    When I see Contact list with name <Contact>
    And I set muted state for conversation <Contact>
    And I archive conversation <Contact>
    And User <Contact> pinged in the conversation with me
    Then I do not see Contact list with name <Contact>
    And I see Archive button at the bottom of my Contact list

    Examples:
      | Email      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C345359 @staging
  Scenario Outline: Verify that Call event can unarchive muted conversation automatically
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    When I am signed in properly
    And I set muted state for conversation <Contact>
    And I archive conversation <Contact>
    Then I do not see Contact list with name <Contact>
    When <Contact> calls me
    Then I see the incoming call controls for conversation <Contact>
    When <Contact> stops calling me
    Then I see Contact list with name <Contact>
    Then I do not see Archive button at the bottom of my Contact list

    Examples:
      | Email      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | zcall       |

  @C1687 @regression
  Scenario Outline: Verify the conversation is unarchived when there are new messages in this conversation (Ping message)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I am signed in properly
    When I archive conversation <Contact>
    And User <Contact> pinged in the conversation with me
    Then I see Contact list with name <Contact>
    And I do not see Archive button at the bottom of my Contact list

    Examples:
      | Email      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C1688 @regression
  Scenario Outline: Verify the conversation is unarchived when there are new calls in this conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I am signed in properly
    And I archive conversation <Contact>
    When <Contact> calls me
    And I wait for 5 seconds
    And <Contact> stops calling me
    Then I see Contact list with name <Contact>
    And I do not see Archive button at the bottom of my Contact list

    Examples:
      | Email      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | zcall       |

  @C131206 @regression
  Scenario Outline: Verify you archive the conversation when you press ⌥ ⌘ ⇧ D (Mac) or alt + ctrl + D (Win)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I see Contact list with name <Contact1>
    And I see Contact list with name <Contact2>
    And I open conversation with <Contact1>
    And I type shortcut combination to archive the conversation
    Then I see Contact list with name <Contact2>
    And I do not see Contact list with name <Contact1>
    When I open archive
    Then I see archive list with name <Contact1>

    Examples:
      | Email      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |
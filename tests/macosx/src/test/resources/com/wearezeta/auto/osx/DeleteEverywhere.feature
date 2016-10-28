Feature: Delete Everywhere

  @C206255 @smoke
  Scenario Outline: Verify I can delete my message everywhere (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    Then I open conversation with <Contact>
    And I write message <Message>
    And I send message
    Then I verify the last text message equals to <Message>
    And I see 2 messages in conversation
    When I click context menu of the last message
    And I click delete everywhere in message context menu for my own message
    And I click confirm to delete message for everyone
    Then I do not see text message <Message>
    And I see 1 messages in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   | Message   |
      | user1Email | user1Password | user1Name | user2Name | delete me |

  @C206275 @smoke
  Scenario Outline: Verify I can delete my message locally (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    Then I open conversation with <Contact>
    And I write message <Message>
    And I send message
    Then I verify the last text message equals to <Message>
    And I see 2 messages in conversation
    When I click context menu of the last message
    And I click delete in message context menu for my own message
    And I click confirm to delete message for me
    Then I do not see text message <Message>
    And I see 1 messages in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   | Message   |
      | user1Email | user1Password | user1Name | user2Name | delete me |

  @C206276 @smoke
  Scenario Outline: Verify I can only locally delete a message from other person (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given user <Contact> adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    Then I open conversation with <Contact>
    And Contact <Contact> sends message <Message> via device Device1 to user <Name>
    Then I verify the last text message equals to <Message>
    And I see 2 messages in conversation
    When I click context menu of the last message
# as long as there is no entry for deleting everywhere this will select delete locally
    And I click delete everywhere in message context menu for my own message
# following step will fail if it's NOT local delete
    And I click confirm to delete message for me
    Then I do not see text message <Message>
    And I see 1 messages in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   | Message   |
      | user1Email | user1Password | user1Name | user2Name | delete me |
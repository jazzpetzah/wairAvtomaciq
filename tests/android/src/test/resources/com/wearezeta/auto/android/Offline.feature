Feature: Offline

  @C415 @id1515 @regression
  Scenario Outline: Receive updated content when changing from offline to online
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact>
    When User <Contact> sends encrypted message <Message1> to user Myself
    Then Last message is <Message1>
    When I enable Airplane mode on the device
    And User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    Then I do not see new picture in the dialog
    When User <Contact> sends encrypted message <Message2> to user Myself
    Then Last message is <Message1>
    When I disable Airplane mode on the device
    And I scroll to the bottom of conversation view
    Then Last message is <Message2>
    And I see new picture in the dialog

    Examples:
      | Name      | Contact   | Message1 | Message2 | Picture     |
      | user1Name | user2Name | Msg1     | Msg2     | testing.jpg |

  @C720 @id1516 @regression @rc
  Scenario Outline: I want to see an unsent indicator when I send message or image during offline
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact>
    And I enable Airplane mode on the device
    When I tap on text input
    And I type the message "<Message>" and send it
    Then I see unsent indicator next to the message "<Message>" in the dialog
    When I hide keyboard
    And I swipe on text input
    And I press Add Picture button
    And I press "Take Photo" button
    And I press "Confirm" button
    Then I see unsent indicator next to new picture in the dialog

    Examples:
      | Name      | Contact   | Message    |
      | user1Name | user2Name | My message |
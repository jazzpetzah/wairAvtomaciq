Feature: Offline

  @C415 @regression
  Scenario Outline: Receive updated content when changing from offline to online
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I tap on conversation name <Contact>
    When User <Contact> sends encrypted message <Message1> to user Myself
    Then I see the most recent conversation message is "<Message1>"
    When I enable Airplane mode on the device
    And I see No Internet bar in 15 seconds
    And User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    Then I do not see any pictures in the conversation view
    When User <Contact> sends encrypted message <Message2> to user Myself
    Then I see the most recent conversation message is "<Message1>"
    When I disable Airplane mode on the device
    And I do not see No Internet bar in 15 seconds
    # Wait for sync
    And I wait for 5 seconds
    And I scroll to the bottom of conversation view
    Then I see the most recent conversation message is "<Message2>"
    And I see a picture in the conversation view

    Examples:
      | Name      | Contact   | Message1 | Message2 | Picture     |
      | user1Name | user2Name | Msg1     | Msg2     | testing.jpg |

  @C720 @regression @rc
  Scenario Outline: I want to see an unsent indicator when I send message or image during offline
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I tap on conversation name <Contact>
    And I enable Airplane mode on the device
    When I tap on text input
    And I type the message "<Message>" and send it
    Then I see unsent indicator next to "<Message>" in the conversation view
    When I hide keyboard
    And I tap Add picture button from cursor toolbar
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    And I scroll to the bottom of conversation view
    Then I see unsent indicator next to new picture in the dialog

    Examples:
      | Name      | Contact   | Message    |
      | user1Name | user2Name | My message |
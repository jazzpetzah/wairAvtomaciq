Feature: Offline Mode

  @C778 @regression @rc
  Scenario Outline: Receive updated content when changing from offline to online
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact>
    When User <Contact> sends encrypted message <Message1> to user Myself
    Then I see the message "<Message1>" in the conversation view
    When I enable Airplane mode on the device
    And User <Contact> sends encrypted message <Message2> to user Myself
    And I wait for 3 seconds
    Then I do not see the message "<Message2>" in the conversation view
    When User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    And I wait for 3 seconds
    Then I do not see any new pictures in the conversation view
    When I disable Airplane mode on the device
    # To let the content load properly after offline mode
    And I wait for 15 seconds
    Then I see the message "<Message2>" in the conversation view
    And I scroll to the bottom of the conversation view
    And I see a new picture in the conversation view

    Examples:
      | Name      | Contact   | Message1 | Message2  | Picture     |
      | user1Name | user2Name | FirstMsg | SecondMsg | testing.jpg |

  @C780 @regression @rc
  Scenario Outline: (CM-682) I want to see an unsent indicator when I send message or image during offline
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact>
    When I enable Airplane mode on the device
    And I tap on text input
    And I type the message "<Message>" in the conversation view
    And I send the typed message in the conversation view
    Then I see the message "<Message>" in the conversation view
    And I see unsent indicator next to the message "<Message>" in the conversation view
    When I hide keyboard
    And I tap Add picture button from cursor toolbar
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a new picture in the conversation view
    And I scroll to the bottom of the conversation view
    And I see unsent indicator next to new picture in the conversation view

    Examples:
      | Name      | Contact   | Message    |
      | user1Name | user2Name | My message |
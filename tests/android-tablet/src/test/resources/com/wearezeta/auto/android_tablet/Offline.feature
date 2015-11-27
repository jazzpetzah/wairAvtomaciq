Feature: Offline Mode

  @id2889 @regression @rc
  Scenario Outline: Receive updated content when changing from offline to online
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    When Contact <Contact> sends message "<Message1>" to user Myself
    Then I see the message "<Message1>" in the conversation view
    When I enable Airplane mode on the device
    And Contact <Contact> sends image <Picture> to single user conversation <Name>
    Then I do not see any new pictures in the conversation view
    When Contact <Contact> sends message "<Message2>" to user Myself
    Then I do not see the message "<Message2>" in the conversation view
    When I disable Airplane mode on the device
    # To let the content load properly after offline mode
    And I wait for 15 seconds
    And I scroll to the bottom of the conversation view
    Then I see the message "<Message2>" in the conversation view
    And I see a new picture in the conversation view

    Examples:
      | Name      | Contact   | Message1 | Message2  | Picture     |
      | user1Name | user2Name | FirstMsg | SecondMsg | testing.jpg |

  @id2904 @regression @rc
  Scenario Outline: (CM-682) I want to see an unsent indicator when I send message or image during offline
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    When I enable Airplane mode on the device
    And I tap the text input in the conversation view
    And I type the message "<Message>" in the conversation view
    And I send the typed message in the conversation view
    Then I see the message "<Message>" in the conversation view
    And I see unsent indicator next to the message "<Message>" in the conversation view
    When I hide keyboard
    And I swipe right on text input in the conversation view
    And I tap Add Picture button in the conversation view
    And I tap Take Photo button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view
    And I see unsent indicator next to new picture in the conversation view

    Examples:
      | Name      | Contact   | Message    |
      | user1Name | user2Name | My message |

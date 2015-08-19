Feature: Ping

  @id1515 @staging
  Scenario Outline: Receive updated content when changing from offline to online
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And I tap on contact name <Contact>
    And I see dialog page
    When Contact <Contact> send message <Message1> to user <Name>
    Then Last message is <Message1>
    When I enable Airplane mode on the device
    And Contact <Contact> sends image <Picture> to single user conversation <Name>
    Then I do not see new picture in the dialog
    When Contact <Contact> send message <Message2> to user <Name>
    Then Last message is <Message1>
    When I disable Airplane mode on the device
    And I scroll to the bottom of conversation view
    Then Last message is <Message2>
    And I see new picture in the dialog

    Examples: 
      | Name      | Contact   | Message1 | Message2 | Picture     |
      | user1Name | user2Name | Msg1     | Msg2     | testing.jpg | 
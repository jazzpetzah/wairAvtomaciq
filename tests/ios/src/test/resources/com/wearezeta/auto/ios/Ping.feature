Feature: Ping

  @staging @id1357
  Scenario Outline: Verify you can send Ping in a group conversation
    Given I Sign in using login <Login> and password <Password>
    Given I have group chat named <GroupChatName> with an unconnected user, made by <GroupCreator>
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I swipe the text input cursor
    And I click Ping button
    Then I see You Pinged message in the dialog
    And I click Ping button
    Then I see You Pinged Again message in the dialog

    Examples: 
      | Login   | Password    | Name    | GroupCreator      | GroupChatName |
      | aqaUser | aqaPassword | aqaUser | aqaPictureContact | TESTCHAT      |

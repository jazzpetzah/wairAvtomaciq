Feature: Calling

  @id3175 @staging @torun
  Scenario Outline: Verify receiving "missed call" notification (GCM) after ending group call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    When <Contact1>,<Contact2> call <GroupChatName> using <CallBackend>
    And I wait for 5 seconds
    And <Contact1>,<Contact2> stop all calls to <GroupChatName>
    Then I see missed group call notification in the conversation view

    Examples: 
      | CallBackend | Name      | Contact1  | Contact2  | GroupChatName    |
      | autocall    | user1Name | user2Name | user3Name | ChatForGroupCall |


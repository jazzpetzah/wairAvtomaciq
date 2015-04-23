Feature: Archive

  @staging @id1540
  Scenario Outline: Verify the conversation is unarchived when there are new messages in this conversation (simple message)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I Sign in using login <Email> and password <Password>
    And I see my name on top of Contact list
    When I archive conversation <Contact>
    Then I do not see Contact list with name <Contact>
    When User <Contact> sent message <Message> to conversation <Contact>
    Then I see Contact list with name <Contact>
    Then I verify that <Contact> index in Contact list is 1

    Examples: 
      | Email      | Password      | Name      | Contact   | Contact2  | Message |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Hello   |

  @staging @id1537
  Scenario Outline: Verify acrhived list disappears if there are no more archived conversations
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I Sign in using login <Email> and password <Password>
    And I see my name on top of Contact list
    And I do not see Archive button at the bottom of my Contact list
    When I archive conversation <Contact>
    And I archive conversation <Contact2>
    Then I see Archive button at the bottom of my Contact list
    When User <Contact> sent message <Message> to conversation <Contact>
    And I open archive
    And I unarchive conversation <Contact2>
    Then I do not see Archive button at the bottom of my Contact list

    Examples: 
      | Email      | Password      | Name      | Contact   | Contact2  | Message |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Hello   |
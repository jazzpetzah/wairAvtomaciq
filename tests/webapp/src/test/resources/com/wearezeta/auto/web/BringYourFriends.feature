Feature: Bring Your Friends

  @C1727 @regression @id1742
  Scenario Outline: Invite people when you have no contacts
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I see Invite People button on People Picker page
    And I click Invite People button on People Picker page
    Then I see Invite People popover
    And I do not see Share Contacts button
    When I remember invitation link on Bring Your Friends popover
    And I navigate to previously remembered invitation link
    Then I see You are invited page

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C3217 @regression
  Scenario Outline: Invite people when you have top people or search suggestions
    Given There is 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User Me sent message <Message> to conversation <Contact>
    Given User <Contact> sent message <Message> to conversation <Name>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I see my avatar on top of Contact list
    And Myself waits until 1 people in backend top people results
    And I open People Picker from Contact List
    Then I see Invite People button on People Picker page
    When I click Invite People button on People Picker page
    Then I see Invite People popover
    And I see Share Contacts button
    When I remember invitation link on Bring Your Friends popover
    And I navigate to previously remembered invitation link
    Then I see You are invited page

    Examples: 
      | Login      | Password      | Name      | Contact   | Message |
      | user1Email | user1Password | user1Name | user2Name | Hello   |

  @C1774 @regression @id2227
  Scenario Outline: Show invitation button when Gmail import has no suggestions
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    Given I see Sign In page
    When I enter email "<Email>"
    And I enter password "<Password>"
    And I press Sign In button
    Then I am signed in properly
    And I click button to bring friends from Gmail
    And I see Google login popup
    When I sign up at Google with email <Gmail> and password <GmailPassword>
    Then I see Search is opened
    And I see Bring Your Friends button on People Picker page
    When I click Bring Your Friends button on People Picker page
    Then I remember invitation link on Bring Your Friends popover
    And I do not see Gmail Import button on People Picker page

    Examples: 
      | Email      | Password      | Name      | Gmail                       | GmailPassword |
      | user1Email | user1Password | user1Name | smoketester.wire2@gmail.com | aqa123456!    |

  @C1775 @regression @id2229
  Scenario: Use Gmail contacts import from search UI
    Given There is 1 user where user1Name is me without avatar picture
    Given I switch to Sign In page
    Given I Sign in using login user1Email and password user1Password
    Given I see Self Picture Upload dialog
    Given I force carousel mode on Self Picture Upload dialog
    Given I confirm picture selection on Self Picture Upload dialog
    When I click button to bring friends from Gmail
    And I see Google login popup
    And I sign up at Google with email smoketester.wire@gmail.com and password aqa123456!
    Then I see more than 5 suggestions in people picker

  @C3218 @regression
  Scenario Outline: Switch between Invitation and Share Contacts bubbles
    Given There is 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User Me sent message <Message> to conversation <Contact>
    Given User <Contact> sent message <Message> to conversation <Name>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I see my avatar on top of Contact list
    And Myself waits until 1 people in backend top people results
    And I open People Picker from Contact List
    Then I see Invite People button on People Picker page
    When I click Invite People button on People Picker page
    Then I see Invite People popover
    And I see Share Contacts button
    When I click Share Contacts button
    Then I click Invite People button
    And I click Share Contacts button

    Examples: 
      | Login      | Password      | Name      | Contact   | Message |
      | user1Email | user1Password | user1Name | user2Name | Hello   |
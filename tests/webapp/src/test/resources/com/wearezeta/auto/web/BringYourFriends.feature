Feature: Bring Your Friends

  @C1727 @regression
  Scenario Outline: Invite people when you have no contacts
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    When I see Bring Your Friends or Invite People button
    And I click Bring Your Friends or Invite People button
    Then I see Invite People popover
    And I do not see Share Contacts button
    When I remember invitation link on Bring Your Friends popover
    And I navigate to previously remembered invitation link
    Then I see Registration page
    And I verify text about Wire is visible
    And I see intro about Wire saying <TextWire>

    Examples: 
      | Login      | Password      | Name      | TextWire                                                                                    |
      | user1Email | user1Password | user1Name | Simple, private & secure messenger for chat, calls, sharing pics, music, videos, GIFs and more.       |

  @C3217 @regression
  Scenario Outline: Invite people when you have top people or search suggestions
    Given There is 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given Contact Me sends message <Message> to user <Contact>
    Given Contact <Contact> sends message <Message> to user <Name>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    When I am signed in properly
    And Myself waits until 1 people in backend top people results
    And I open People Picker from Contact List
    Then I see Bring Your Friends or Invite People button
    When I click Bring Your Friends or Invite People button
    Then I see Invite People popover
    And I see Share Contacts button
    When I remember invitation link on Bring Your Friends popover
    And I navigate to previously remembered invitation link
    Then I see Registration page
    And I verify text about Wire is visible
    And I see intro about Wire saying <TextWire>

    Examples: 
      | Login      | Password      | Name      | Contact   | Message | TextWire                                                                                    |
      | user1Email | user1Password | user1Name | user2Name | Hello   | Simple, private & secure messenger for chat, calls, sharing pics, music, videos, GIFs and more.       |

  @C1774 @regression
  Scenario Outline: Show invitation button when Gmail import has no suggestions
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    Given I see Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I click button to bring friends from Gmail
    And I see Google login popup
    When I sign up at Google with email <Gmail> and password <GmailPassword>
    Then I see Search is opened
    And I see Bring Your Friends or Invite People button
    When I click Bring Your Friends or Invite People button
    Then I remember invitation link on Bring Your Friends popover
    And I do not see Gmail Import button on People Picker page

    Examples: 
      | Email      | Password      | Name      | Gmail                       | GmailPassword |
      | user1Email | user1Password | user1Name | smoketester.wire2@gmail.com | aqa123456!    |

  @C1775 @regression
  Scenario: Use Gmail contacts import from search UI
    Given There is 1 user where user1Name is me without avatar picture
    Given I switch to Sign In page
    Given I Sign in using login user1Email and password user1Password
    Given I see Welcome page
    Given I confirm keeping picture on Welcome page
    When I click button to bring friends from Gmail
    And I see Google login popup
    And I sign up at Google with email smoketester.wire@gmail.com and password aqa123456!
    Then I see more than 5 suggestions in people picker
    And I remember first suggested user
    And I see first remembered user in People Picker
    When I click on remembered not connected contact found in People Picker
    And I see Connect To popover
    And I click Connect button on Connect To popover
    Then I see Contact list with remembered user
    And I see cancel pending request button in the conversation view
    And I verify that conversation input and buttons are not visible
    When I open People Picker from Contact List
    And I click button to bring friends from Gmail
    And I click on remembered pending contact found in People Picker
    And I click Cancel request on Pending Outgoing Connection popover
    And I see Cancel request confirmation popover
    And I click Yes button on Cancel request confirmation popover
    And I close People Picker
    Then I do not see Contact list with remembered user

  @C3218 @regression
  Scenario Outline: Switch between Invitation and Share Contacts bubbles
    Given There is 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given Contact Me sends message <Message> to user <Contact>
    Given Contact <Contact> sends message <Message> to user <Name>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    When I am signed in properly
    And Myself waits until 1 people in backend top people results
    And I open People Picker from Contact List
    Then I see Bring Your Friends or Invite People button
    When I click Bring Your Friends or Invite People button
    Then I see Invite People popover
    And I see Share Contacts button
    When I click Share Contacts button
    Then I click Invite People button
    And I click Share Contacts button

    Examples: 
      | Login      | Password      | Name      | Contact   | Message |
      | user1Email | user1Password | user1Name | user2Name | Hello   |

  @C80773 @regression
  Scenario Outline: Use Gmail contacts import from settings
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I click gear button on self profile page
    And I select Settings menu item on self profile page
    And I see Settings dialog
    And I click button to import contacts from Gmail
    And I see Google login popup
    And I sign up at Google with email smoketester.wire@gmail.com and password aqa123456!
    Then I see more than 5 suggestions in people picker

    Examples:
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |
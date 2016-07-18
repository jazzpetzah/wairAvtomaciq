Feature: Invitations

  @C824 @regression @rc @useSpecialEmail
  Scenario Outline: (AN-4019) Invitations (Conversations List): I can send an email notification from conversations list
    Given I delete all contacts from Address Book
    Given There is 1 user where <Name> is me
    Given I add <Contact> into Address Book
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    And I open Search UI
    And I see user <Contact> in Contact list
    And I remember the state of <Contact> avatar in Contact list
    And I tap Invite button next to <Contact>
    And I select <ContactEmail> email on invitation sending alert
    And I start listening to invitation messages for <Contact>
    And I tap OK button on the alert
    Then I verify the state of <Contact> avatar in Contact list is changed
    And I verify user <Contact> has received an email invitation

    Examples:
      | Name      | Contact   | ContactEmail |
      | user1Name | user2Name | user2Email   |

  @C825 @regression @rc @useSpecialEmail
  Scenario Outline: Invitations (Registration): I can receive and accept an email notification
    Given There is 1 user where <Name> is me
    Given I start listening to invitation messages for <Contact>
    Given Myself sends personal invitation to mail <ContactEmail> with message <Message>
    Given I verify user <Contact> has received an email invitation
    Given I see welcome screen
    When I hide keyboard
    And I press back button
    And I broadcast the invitation for <ContactEmail>
    And I restore the application
    And I input password "<ContactPassword>"
    And I confirm password
    And I wait until Unsplash screen is visible
    And I select to choose my own picture
    And I select Camera as picture source
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    And I add <Contact> to the list of test case users
    And User <Contact> is me without picture
    Then I see Contact list with contacts
    When I tap on contact name <Name>
    Then I see conversation view

    Examples:
      | Name      | Contact   | ContactEmail | ContactPassword | Message |
      | user1Name | user2Name | user2Email   | user2Password   | Hello   |

  @C567 @regression
  Scenario Outline: Verify that swipe do nothing in invites page
    Given There are 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I open Search UI
    And I hide keyboard
    And I take screenshot
    And I swipe up
    Then I verify the previous and the current screenshots are not different
    When I swipe right
    Then I verify the previous and the current screenshots are not different
    When I swipe left
    Then I verify the previous and the current screenshots are not different

    Examples:
      | Name      |
      | user1Name |

  @C568 @regression @rc
  Scenario Outline: Sending invite to user which already on Wire create pending connection request
    Given I delete all contacts from Address Book
    Given There are 2 users where <Name> is me
    Given I add <Contact> into Address Book
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    And I open Search UI
    And I see user <Contact> in Contact list
    And I tap Invite button next to <Contact>
    And I select <ContactEmail> email on invitation sending alert
    And I tap OK button on the alert
    And I hide keyboard
    When I press back button
    Then I see contact list with name <Contact>
    When I tap on contact name <Contact>
    Then I see outgoing pending connection to <Contact>

    Examples:
      | Name      | Contact   | ContactEmail |
      | user1Name | user2Name | user2Email   |
Feature: Invitations

  @id4161 @regression @rc
  Scenario Outline: Invitations (Conversations List): I can send an email notification from conversations list
    Given I delete all contacts from Address Book
    Given There is 1 user where <Name> is me
    Given I add <Contact> into Address Book
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    And I tap Invite button at the bottom of conversations list
    And I see <Contact> in the invites list
    And I remember the state of <Contact> avatar in the invites list
    And I tap Invite button next to <Contact>
    And I select <ContactEmail> email on invitation sending alert
    And I confirm invitation sending alert
    Then I verify the state of <Contact> avatar in the invites list is changed
    And I verify user <Contact> has received an email invitation

    Examples: 
      | Name      | Contact   | ContactEmail |
      | user1Name | user2Name | user2Email   |

  @id4162 @regression @rc
  Scenario Outline: Invitations (Registration): I can receive and accept an email notification
    Given There is 1 user where <Name> is me
    Given Myself sends personal invitation to mail <ContactEmail> with message <Message>
    Given I verify user <Contact> has received an email invitation
    Given I see welcome screen
    When I hide keyboard
    And I press back button
    And I broadcast the invitation for <ContactEmail>
    And I restore the application
    And I input password "<ContactPassword>"
    And I confirm password
    And I press Camera button twice
    And I confirm selection
    And I add <Contact> to the list of test case users
    And User <Contact> is me without picture
    Then I see Contact list with contacts
    When I tap on contact name <Name>
    Then I see dialog page

    Examples:
      | Name      | Contact   | ContactEmail | ContactPassword | Message |
      | user1Name | user2Name | user2Email   | user2Password   | Hello   |

  @id4158 @regression
  Scenario Outline: Invite people button present in the list if user has up to 5 contacts
    Given There are 7 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And I wait until <Contact6> exists in backend search results
    Then I see invite more people button in contacts list
    Given <Contact6> sent connection request to <Name>
    When <Name> accept all requests
    Then I do not see invite more people button in contacts list
    When I swipe right on a <Contact1>
    And I select DELETE from conversation settings menu
    And I press DELETE on the confirm alert
    Then I see invite more people button in contacts list

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | Contact6  |
      | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | user7Name |

  @id4157 @regression
  Scenario Outline: Invite people button replaced with actions buttons when connected users selected in search
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I open search by tap
    Then I see invite more people button in search
    When I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    Then I see action buttons appeared on People picker page
    And I do not see invite more people button in search

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |
      
  @id4159 @staging
  Scenario Outline: Verify that keyboard is closed when I close invites page
    Given There are 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    When I take screenshot
    And I tap Invite button at the bottom of conversations list
    And I tap search in invites page
    And I tap invites page close button
    Then I see Contact list with no contacts
    When I take screenshot
    Then I compare 1st and 2nd screenshots and they are not different

    Examples: 
      | Name      |
      | user1Name |

  @id4172 @staging
  Scenario Outline: (AN-3118) Sending invite to user which already on Wire create pending connection request
    Given I delete all contacts from Address Book
    Given There are 2 users where <Name> is me
    Given I add <Contact> into Address Book
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    And I tap Invite button at the bottom of conversations list
    And I see <Contact> in the invites list
    And I tap Invite button next to <Contact>
    And I select <ContactEmail> email on invitation sending alert
    And I confirm invitation sending alert
    When I press back button
    # FIXME: workaround for AN-3118
    And I minimize the application
    And I restore the application
    Then I see contact list with name <Contact>
    When I tap on contact name <Contact>
    Then I see outgoing pending connection to <Contact>

    Examples:
      | Name      | Contact   | ContactEmail |
      | user1Name | user2Name | user2Email   |
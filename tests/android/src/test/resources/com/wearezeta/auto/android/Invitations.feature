Feature: Invitations

  @id4161 @regression @rc
  Scenario Outline: (AN-3090) Invitations (Conversations List): I can send an email notification from conversations list
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

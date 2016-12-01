Feature: Unique Usernames

  @C352028 @staging
  Scenario Outline: Verify autogeneration of a username for a user with latin characters only
    Given I see sign in screen
    Given I enter phone number for <Name>
    Given I enter activation code
    Given I accept terms of service
    Given I input name <Name> and commit it
    Given I accept alert if visible
    Given I tap Keep This One button
    Given I tap Not Now button on Share Contacts overlay
    # Wait until takeover screen appears
    Given I wait for 7 seconds
    When I see username <WireName> on Unique Username Takeover page
    Then I see unique username <ExpectedUniqueName> on Unique Username Takeover page
    When I tap Keep This One button on Unique Username Takeover page
    Then I see conversations list

    Examples:
      | Name      | ExpectedUniqueName   |
      | user1Name | @user1UniqueUsername |

  @C352060 @addressbookStart @forceReset @torun
  Scenario Outline: Verify incoming connection view
    Given There are 8 users where <Name> is me
    Given <Contact1WithABEmail> sent connection request to Me
    Given <Contact2WithABPhoneNumber> sent connection request to Me
    Given <Contact3WithUniqueUserName> sent connection request to Me
    Given User <Contact3WithUniqueUserName> sets the unique username
    Given <Contact4WOUniqueUserName> sent connection request to Me
    Given <Contact5WithCommonFriends> sent connection request to Me
    Given <Contact5WithCommonFriends> is connected to <Contact7Common>
    Given <Contact6WithSameNameInAB> sent connection request to Me
    Given <Contact7Common> is connected to me
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I add name <Contact1ABName> and email <Contact1Email> to Address Book
    Given I add name <Contact2ABName> and phone <Contact2PhoneNumber> to Address Book
    Given I add name <Contact6WithSameNameInAB> and email <Contact6Email> to Address Book
    Given I restore Wire
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I open search UI
    Given I accept alert if visible
    Given I tap input field on Search UI page
    Given I type "<Contact1WithABEmail>" in Search UI input field
    When I tap on conversation <Contact1WithABEmail> in search result
    And I see <Contact1WithABEmail> name on Single user Pending incoming connection page
    Then I see <Contact1ABName> Address Book name on Single user Pending incoming connection page
    And I tap Ignore button on Single user Pending incoming connection page
    When I tap X button on Search UI page
    And I type "<Contact2WithABPhoneNumber>" in Search UI input field
    And I tap on conversation <Contact2WithABPhoneNumber> in search result
    Then I see <Contact2WithABPhoneNumber> name on Single user Pending incoming connection page
    And I see <Contact2ABName> Address Book name on Single user Pending incoming connection page

    Examples:
      | Name      | Contact1WithABEmail | Contact1ABName | Contact1Email | Contact2WithABPhoneNumber | Contact2ABName | Contact2PhoneNumber | Contact3WithUniqueUserName | Contact4WOUniqueUserName | Contact5WithCommonFriends | Contact6WithSameNameInAB | Contact6Email | Contact7Common |
      | user1Name | user2Name           | user2ABName    | user2Email    | user3Name                 | user3ABName    | user3PhoneNumber    | user4Name                  | user5Name                | user6Name                 | user7Name                | user7Email    | user8Name      |
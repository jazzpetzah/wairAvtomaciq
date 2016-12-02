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

  @C352060 @addressbookStart @forceReset
  Scenario Outline: (ZIOS-7699) Verify incoming connection view
    Given There are 7 users where <Name> is me
    Given <Contact1WithABEmail> sent connection request to Me
    Given <Contact2WithABPhoneNumber> sent connection request to Me
    Given <Contact3WithUniqueUserName> sent connection request to Me
    Given User <Contact3WithUniqueUserName> sets the unique username
    Given <Contact4WithCommonFriends> sent connection request to Me
    Given <Contact4WithCommonFriends> is connected to <Contact6Common>
    Given <Contact5WithSameNameInAB> sent connection request to Me
    Given <Contact6Common> is connected to me
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I add name <Contact1ABName> and email <Contact1Email> to Address Book
    Given I add name <Contact2ABName> and phone <Contact2PhoneNumber> to Address Book
    Given I add name <Contact5WithSameNameInAB> and email <Contact5Email> to Address Book
    Given I restore Wire
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I open search UI
    Given I accept alert if visible
    Given I tap input field on Search UI page
    Given I type "<Contact1WithABEmail>" in Search UI input field
    When I tap on conversation <Contact1WithABEmail> in search result
    And I see name "<Contact1WithABEmail>" on Single user Pending incoming connection page
    Then I see Address Book name "<Contact1ABName>" on Single user Pending incoming connection page
    And I do not see unique username on Single user Pending incoming connection page
    And I tap Ignore button on Single user Pending incoming connection page
    When I tap X button on Search UI page
    And I type "<Contact2WithABPhoneNumber>" in Search UI input field
    And I tap on conversation <Contact2WithABPhoneNumber> in search result
    Then I see name "<Contact2WithABPhoneNumber>" on Single user Pending incoming connection page
    And I see Address Book name "<Contact2ABName>" on Single user Pending incoming connection page
    And I do not see unique username on Single user Pending incoming connection page
    And I tap Ignore button on Single user Pending incoming connection page
    When I tap X button on Search UI page
    And I type "<Contact3WithUniqueUserName>" in Search UI input field
    And I tap on conversation <Contact3WithUniqueUserName> in search result
    Then I see name "<Contact3WithUniqueUserName>" on Single user Pending incoming connection page
    And I see unique username "<Contact3UniqueUserName>" on Single user Pending incoming connection page
    And I do not see Address Book name on Single user Pending incoming connection page
    And I tap Ignore button on Single user Pending incoming connection page
    When I tap X button on Search UI page
    And I type "<Contact4WithCommonFriends>" in Search UI input field
    And I tap on conversation <Contact4WithCommonFriends> in search result
    Then I see name "<Contact4WithCommonFriends>" on Single user Pending incoming connection page
    And I do not see unique username on Single user Pending incoming connection page
    And I do not see Address Book name on Single user Pending incoming connection page
    # TODO: Verify common friends label
    And I tap Ignore button on Single user Pending incoming connection page
    When I tap X button on Search UI page
    And I type "<Contact5WithSameNameInAB>" in Search UI input field
    And I tap on conversation <Contact5WithSameNameInAB> in search result
    Then I see name "<Contact5WithSameNameInAB>" on Single user Pending incoming connection page
    And I do not see unique username on Single user Pending incoming connection page
    And I see Address Book name "" on Single user Pending incoming connection page
    And I tap Ignore button on Single user Pending incoming connection page

    Examples:
      | Name      | Contact1WithABEmail | Contact1ABName | Contact1Email | Contact2WithABPhoneNumber | Contact2ABName | Contact2PhoneNumber | Contact3WithUniqueUserName | Contact3UniqueUserName | Contact4WithCommonFriends | Contact5WithSameNameInAB | Contact5Email | Contact6Common |
      | user1Name | user2Name           | user2ABName    | user2Email    | user3Name                 | user3ABName    | user3PhoneNumber    | user4Name                  | user4UniqueUsername    | user5Name                 | user6Name                | user6Email    | user7Name      |

  @C352039 @staging @fastLogin
  Scenario Outline: Verify impossibility to save incorrect username
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    Given I select settings item @name
    When I enter "<Empty>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    And I enter "<MinChars>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    And I attempt to enter over max allowed <MaxChars> chars as name on Unique Username page
    Then I see that name length is less than <MaxChars> chars on Unique Username page
    And I type unique usernames from the data table and verify they cannot be committed on Unique Username page
      | Charset      | Chars  |
      | Cyrillic     | МоёИмя |
      | Arabic       | اسمي   |
      | Chinese      | 我的名字|
      | SpecialChars | %^&@#$ |

    Examples:
      | Name      | Empty | MinChars | MaxChars |
      | user1Name | ""    | 1        | 22       |

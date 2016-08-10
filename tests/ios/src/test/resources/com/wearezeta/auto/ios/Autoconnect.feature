Feature: Autoconnect

  @torun @C2034 @C2035 @addressbookStart
  Scenario Outline: Verify autoconnect users by direct match phone numbers
    Given There are 2 users
    Given I quit Wire
    Given I install Addressbook helper app
    Given I launch Addressbook helper app
    Given I delete all contacts from addressbook
    Given I add name <Contact1> and phone <CPhone> with prefix <PhonePrefix> to Address Book
    Given I add name <Contact2> and phone <C2Phone> with prefix <PhonePrefix> to Address Book
    Given I relaunch Wire
    Given I see sign in screen
    When I enter phone number for <Name>
    And I enter activation code
    And I accept terms of service
    And I input name <Name> and hit Enter
    And I press Keep This One button
    And I tap Share Contacts button on Share Contacts overlay
    Then I see conversation <Contact1> in conversations list
    And I see conversation <Contact2> in conversations list

    Examples:
      | Contact1  | Contact2   | CPhone     | C2Phone    |PhonePrefix  | Name      |
      | user1Name | user2Name  | user1Phone | user2Phone | +0          | user3Name |

  @C202304 @noAcceptAlert
  Scenario Outline: Verify autoconnect users by direct match phone numbers - delayed
    Given There are 3 user where <Name> is me
    Given I sign in using my email or phone number
    Given User Myself has phone numbers <PhonePrefix><APhone>,<PhonePrefix><A2Phone> in address book
    Given I see conversations list
    Then I see conversation <AName> in conversations list
    And I see conversation <A2Name> in conversations list

    Examples:
      | Name      | APhone     | PhonePrefix | A2Phone    | AName     | A2Name    |
      | user1Name | user2Phone | +0          | user3Phone | user2Name | user3Name |

  @C202303
  Scenario Outline: Verify direct matching email - delayed
    Given There are 2 users where <UserA> is me
    Given I sign in using my email
    Given User <UserB> has email <UserA> in address book
    When I open search UI
    And I wait for 10 seconds
    And I input in People picker search field first 5 letters of user name <UserB>
    Then I see the conversation "<UserB>" exists in Search results

    Examples:
      | UserA     | UserB     |
      | user1Name | user2Name |

  @CTODO
  Scenario Outline: Verify direct matching of emails
    Given There are 2 users where <Name> is me
    Given I install Addressbook helper app
    Given I launch Addressbook helper app
    Given I delete all contacts from addressbook
    Given I add name <AName> and email <AEmail> to Address Book
    Given I see sign in screen
    When I enter phone number for <Name>
    And I enter activation code
    And I accept terms of service
    And I input name <Name> and hit Enter
    And I press Keep This One button
    And I tap Share Contacts button on Share Contacts overlay
    And I see conversations list
    When I open search UI
    And I input in People picker search field first 5 letters of user name <AName>
    Then I see the conversation "<AName>" exists in Search results

    Examples:
      | Name      | AName     | AEmail     |
      | user1Name | user2Name | user2Email |

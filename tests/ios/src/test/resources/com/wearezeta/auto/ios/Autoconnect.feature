Feature: Autoconnect

  @C2034 @C2035 @addressbookStart
  Scenario Outline: Verify autoconnect users by direct match phone numbers
    Given There are 2 users
    Given I quit Wire
    Given I install Addressbook helper app
    Given I launch Addressbook helper app
    Given I delete all contacts from addressbook
    Given I add name <Contact1> and phone <CPhone> to Address Book
    Given I add name <Contact2> and phone <C2Phone> to Address Book
    Given I relaunch Wire
    Given I see sign in screen
    When I enter phone number for <Name>
    And I enter activation code
    And I accept terms of service
    And I input name <Name> and hit Enter
    And I tap Keep This One button
    And I tap Share Contacts button on Share Contacts overlay
    Then I see conversation <Contact1> in conversations list
    And I see conversation <Contact2> in conversations list

    Examples:
      | Contact1  | Contact2   | CPhone           | C2Phone          | Name      |
      | user1Name | user2Name  | user1PhoneNumber | user2PhoneNumber | user3Name |

  @C202304 @addressbookStart
  Scenario Outline: Verify autoconnect users by direct match phone numbers - delayed
    Given There are 3 user where <Name> is me
    Given I quit Wire
    Given I install Addressbook helper app
    Given I launch Addressbook helper app
    Given I delete all contacts from addressbook
    Given I add name <Contact1> and phone <CPhone> to Address Book
    Given I add name <Contact2> and phone <C2Phone> to Address Book
    Given I relaunch Wire
    Given I sign in using my email or phone number
    When I open search UI
    And I accept alert
    And I click clear button
    Then I see conversation <Contact1> in conversations list
    And I see conversation <Contact2> in conversations list

    Examples:
      | Contact1  | Contact2   | CPhone           | C2Phone          | Name      |
      | user3Name | user2Name  | user3PhoneNumber | user2PhoneNumber | user1Name |


  #@C202304 @noAcceptAlert
  #Scenario Outline: Verify autoconnect users by direct match phone numbers - delayed OLD TEST
    #Given There are 3 user where <Name> is me
    #Given I sign in using my email or phone number
    #Given User Myself has phone numbers <PhonePrefix><APhone>,<PhonePrefix><A2Phone> in address book
    #Given I see conversations list
    #Then I see conversation <AName> in conversations list
    #And I see conversation <A2Name> in conversations list

    #Examples:
      #| Name      | APhone     | PhonePrefix | A2Phone    | AName     | A2Name    |
      #| user1Name | user2Phone | +0          | user3Phone | user2Name | user3Name |

  #@C202303
  #Scenario Outline: Verify direct matching email - delayed OLD TEST
    #Given There are 2 users where <UserA> is me
    #Given I sign in using my email
    #Given User <UserB> has email <UserA> in address book
    #When I open search UI
    #And I wait for 10 seconds
    #And I input in People picker search field first 5 letters of user name <UserB>
    #Then I see the conversation "<UserB>" exists in Search results

    #Examples:
      #| UserA     | UserB     |
      #| user1Name | user2Name |

  @torun @C202303 @addressbookStart
  Scenario Outline: Verify direct matching email - delayed
    Given There are 2 user where <Name> is me
    Given I quit Wire
    Given I install Addressbook helper app
    Given I launch Addressbook helper app
    Given I delete all contacts from addressbook
    Given I add name <Contact> and email <CEmail> to Address Book
    Given I relaunch Wire
    Given I sign in using my email or phone number
    And I wait for 60 seconds
    When I open search UI
    And I accept alert
    And I wait for 120 seconds
    And I input in People picker search field first 1 letters of user name <Contact>
    Then I see the conversation "<Contact>" exists in Search results

    Examples:
      | Contact   | CEmail     | Name      |
      | user2Name | user2Email | user1Name |

  @C206254 @addressbookStart
  Scenario Outline: Verify direct matching of emails
    Given There is 1 user
    Given I quit Wire
    Given I install Addressbook helper app
    Given I launch Addressbook helper app
    Given I delete all contacts from addressbook
    Given I add name <Contact> and email <CEmail> to Address Book
    Given I relaunch Wire
    Given I see sign in screen
    When I enter phone number for <Name>
    And I enter activation code
    And I accept terms of service
    And I input name <Name> and hit Enter
    And I tap Keep This One button
    And I tap Share Contacts button on Share Contacts overlay
    And I see conversations list
    When I open search UI
    And I wait for 5 seconds
    And I input in People picker search field first 4 letters of user name <Contact>
    Then I see the conversation "<Contact>" exists in Search results

    Examples:
      | Contact   | CEmail     | Name      |
      | user1Name | user1Email | user2Name |
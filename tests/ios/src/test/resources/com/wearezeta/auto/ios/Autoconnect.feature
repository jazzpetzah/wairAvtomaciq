Feature: Autoconnect

  #still needs the fix to upload +0 to BE, thats why no label to run yet, because it will fail
  @C2034 @addressbookStart
  Scenario Outline: Verify autoconnect users by direct match phone numbers
    Given There are 2 users
    Given I quit Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I delete all contacts from Address Book
    Given I add name <Contact1> and phone <ContactPhone> to Address Book
    Given I add name <Contact2> and phone <Contact2Phone> to Address Book
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
      | Contact1  | Contact2   | ContactPhone       | Contact2Phone    | Name      |
      | user1Name | user2Name  | user1PhoneNumber   | user2PhoneNumber | user3Name |

  #still needs the fix to upload +0 to BE, thats why no label to run yet, because it will fail
  @C202304 @addressbookStart
  Scenario Outline: Verify autoconnect users by direct match phone numbers - delayed
    Given There are 3 user where <Name> is me
    Given I quit Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I delete all contacts from Address Book
    Given I add name <Contact1> and phone <ContactPhone> to Address Book
    Given I add name <Contact2> and phone <Contact2Phone> to Address Book
    Given I relaunch Wire
    Given I sign in using my email or phone number
    #Wait to make sure user is in the backend
    And I wait until <Contact1> exists in backend search results
    And I wait until <Contact2> exists in backend search results
    When I open search UI
    And I click clear button
    Then I see conversation <Contact1> in conversations list
    And I see conversation <Contact2> in conversations list

    Examples:
      | Contact1  | Contact2   | ContactPhone     | Contact2Phone    | Name      |
      | user3Name | user2Name  | user3PhoneNumber | user2PhoneNumber | user1Name |

  @C202303 @staging @addressbookStart
  Scenario Outline: Verify direct matching email - delayed
    Given There are 2 user where <Name> is me
    Given I quit Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I delete all contacts from Address Book
    Given I add name <Contact> and email <ContactEmail> to Address Book
    Given I relaunch Wire
    Given I sign in using my email or phone number
    #Wait to make sure user is in the backend
    And I wait until <Contact> exists in backend search results
    When I open search UI
    #Wait to be sure the match happend on the backend
    And I wait until <Contact> is first search result on backend
    And I input in People picker search field first 1 letter of user name <Contact>
    Then I see the first item in Search result is <Contact>

    Examples:
      | Contact   | ContactEmail  | Name      |
      | user2Name | user2Email    | user1Name |

  @C206254 @staging @addressbookStart
  Scenario Outline: Verify direct matching of emails
    Given There is 1 user
    Given I quit Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I delete all contacts from Address Book
    Given I add name <Contact> and email <ContactEmail> to Address Book
    Given I relaunch Wire
    Given I see sign in screen
    When I enter phone number for <Name>
    And I enter activation code
    And I accept terms of service
    And I input name <Name> and hit Enter
    And I tap Keep This One button
    And I tap Share Contacts button on Share Contacts overlay
    And I see conversations list
    #Wait to make sure user is in the backend
    And I wait until <Contact> exists in backend search results
    When I open search UI
    #Wait to be sure the match happend on the backend
    And I wait until <Contact> is first search result on backend
    And I input in People picker search field first 1 letter of user name <Contact>
    Then I see the first item in Search result is <Contact>

    Examples:
      | Contact   | ContactEmail | Name      |
      | user1Name | user1Email   | user2Name |
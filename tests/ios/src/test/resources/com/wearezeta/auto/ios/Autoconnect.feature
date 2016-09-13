Feature: Autoconnect

  @C2034 @regression @addressbookStart
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
    And User <Name> is me
    Then I see conversation <Contact1> in conversations list
    And I see conversation <Contact2> in conversations list

    Examples:
      | Contact1  | Contact2  | ContactPhone     | Contact2Phone    | Name      |
      | user1Name | user2Name | user1PhoneNumber | user2PhoneNumber | user3Name |

  @C202304 @regression @addressbookStart
  Scenario Outline: Verify autoconnect users by direct match phone numbers - delayed
    Given There are 3 users where <Name> is me
    Given I quit Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I delete all contacts from Address Book
    Given I add name <Contact1> and phone <ContactPhone> to Address Book
    Given I add name <Contact2> and phone <Contact2Phone> to Address Book
    Given I relaunch Wire
    Given I sign in using my email or phone number
    And I wait until <Contact1> exists in backend search results
    And I wait until <Contact2> exists in backend search results
    When I open search UI
    And I click clear button
    Then I see conversation <Contact1> in conversations list
    And I see conversation <Contact2> in conversations list

    Examples:
      | Contact1  | Contact2  | ContactPhone     | Contact2Phone    | Name      |
      | user3Name | user2Name | user3PhoneNumber | user2PhoneNumber | user1Name |

  @C202303 @regression @addressbookStart
  Scenario Outline: Verify direct matching email - delayed
    Given There are 2 users where <Name> is me
    Given I quit Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I delete all contacts from Address Book
    Given I add name <Contact> and email <ContactEmail> to Address Book
    Given I relaunch Wire
    Given I sign in using my email or phone number
    And I wait until <Contact> exists in backend search results
    When I open search UI
    And I wait until <Contact> is first search result on backend
    And I input in People picker search field first 1 letter of user name <Contact>
    Then I see the first item in Search result is <Contact>

    Examples:
      | Contact   | ContactEmail | Name      |
      | user2Name | user2Email   | user1Name |

  @C206254 @regression @addressbookStart
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
    And User <Name> is me
    And I see conversations list
    And I wait until <Contact> exists in backend search results
    When I open search UI
    And I wait until <Contact> is first search result on backend
    And I input in People picker search field first 1 letter of user name <Contact>
    Then I see the first item in Search result is <Contact>

    Examples:
      | Contact   | ContactEmail | Name      |
      | user1Name | user1Email   | user2Name |

  @C226448 @addressbookStart @staging
  Scenario Outline: Verify Address Book is uploaded in batches
    Given There is 1 user where <Name> is me
    Given I quit Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I delete all contacts from Address Book
    Given I add 3000 users to Address Book
    Given I read list of contacts in Address Book
    Given I separate list of contacts into 3 chunks
    Given I pick 1 random contact of chunk 1 to register at BE
    Given I pick 1 random contact of chunk 2 to register at BE
    Given I pick 1 random contact of chunk 3 to register at BE
    Given I relaunch Wire
    Given I sign in using my email or phone number
    When I open search UI
    And I click clear button
    Then I see 1st autoconnection in conversations list
    Given I quit Wire
    Given I relaunch Wire
    Then I see 2nd autoconnection in conversations list
    Given I quit Wire
    Given I relaunch Wire
    Then I see 3rd autoconnection in conversations list
    And I pick 1 random contact of chunk 1 to register at BE
    Given I quit Wire
    Given I relaunch Wire
    Then I see 4th autoconnection in conversations list

    Examples:
      | Name      |
      | user1Name |
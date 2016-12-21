Feature: Autoconnect

  @C2034 @regression @addressbookStart @forceReset
  Scenario Outline: Verify autoconnect users by direct match phone numbers
    Given There are 2 users
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I delete all contacts from Address Book
    Given I add name <Contact1> and phone <ContactPhone> to Address Book
    Given I add name <Contact2> and phone <Contact2Phone> to Address Book
    Given I restore Wire
    Given I see sign in screen
    When I enter phone number for <Name>
    And I enter activation code
    And I accept terms of service
    And I input name <Name> and commit it
    And I accept alert
    And I tap Keep This One button
    # Wait for sync
    And I wait for 3 seconds
    And I accept alert if visible
    And I tap Share Contacts button on Share Contacts overlay
    And User <Name> is me
    And User Myself sets the unique username
    And I accept alert if visible
    And I dismiss settings warning if visible
    Then I see conversation <Contact1> in conversations list
    And I see conversation <Contact2> in conversations list

    Examples:
      | Contact1  | Contact2  | ContactPhone     | Contact2Phone    | Name      |
      | user1Name | user2Name | user1PhoneNumber | user2PhoneNumber | user3Name |

  @C202304 @regression @addressbookStart @forceReset
  Scenario Outline: Verify autoconnect users by direct match phone numbers - delayed
    Given There are 3 users where <Name> is me
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I delete all contacts from Address Book
    Given I add name <Contact1> and phone <ContactPhone> to Address Book
    Given I add name <Contact2> and phone <Contact2Phone> to Address Book
    Given I restore Wire
    Given I sign in using my email or phone number
    And I wait until <Contact1> exists in backend search results
    And I wait until <Contact2> exists in backend search results
    When I open search UI
    And I accept alert
    And I tap X button on Search UI page
    # Let it sync
    And I wait for 5 seconds
    Then I see conversation <Contact1> in conversations list
    And I see conversation <Contact2> in conversations list

    Examples:
      | Contact1  | Contact2  | ContactPhone     | Contact2Phone    | Name      |
      | user3Name | user2Name | user3PhoneNumber | user2PhoneNumber | user1Name |

  @C202303 @regression @addressbookStart @forceReset
  Scenario Outline: Verify direct matching email - delayed
    Given There are 2 users where <Name> is me
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I delete all contacts from Address Book
    Given I add name <Contact> and email <ContactEmail> to Address Book
    Given I restore Wire
    Given I sign in using my email or phone number
    And I wait until <Contact> exists in backend search results
    When I open search UI
    And I accept alert
    And I tap input field on Search UI page
    And I type first 1 letter of user name "<Contact>" into Search UI input field
    Then I see the first item in Search result is <Contact>

    Examples:
      | Contact   | ContactEmail | Name      |
      | user2Name | user2Email   | user1Name |

  @C206254 @regression @addressbookStart @forceReset
  Scenario Outline: Verify direct matching of emails
    Given There is 1 user
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I delete all contacts from Address Book
    Given I add name <Contact> and email <ContactEmail> to Address Book
    Given I restore Wire
    Given I see sign in screen
    When I enter phone number for <Name>
    And I enter activation code
    And I accept terms of service
    And I input name <Name> and commit it
    And I accept alert
    And I tap Keep This One button
    # Wait for self picture to be applied
    And I wait for 3 seconds
    And I tap Share Contacts button on Share Contacts overlay
    And User <Name> is me
    And User Myself sets the unique username
    And I accept alert
    And I dismiss settings warning if visible
    And I see conversations list
    And I wait until <Contact> exists in backend search results
    When I open search UI
    And I tap input field on Search UI page
    And I type first 1 letter of user name "<Contact>" into Search UI input field
    Then I see the first item in Search result is <Contact>

    Examples:
      | Contact   | ContactEmail | Name      |
      | user1Name | user1Email   | user2Name |

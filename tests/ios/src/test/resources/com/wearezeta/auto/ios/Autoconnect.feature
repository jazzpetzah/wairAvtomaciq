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
    And I input name <Name> and hit Enter
    And I accept alert
    And I tap Keep This One button
    And I tap Share Contacts button on Share Contacts overlay
    And I accept alert
    And User <Name> is me
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
    And I tap X button in People Picker input field
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
    And I wait until <Contact> is first search result on backend
    And I tap input field on Search UI page
    And I input in People picker search field first 1 letter of user name <Contact>
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
    And I input name <Name> and hit Enter
    And I accept alert
    And I tap Keep This One button
    And I tap Share Contacts button on Share Contacts overlay
    And I accept alert
    And User <Name> is me
    And I see conversations list
    And I wait until <Contact> exists in backend search results
    When I open search UI
    And I wait until <Contact> is first search result on backend
    And I tap input field on Search UI page
    And I input in People picker search field first 1 letter of user name <Contact>
    Then I see the first item in Search result is <Contact>

    Examples:
      | Contact   | ContactEmail | Name      |
      | user1Name | user1Email   | user2Name |

  @C79 @addressbookStart @regression @forceReset
  Scenario Outline: Verify name from the address book is shown as a subtitle
    Given There are 2 users where <Name> is me
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I delete all contacts from Address Book
    Given I add name <Contact> and phone <ContactPhone> to Address Book
    Given I restore Wire
    Given I remember the name of user <Contact> in Address Book
    Given User <Contact> changes name to <NewName>
    Given I sign in using my email or phone number
    Given I wait until <Contact> exists in backend search results
    When I open search UI
    And I accept alert
    And I tap X button in People Picker input field
    And I tap on contact name <Contact>
    And I open conversation details
    Then I verify username <Contact> on Other User Profile page is displayed
    And I verify the previously remembered user name from Address Book is displayed on Other User Profile page

    Examples:
      | Name      | Contact   | ContactPhone     | NewName |
      | user1Name | user2Name | user2PhoneNumber | Beyonce |
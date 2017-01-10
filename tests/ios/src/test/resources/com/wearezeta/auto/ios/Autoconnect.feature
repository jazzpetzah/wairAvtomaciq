Feature: Autoconnect

  @C2034 @regression @addressbookStart @forceReset
  Scenario Outline: Verify autoconnect users by direct match phone numbers
    Given There are 2 users
    Given Users <Contact1>,<Contact2> upload own details
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
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
    Given Users <Name>,<Contact1>,<Contact2> upload own details
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
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

  @C226448 @addressbookStart @regression
  Scenario Outline: (MEC-1557) Verify Address Book is uploaded in batches
    Given There is 1 user where <Name> is me
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I delete all contacts from Address Book
    Given I add <NumberOfUsers> users to Address Book
    Given I read list of contacts in Address Book
    #Given I separate list of contacts into <NumberOfChunks> chunks
    Given I register <NumberOfUsers> at BE
    #Given I pick 10 random contact from chunk 1 to register at BE
    #Given I pick 1 random contact from chunk 2 to register at BE
    #Given I pick 1 random contact from chunk 3 to register at BE
    Given I restore Wire
    Given I sign in using my email or phone number
    When I open search UI
    And I accept alert
    And I tap X button on Search UI page
    Then <Name> has <NumberOfUsers> in conversation list
    #Then I see 1st autoconnection in conversations list
    #When I quit Wire
    #And I relaunch Wire
    #Then I see 2nd autoconnection in conversations list
    #When I quit Wire
    #And I relaunch Wire
    #Then I see 3rd autoconnection in conversations list
    #When I pick 1 random contact from chunk 1 to register at BE
    #And I quit Wire
    #And I relaunch Wire
    #Then I see 4th autoconnection in conversations list
    #Then I see 5th autoconnection in conversations list
    #Then I see 6th autoconnection in conversations list
    #Then I see 7th autoconnection in conversations list
    #Then I see 8th autoconnection in conversations list
    #Then I see 9th autoconnection in conversations list
    #Then I see 10th autoconnection in conversations list

    Examples:
      | Name      | NumberOfUsers | NumberOfChunks |
      | user1Name | 300           | 1              |
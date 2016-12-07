Feature: Unique Username

  @C352018 @staging
  Scenario Outline: Verify I see Unique User Name and AB name within Incoming Request and Conversation View in different condition
    Given I delete all contacts from Address Book
    Given There are 5 users where <Name> is me
    Given I add <ContactInABEmail> having custom name "<ABNameEmail>" into Address Book with email
    Given I add <ContactInABPhone> having custom name "<ABNamePhone>" into Address Book with phone
    Given I add <ContactInABSameName> into Address Book with phone and email
    Given User <ContactInABEmail> sets the unique username
    Given User <ContactInABPhone> sets the unique username
    Given User <ContactInABSameName> sets the unique username
    Given User <Connected> sets the unique username
    Given Myself is connected to <Connected>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list
    # Connect in AB with Email
    When <ContactInABEmail> sent connection request to me
    And I tap on conversation name <WaitingMess1>
    Then I see unique user name of user <ContactInABEmail> on Single pending incoming connection page
    And I see user info "<ABNameEmail> in Address Book" on Single pending incoming connection page
    When I tap connect button for user <ContactInABEmail> on Single pending incoming connection page
    # Connect in AB with Phone
    When <ContactInABPhone> sent connection request to me
    And I tap on conversation name <WaitingMess1>
    And I see unique user name of user <ContactInABPhone> on Single pending incoming connection page
    # And I see user info "<ABNamePhone> in Address Book" on Single pending incoming connection page
    And I tap connect button for user <ContactInABPhone> on Single pending incoming connection page
    # Connect in AB with Same Name
    When <ContactInABSameName> sent connection request to me
    And I tap on conversation name <WaitingMess1>
    Then I see unique user name of user <ContactInABSameName> on Single pending incoming connection page
    And I see user info "in Address Book" on Single pending incoming connection page
    And I tap connect button for user <ContactInABSameName> on Single pending incoming connection page

    Examples:
      | Name      | ContactInABEmail | ContactInABPhone | ContactInABSameName | Connected | WaitingMess1     | ABNameEmail | ABNamePhone |
      | user1Name | user2Name        | user3Name        | user4Name           | user5Name | 1 person waiting | Email       | Phone       |

  @C352019 @staging
  Scenario Outline: Verify number of common friends is shown on the incoming connection request
    Given I delete all contacts from Address Book
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given Myself is connected to <Contact2>,<Contact3>
    Given <Contact1> sent connection request to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with name <WaitingMess1>
    When I tap on conversation name <WaitingMess1>
    And Myself wait until 2 common contacts with <Contact1> exists in backend
    #TODO: Verify common contact2 and contact3 are show

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | WaitingMess1     |
      | user1Name | user2Name | user3Name | user4Name | 1 person waiting |


  @C352020 @staging
  Scenario Outline: Verify I see Unique User Name and AB name within outgoing Request in different condition
    Given I delete all contacts from Address Book
    Given There are 4 users where <Name> is me
    Given I add <ContactInABEmail> having custom name "Email" into Address Book with email
    Given I add <ContactInABPhone> having custom name "Phone" into Address Book with phone
    Given I add <ContactInABSameName> into Address Book with phone and email
    Given User <ContactInABEmail> updates the unique user name to "<UniqueName1>"
    Given User <ContactInABPhone> updates the unique user name to "<UniqueName2>"
    Given User <ContactInABSameName> updates the unique user name to "<UniqueName3>"
    Given Myself sent connection request to <ContactInABEmail>
    Given Myself sent connection request to <ContactInABPhone>
    Given Myself sent connection request to <ContactInABSameName>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <ContactInABEmail>
    Then I see user name of user <ContactInABEmail> on Single pending outgoing connection page
    And I see unique user name of user <ContactInABEmail> on Single pending outgoing connection page
    # TODO I see AB name
    And I tap Back button
    # Positive test: verify that contact with phone number in AB has it's unique username visible
    When I tap on conversation name <ContactInABPhone>
    Then I see user name of user <ContactInABPhone> on Single pending outgoing connection page
    And I see unique user name of user <ContactInABPhone> on Single pending outgoing connection page
    # TODO I see AB name
    And I tap Back button
    # Negative test: verify that contact with same name in AB has it's unique username visible
    When I tap on conversation name <ContactInABSameName>
    Then I see user name of user <ContactInABSameName> on Single pending outgoing connection page
    And I see unique user name of user <ContactInABSameName> on Single pending outgoing connection page
    # TODO I do not see AB name

    Examples:
      | Name      | ContactInABEmail | ContactInABPhone | ContactInABSameName | UniqueName1         | UniqueName2         | UniqueName3         |
      | user1Name | user2Name        | user3Name        | user4Name           | user2UniqueUsername | user3UniqueUsername | user4UniqueUsername |

  @C352021 @staging
  Scenario Outline: Verify number of common friends is shown on the outgoing connection request
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself sent connection request to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given Myself wait until <CommonContact> common contacts with <Contact1> exists in backend
    When I open Search UI
    And I type user name "<Contact1>" in search field
    And I tap on user name found on Search page <Contact1>
    Then I see user info "<CommonContact> people in common" on Single unconnected user details page

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | CommonContact |
      | user1Name | user2Name | user3Name | user4Name | 2             |

  @C352072 @staging
  Scenario Outline: Verify search works with username and doesn't work with email
    Given There are 2 users where <Name> is me
    When I sign in using my email or phone number
    And I accept First Time overlay as soon as it is visible
    Then I see Conversations list
     # Negative test: searching by email
    When I open Search UI
    And I type user email "<Contact2Email>" in search field
    Then I see user <Contact2InABEmail> in Search result list
     # It will be Negative soon, I hope
     # Then I do not see user <Contact2InABEmail> in Search result list
    And I tap Clear button
     # Positive test: searching by name
    When I open Search UI
    And I type user name "<Contact2InABEmail>" in search field
    Then I see user <Contact2InABEmail> in Search result list
    And I tap on user name found on Search page <Contact2InABEmail>
    Then I see connect to <Contact2InABEmail> dialog
    When I tap Connect button on connect to page
    Then I see Search page
    When I tap Clear button
    Then I see Conversations list with name <Contact2InABEmail>

    Examples:
      | Name      | Contact2InABEmail | Contact2Email |
      | user1Name | user2Name         | user2Email    |

  @C352075 @staging
  Scenario Outline: Verify search shows correct user info for unconnected user
    Given There are 9 users where <Name> is me
    Given I add <Contact1InABWithCF> having custom name "<Contact1NameInAB>" into Address Book with phone
    Given I add <Contact2InABWoCF> having custom name "<Contact2NameInAB>" into Address Book with phone
    Given I add <Contact5SameName> into Address Book with phone
    Given Myself is connected to <CF1>,<CF2>,<CF3>
    Given <Contact1InABWithCF> is connected to <CF1>,<CF2>
    Given <Contact3WithCF> is connected to <CF2>,<CF3>
    Given <Contact5SameName> is connected to <CF3>
    When I sign in using my email or phone number
    And I accept First Time overlay as soon as it is visible
    # Verify search for unconnected user, in address book, with the common friends
    Then I see Conversations list
    When I open Search UI
    Then I verify results in search page, according to datatable
      | Name                 | ABName             | CommonFriends |
      | <Contact1InABWithCF> | <Contact1NameInAB> | 2             |
      | <Contact2InABWoCF>   | <Contact2NameInAB> | 0             |
      | <Contact3WithCF>     |                    | 2             |
      | <Contact4WoCF>       |                    | 0             |
      | <Contact5SameName>   | <Contact5SameName> | 1             |

    Examples:
      | Name      | Contact1InABWithCF | Contact1NameInAB | Contact2InABWoCF | Contact2NameInAB | Contact3WithCF | Contact4WoCF | Contact5SameName | CF1       | CF2       | CF3       |
      | user1Name | user2Name          | user2ABName      | user3Name        | user3ABName      | user4Name      | user5Name    | user6Name        | user7Name | user8Name | user9Name |

  @C352086 @staging
  Scenario Outline: Verify I can see unregistered person from AB in Contacts UI
    Given There are 1 users where <Name> is me
    Given I add <Contact1InABWithCF> having custom name "<Contact1NameInAB>" into Address Book with email
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I open Search UI
    Then I see user <Contact1NameInAB> in Contact list

    Examples:
      | Name      | Contact1InABWithCF | Contact1NameInAB |
      | user1Name | user2Name          | user2ABName      |

  @C352076 @staging
  Scenario Outline: Verify search shows correct user info for connected user
    Given There are 9 users where <Name> is me
    Given I add <Contact1InABWithCF> having custom name "<Contact1NameInAB>" into Address Book with phone
    Given I add <Contact2InABWoCF> having custom name "<Contact2NameInAB>" into Address Book with phone
    Given I add <Contact5SameName> into Address Book with phone
    Given Myself is connected to <Contact1InABWithCF>,<Contact2InABWoCF>,<Contact3WithCF>,<Contact4WoCF>,<Contact5SameName>,<CF1>,<CF2>,<CF3>
    Given <Contact1InABWithCF> is connected to <CF1>,<CF2>
    Given <Contact3WithCF> is connected to <CF2>,<CF3>
    Given <Contact5SameName> is connected to <CF3>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    # Verify search for unconnected user, in address book, with the common friends
    Given I see Conversations list
    When I open Search UI
    Then I verify results in search page, according to datatable
      | Name                 | ABName             | CommonFriends |
      | <Contact1InABWithCF> | <Contact1NameInAB> | 2             |
      | <Contact2InABWoCF>   | <Contact2NameInAB> | 0             |
      | <Contact3WithCF>     |                    | 2             |
      | <Contact4WoCF>       |                    | 0             |
      | <Contact5SameName>   | <Contact5SameName> | 1             |

    Examples:
      | Name      | Contact1InABWithCF | Contact1NameInAB | Contact2InABWoCF | Contact2NameInAB | Contact3WithCF | Contact4WoCF | Contact5SameName | CF1       | CF2       | CF3       |
      | user1Name | user2Name          | user2ABName      | user3Name        | user3ABName      | user4Name      | user5Name    | user6Name        | user7Name | user8Name | user9Name |
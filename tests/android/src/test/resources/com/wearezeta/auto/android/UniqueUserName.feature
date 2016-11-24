Feature: Unique Username

  @C352018 @staging
  Scenario Outline: Verify I see Unique User Name and AB name within Incoming Request in different condition
    Given I delete all contacts from Address Book
    Given There are 4 users where <Name> is me
    Given I add <ContactInABEmail> having custom name "Email" into Address Book with email
    Given <ContactInABEmail> sent connection request to me
    Given I add <ContactInABPhone> having custom name "Phone" into Address Book with phone
    Given <ContactInABPhone> sent connection request to me
    Given I add <ContactInABSameName> into Address Book with phone and email
    Given <ContactInABSameName> sent connection request to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I see Conversations list with name <WaitingMess1>
    When I tap on conversation name <WaitingMess1>
    And I scroll to inbox contact <ContactInABEmail>
    #TODO: Check Unique Name + AB Name are visible
    And I scroll to inbox contact <ContactInABPhone>
    #TODO: Check Unique Name + AB  are visible
    And I scroll to inbox contact <ContactInABSameName>
    #TODO: Check Unique Name is visible, but AB name is invisible

    Examples:
      | ContactInABEmail | Name      | ContactInABPhone | ContactInABSameName | WaitingMess1     |
      | user1Name        | user2Name | user3Name        | user4Name           | 3 people waiting |

  @C352019 @staging
  Scenario Outline: Verify number of common friends is shown on the incoming connection request
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given Myself is connected to <Contact2>,<Contact3>
    Given <Contact1> sent connection request to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I see Conversations list with name <WaitingMess1>
    When I tap on conversation name <WaitingMess1>
    #TODO: Verify common contact2 and contact3 are show

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | WaitingMess1     |
      | user1Name | user2Name | user3Name | user4Name | 1 person waiting |


  @C352020 @staging
  Scenario Outline: Verify I see Unique User Name and AB name within outcoming Request in different condition
    Given I delete all contacts from Address Book
    Given There are 4 users where <Name> is me
    Given I add <Contact2InABEmail> having custom name "Email" into Address Book with email
    Given I add <Contact3InABPhone> having custom name "Phone" into Address Book with phone
    Given I add <Contact4InABSameName> into Address Book with phone and email
    Given Myself sent connection request to <Contact2InABEmail>
    Given Myself sent connection request to <Contact3InABPhone>
    Given Myself sent connection request to <Contact4InABSameName>
    When I sign in using my email or phone number
    And I accept First Time overlay as soon as it is visible
    And I see Conversations list with name <Contact2InABEmail>
    # Positive test: verify that contact with email in AB has it's unique username visible
    When I tap on conversation name <Contact2InABEmail>
    Then I see outgoing pending connection to <Contact2InABEmail>
    And I see unique username <Contact2InABEmail> on Pending outgoing connection page
    # TODO I see AB name
    And I tap Back button
    # Positive test: verify that contact with phone number in AB has it's unique username visible
    When I tap on conversation name <Contact3InABPhone>
    Then I see outgoing pending connection to <Contact3InABPhone>
    And I see unique username <Contact3InABPhone> on Pending outgoing connection page
    # TODO I see AB name
    And I tap Back button
    # Negative test: verify that contact with same name in AB has it's unique username visible
    When I tap on conversation name <Contact4InABSameName>
    Then I see outgoing pending connection to <Contact4InABSameName>
    And I see unique username <Contact4InABSameName> on Pending outgoing connection page
    # TODO I do not see AB name

    Examples:
      | Name      | Contact2InABEmail | Contact3InABPhone | Contact4InABSameName |
      | user1Name | user2Name         | user3Name         | user4Name            |

  @C352021 @staging
  Scenario Outline: Verify number of common friends is shown on the outcoming connection request
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself sent connection request to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Then I see Conversations list with name <Contact2>
    Then I see Conversations list with name <Contact3>
    When I tap on conversation name <Contact1>
    #TODO: Verify common contact2 and contact3 are show

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |
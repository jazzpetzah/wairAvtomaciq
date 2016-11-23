Feature: Unique User Name

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
    Given I see Conversations list with name <WaitingMess1>
    When I tap on conversation name <WaitingMess1>
    And I scroll to inbox contact <ContactInABEmail>
    #TODO: Check Unique Name + AB Name are visible
    When I scroll to inbox contact <ContactInABPhone>
    #TODO: Check Unique Name + AB  are visible
    When I scroll to inbox contact <ContactInABSameName>
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
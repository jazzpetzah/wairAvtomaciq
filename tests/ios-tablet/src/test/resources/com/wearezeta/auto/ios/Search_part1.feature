Feature: Search

  @C2783 @regression @rc @id2147
  Scenario Outline: Verify search by email [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I input in People picker search field user email <ContactEmail>
    Then I see user <ContactName> found on People picker page

    Examples: 
      | Name      | ContactEmail | ContactName |
      | user1Name | user2Email   | user2Name   |

  @C2788 @regression @id2926
  Scenario Outline: Verify search by email [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I input in People picker search field user email <ContactEmail>
    Then I see user <ContactName> found on People picker page

    Examples: 
      | Name      | ContactEmail | ContactName |
      | user1Name | user2Email   | user2Name   |

  @C2784 @regression @rc @id2148
  Scenario Outline: Verify search by name [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2789 @regression @id2927
  Scenario Outline: Verify search by name [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2838 @regression @id2656
  Scenario Outline: Start 1:1 chat with users from Top Connections [PORTRAIT]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 1 top connections
    #And I click Go button to create 1:1 conversation
    And I click open conversation button on People picker page
    And I wait for 2 seconds
    And I see dialog page

    Examples: 
      | Name      | UserCount |
      | user1Name | 2         |

  @C2839 @regression @id2929
  Scenario Outline: Start 1:1 chat with users from Top Connections [LANDSCAPE]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 1 top connections
    #And I click Go button to create 1:1 conversation
    And I click open conversation button on People picker page
    And I wait for 2 seconds
    And I see dialog page

    Examples: 
      | Name      | UserCount |
      | user1Name | 2         |

  @C2835 @regression @rc @id2550
  Scenario Outline: Start group chat with users from Top Connections [PORTRAIT]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 2 top connections
    And I click Create Conversation button on People picker page
    And I wait for 2 seconds
    And I open group conversation details
    And I change group conversation name to <ConvoName>
    And I dismiss popover on iPad
    And I navigate back to conversations list
    Then I see first item in contact list named <ConvoName>

    Examples: 
      | Name      | ConvoName    | UserCount |
      | user1Name | TopGroupTest | 3         |

  @C2840 @regression @id2930
  Scenario Outline: Start group chat with users from Top Connections [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on first 2 top connections
    #And I click hide keyboard button
    And I click Create Conversation button on People picker page
    And I wait for 2 seconds
    And I open group conversation details
    And I change group conversation name to <ConvoName>
    And I dismiss popover on iPad
    And I see first item in contact list named <ConvoName>

    Examples: 
      | Name      | ConvoName    |
      | user1Name | TopGroupTest |

  @C2456 @C2778 @regression @rc @id2342 @id1456
  Scenario Outline: Verify you can unblock someone from search list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I dont see conversation <Contact> in contact list
    And I wait until <Contact> exists in backend search results
    And I open search by taping on it
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I unblock user on iPad
    And I type the default message and send it
    Then I see 1 default message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2790 @regression @id2931
  Scenario Outline: Verify you can unblock someone from search list [LANDSAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I dont see conversation <Contact> in contact list
    And I wait until <Contact> exists in backend search results
    And I open search by taping on it
    And I input in People picker search field user name <Contact>
    And I click hide keyboard button
    And I tap on conversation <Contact> in search result
    And I unblock user on iPad
    And I type the default message and send it
    Then I see 1 default message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2785 @regression @rc @id2149
  Scenario Outline: Verify search by second name (something after space) [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I wait until <LastName> exists in backend search results
    And I input in People picker search field user name <LastName>
    Then I see user <NewName> found on People picker page

    Examples: 
      | Name      | Contact   | NewName  | LastName |
      | user1Name | user2Name | NEW NAME | NAME     |

  @C2792 @regression @id2935
  Scenario Outline: Verify search by second name (something after space) [LANDSAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I wait until <LastName> exists in backend search results
    And I input in People picker search field user name <LastName>
    Then I see user <NewName> found on People picker page

    Examples: 
      | Name      | Contact   | NewName  | LastName |
      | user1Name | user2Name | NEW NAME | NAME     |

  @C2777 @regression @rc @id2150
  Scenario Outline: Verify search by part of the name [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I wait until <Contact> exists in backend search results
    And I input in People picker search field first 5 letters of user name <Contact>
    Then I see user <Contact> found on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2795 @regression @id2945
  Scenario Outline: Verify search by part of the name [LANDSAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I wait until <Contact> exists in backend search results
    And I input in People picker search field first 5 letters of user name <Contact>
    Then I see user <Contact> found on People picker page

    Examples: 
      | Name      | Contact   | 
      | user1Name | user2Name |

  @C2787 @regression @id2703
  Scenario Outline: Verify search is possible after selection users from Top People [PORTRAIT]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 3 top connections but not <Contact>
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I click space keyboard button
    Then I see that <Number> contacts are selected

    Examples: 
      | Name      | UserCount | Contact   | Number |
      | user1Name | 7         | user2Name | 4      |

  @C2793 @regression @id2936
  Scenario Outline: Verify search is possible after selection users from Top People [LANDSAPE]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until <Contact> exists in backend search results
    When I open search by taping on it
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 3 top connections but not <Contact>
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I click space keyboard button
    Then I see that <Number> contacts are selected

    Examples: 
      | Name      | UserCount | Contact   | Number |
      | user1Name | 7         | user2Name | 4      |
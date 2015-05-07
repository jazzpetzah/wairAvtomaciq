Feature: Search

  @id218 @regression
  Scenario Outline: I can do full name search for existing 1:1 non-archive
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    Then I see user <Contact>  in People picker

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id220 @regression
  Scenario Outline: I can do full name search for existing group convo non-archive
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <GroupChatName>
    Then I see group <GroupChatName>  in People picker

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName          |
      | user1Email | user1Password | user1Name | user3Name | user2Name | PeoplePicker GroupChat |

  @id223 @regression
  Scenario Outline: I can do partial name search for existing 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field part <Size> of user name to connect to <Contact>
    Then I see user <Contact>  in People picker

    Examples: 
      | Login      | Password      | Name      | Contact   | Size |
      | user1Email | user1Password | user1Name | user2Name | 12   |

  @id225 @regression
  Scenario Outline: I can do partial name search for existing group convo non-archive
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field part <Size> of user name to connect to <GroupChatName>
    Then I see group <GroupChatName>  in People picker

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName           | Size |
      | user1Email | user1Password | user1Name | user3Name | user2Name | PeoplePicker GroupChat1 | 5    |

  @id327 @smoke
  Scenario Outline: Open/Close People picker
    Given There is 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I press Clear button
    Then Contact list appears

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id1494 @regression
  Scenario Outline: Verify possibility of invitation accepting
    Given There is 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I minimize the application
    Then I connect using invitation link from <Contact2>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @id1517 @regression 
  Scenario Outline: Verify you can send an invite
    Given There is 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I tap on Send an invitation
    And I tap on Gmail link
    Then mail subject is <Subject>
    And mail content contains my <Login>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Subject               |
      | user1Email | user1Password | user1Name | user2Name | Connect to me on Wire |
      
  @id2214 @staging
  Scenario Outline: I can dismiss PYMK by Hide button
    Given I see sign in screen
    Given I press Join button
    Given I press Camera button twice
    Given I confirm selection
    Given I enter name <Name>
    Given I enter email <Email>
    Given I enter password <Password>
    Given I submit registration data
    Given I see confirmation page
    Given I verify registration address
    When I wait for PYMK for 30 secs
    And I hide keyboard
    And I swipe on random connect
    And I click on PYMK hide button
    Then I do not see random connect

    Examples: 
      | Email      | Password      | Name      | Contact1  |
      | user1Email | user1Password | user1Name | user2Name |

  @id2213 @staging
  Scenario Outline: I can dismiss PYMK by swipe
    Given I see sign in screen
    Given I press Join button
    Given I press Camera button twice
    Given I confirm selection
    Given I enter name <Name>
    Given I enter email <Email>
    Given I enter password <Password>
    Given I submit registration data
    Given I see confirmation page
    Given I verify registration address
    When I wait for PYMK for 30 secs
    And I hide keyboard
    And I swipe on random connect
    And I hide random connect by swipe
    Then I do not see random connect

    Examples: 
      | Email      | Password      | Name      | Contact1  |
      | user1Email | user1Password | user1Name | user2Name |
      
  @id319 @regression
  Scenario Outline: I can create group chat from Search
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact1>
    And I tap on user name found on People picker page <Contact1>
    And I add in search field user name to connect to <Contact2>
    And I tap on user name found on People picker page <Contact2>
    #And I press back button
    And I tap on create conversation
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName           |
      | user1Email | user1Password | user1Name | user3Name | user2Name | PeoplePicker GroupChat2 |

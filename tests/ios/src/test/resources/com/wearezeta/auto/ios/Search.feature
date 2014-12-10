Feature: Search

  #Muted due to sync engine troubles(group chat is not created and app is closed after logout)
  #@mute @smoke @id600
  #Scenario Outline: Verify the new conversation is created on the other end (Search UI source)
    #Given I Sign in using login <Login> and password <Password>
    #And I see Contact list with my name <Name>
    #When I swipe down contact list
    #And I see People picker page
    #And I click clear button
    #And I swipe down contact list
    #And I see top people list on People picker page
    #And I tap on connected user <Contact1> on People picker page
    #And I tap on connected user <Contact2> on People picker page
    #And I see Create Conversation button on People picker page
    #And I click Create Conversation button  on People picker page
    #And I see group chat page with users <Contact1> <Contact2>
    #And I swipe right on group chat page
    #And I tap on my name <Name>
    #And I see Personal page
    #And I click on Settings button on personal page
    #And I click Sign out button from personal page
    #And I Sign in using login <Contact1> and password <Password>
    #And I see Personal page
    #And I swipe right on the personal page
    #And I see <Contact1> and <Contact2> chat in contact list
    #And I tap on my name <Name>
    #And I see Personal page
    #And I click on Settings button on personal page
    #And I click Sign out button from personal page
    #And I Sign in using login <Contact2> and password <Password>
    #And I see Personal page
    #And I swipe right on the personal page
    #And I see <Contact1> and <Contact2> chat in contact list

    #Examples: 
      #| Login   | Password    | Name    | Contact1    | Contact2    |
      #| aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |

  @id1150 @staging
  Scenario Outline: Start group chat with users from Top Connections
    Given I have at least 9 connections
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I swipe down contact list
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on 2 top connections
    And I click Create Conversation button  on People picker page
    And I swipe up on group chat page
    And I change conversation name to <ConvoName>
    And I exit the group info page
    And I return to the chat list
    And I see first item in contact list named <ConvoName>

    Examples: 
      | Login   | Password    | Name    | ConvoName    |
      | aqaUser | aqaPassword | aqaUser | TopGroupTest |

  @staging @id754
  Scenario Outline: Start 1:1 chat with users from Top Connections
    Given I have at least 9 connections
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I swipe down contact list
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on 1 top connections
    And I tap go to enter conversation
  
    Examples:
    |  Login	 | Password	    | Name	    |
    |  aqaUser	| aqaPassword	| aqaUser   |
      
      
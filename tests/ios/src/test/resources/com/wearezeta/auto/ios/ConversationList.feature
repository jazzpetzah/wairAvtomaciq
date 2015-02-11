Feature: Conversation List

  #Muted till new sync engine client stabilization. Mute buttons location is not possible.
  #@smoke @id338 
  #Scenario Outline: Mute conversation
    #Given I have 1 users and 1 contacts for 1 users
    #Given I Sign in using login <Login> and password <Password>
    #And I see Contact list with my name <Name>
    #When I swipe right on a <Contact1>
    #And I click mute conversation
    #Then Contact <Contact1> is muted
    #When I swipe right on a <Contact1>
    #And I click mute conversation
    #Then Contact <Contact1> is not muted

    #Examples: 
      #| Login   | Password    | Name    | Contact1    |
      #| aqaUser | aqaPassword | aqaUser | aqaContact1 |

@regression @id1333
  Scenario Outline: Unarchive conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    And I wait for 30 seconds
    Given I Sign in using login <Login> and password <Password>
	And I see Contact list with my name <Name>
	And I open archived conversations
	And I tap on contact name <ArchivedUser>
	And I navigate back to conversations view
	Then I see first item in contact list named <ArchivedUser>
	
    Examples: 
      | Login      | Password      | Name      | ArchivedUser   |
      | user1Email | user1Password | user1Name | user2Name      |
      
 @torun @staging @id1462
 Scenario Outline: Verify silence the conversation
 	Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change  name to <NewName>
    Given User <Name> change  accent color to <Color>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    
    Examples: 
      | Login      | Password      | Name      | Contact   | Color        | NewName |
      | user1Email | user1Password | user1Name | user2Name | BrightOrange | SILENCE |
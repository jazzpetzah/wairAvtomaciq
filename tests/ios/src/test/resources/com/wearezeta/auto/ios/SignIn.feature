Feature: Sign In

  @smoke @id340 @torun
  Scenario Outline: Sign in to ZClient
    Given I have 1 users and 0 contacts for 1 users
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Login button
    Then I see Contact list with my name <Name>

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |

  #Known issue is IOS-989, once it is fixed test should be updated
  #@staging @id524
  #Scenario Outline: I can change sign in user on iOS
    #Given I Sign in using login <Login> and password <Password>
    #And I see Contact list with my name <UserA>
    #And I tap on my name <UserA>
    #And I click on Settings button on personal page
    #And I click Sign out button from personal page
    #And I Sign in using login <UserB> and password <Password>
    #Then I see Personal page
    #And I see name <UserB> on Personal page
    #And I see email <UserB> on Personal page

    #Examples: 
      #| Login   | Password    | UserA   | UserB       |
      #| aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @regression @id1398 @torun
  Scenario Outline: Notification if SignIn credentials are wrong
  	Given I have 1 users and 0 contacts for 0 users
    Given I see sign in screen
    When I press Sign in button
    And I enter wrong email <WrongMail>
    Then I see error with email notification
    And I have entered login <Login>
    Then I see no error notification
    And I enter wrong password <WrongPassword>
    And I press Login button
    Then I see wrong credentials notification
    And I have entered password <Password>
    And I press Login button
    And I see Contact list with my name <Name>

    Examples: 
      | Login   | Password    | Name    | WrongMail  | WrongPassword |
      | aqaUser | aqaPassword | aqaUser | wrongwrong | wrong         |

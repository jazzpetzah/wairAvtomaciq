Feature: Sign In

  @smoke @id340
  Scenario Outline: Sign in to ZClient
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Login button
    Then I see Contact list with my name <Name>

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

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
  @regression @id1398
  Scenario Outline: Notification if SignIn credentials are wrong
    Given I see sign in screen
    When I press Sign in button
    And I enter wrong email <WrongMail>
    And I enter wrong password <WrongPassword>
    And I attempt to press Login button
    Then I see wrong credentials notification

    Examples: 
      | WrongMail  | WrongPassword |
      | wrongwrong | wrong         |

  @torun @staging @id1479
  Scenario Outline: Verify possibility of reseting password (welcome page)
    Given I see sign in screen
    And I press Sign in button
    And I click on Change Password button on SignIn
    Then I see reset password page
    And I change URL to staging
    And I type in email <Login> to change password
    And I press Change Password button in browser
    And I copy link from email and past it into Safari
    And I type in new password <NewPassword>
    And I press Change Password button in browser
    And I see password changed confirmation page
    And Return to Wire app
    And I Sign in using login <Login> and password <NewPassword>
    Then I see Contact list with my name <Name>
   

    Examples: 
      | Login    			  	      | Password      | Name      | NewPassword   |
      | smoketester.ios@wearezeta.com | user1Password | user1Name | aqa123456789  |

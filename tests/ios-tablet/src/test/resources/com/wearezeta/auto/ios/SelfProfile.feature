Feature: Self Profile

  @staging @id730
  Scenario Outline: Verify about screen contains all the required information [PORTRAIT]
    Given There are 1 users where <Name> is me
    Given User me change accent color to <Color>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on About button on personal page
    Then I see About page
    And I see that the About page is colored <Color>
    And I see WireWebsiteButton
    And I see TermsButton
    And I see PrivacyPolicyButton
    And I see BuildNumberText
    And I open PrivacyPolicyPage
    And I see PrivacyPolicyPage
    And I close legal page
    Then I see About page
    And I open TermsOfUsePage
    And I see TermsOfUsePage
    And I close legal page
    Then I see About page
    And I open WireWebsite
    Then I see WireWebsitePage
    
    Examples: 
      | Login      | Password      | Name      | Contact   | Color  |
      | user1Email | user1Password | user1Name | user2Name | Violet |

  @staging @id731
  Scenario Outline: Verify about screen contains all the required information [LANDSCAPE]
    Given There are 1 users where <Name> is me
    Given User me change accent color to <Color>
    Given I Sign in using login <Login> and password <Password>
    Given I rotate UI to landscape
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on About button on personal page
    Then I see About page
    And I see that the About page is colored <Color>
    And I see WireWebsiteButton
    And I see TermsButton
    And I see PrivacyPolicyButton
    And I see BuildNumberText
    And I open PrivacyPolicyPage
    And I see PrivacyPolicyPage
    And I close legal page
    Then I see About page
    And I open TermsOfUsePage
    And I see TermsOfUsePage
    And I close legal page
    Then I see About page
    And I open WireWebsite
    Then I see WireWebsitePage
    
    Examples: 
      | Login      | Password      | Name      | Contact   | Color  |
      | user1Email | user1Password | user1Name | user2Name | Violet |
      
  @staging @id1054 @id1060    
  Scenario Outline: I verify I am unable to enter a name using only spaces or more than 80 chars [PORTRAIT]
    Given There are 1 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I attempt to change name using only spaces
    And I see error message asking for more characters
    And I attempt to enter an 80 char name
    Then I verify my new name is only first 64 chars
    
    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
      
  @staging @id1054 @id1060
  Scenario Outline: I verify I am unable to enter a name using only spaces or more than 80 chars [LANDSCAPE]
    Given There are 1 users where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I attempt to change name using only spaces
    And I see error message asking for more characters
    And I attempt to enter an 80 char name
    Then I verify my new name is only first 64 chars
    
    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
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
      
  @torun @staging @id2586
  Scenario Outline: Self profile. Verify max limit in 64 chars [PORTRAIT]
    Given There are 1 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I change name <Name> to <NewUsername>
    Then I see my new name <NewUsername1>
    When I rotate UI to landscape
    Then I see Contact list with my name <NewUsername1>
    And I tap to edit my name
    And I change name <Name> to <NewUsername>
    Then I see my new name <NewUsername1>
    
    Examples: 
      | Login      | Password      | Name      | NewUsername                                                          | NewUsername1                                                     | Contact   |
      | user1Email | user1Password | user1Name | mynewusernamewithmorethan64characters3424245345345354353452345234535 | mynewusernamewithmorethan64characters342424534534535435345234523 | user2Name |


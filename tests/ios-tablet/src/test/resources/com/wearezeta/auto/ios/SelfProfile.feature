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
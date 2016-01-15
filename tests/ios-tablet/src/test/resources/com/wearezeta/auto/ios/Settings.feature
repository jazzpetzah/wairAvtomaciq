Feature: Settings

  @C2889 @regression @rc @id2587
  Scenario Outline: Verify user can access settings [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    Then I see settings page

    Examples: 
      | Name      |
      | user1Name |

  @C2905 @regression @id3021
  Scenario Outline: Verify user can access settings [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    Then I see settings page

    Examples: 
      | Name      |
      | user1Name |

  @C2890 @regression @id2588
  Scenario Outline: Attempt to open About screen in settings [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on About button on personal page
    Then I see About page
    And I close About page
    Then I see self profile page

    Examples: 
      | Name      |
      | user1Name |

  @C2906 @regression @id3022
  Scenario Outline: Attempt to open About screen in settings [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on About button on personal page
    Then I see About page
    And I close About page
    Then I see self profile page

    Examples: 
      | Name      |
      | user1Name |

  @C2893 @regression @id2591
  Scenario Outline: Verify reset password page is accessible from settings [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    And I click on Change Password button in Settings
    Then I see reset password page

    Examples: 
      | Name      |
      | user1Name |

  @C2907 @regression @id3023
  Scenario Outline: Verify reset password page is accessible from settings [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    And I click on Change Password button in Settings
    Then I see reset password page

    Examples: 
      | Name      |
      | user1Name |

  @C2898 @regression @id2596
  Scenario Outline: Verify default value for sound settings is all [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    When I tap on Sound Alerts
    And I see the Sound alerts page
    Then I verify that all is the default selected value

    Examples: 
      | Name      |
      | user1Name |

  @C2908 @regression @id3024
  Scenario Outline: Verify default value for sound settings is all [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    When I tap on Sound Alerts
    And I see the Sound alerts page
    Then I verify that all is the default selected value

    Examples: 
      | Name      |
      | user1Name |

  @C2904 @regression @id3019
  Scenario Outline: Verify you can access Help site within the app [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I tap on my name <Name>
    And I click on Settings button on personal page
    When I click on Help button from the options menu
    Then I see Support web page

    Examples: 
      | Name      |
      | user1Name |

  @C2909 @regression @id3025
  Scenario Outline: Verify you can access Help site within the app [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Help button from the options menu
    Then I see Support web page

    Examples: 
      | Name      |
      | user1Name |

  @C2903 @staging @rc @id2602
  Scenario Outline: Verify switching on/off chatheads [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given User <Contact2> change avatar picture to <Picture>
    Given User <Contact2> change name to <NewName>
    Given Myself is connected to <Contact>,<Contact2>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    When I tap on Sound Alerts
    And I see the Sound alerts page
    And I switch on or off the chathead preview
    And I close the Settings
    And I close self profile
    When I tap on contact name <Contact>
    And I see dialog page
    Given User <Contact2> sends 1 encrypted message to user Myself
    Then I do not see chathead of contact <Contact2>
    And I swipe right on Dialog page
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    When I tap on Sound Alerts
    And I see the Sound alerts page
    And I switch on or off the chathead preview
    And I close the Settings
    And I close self profile
    When I tap on contact name <Contact>
    And I see dialog page
    Given User <Contact2> sends 1 encrypted message to user Myself
    Then I see chathead of contact <Contact2>
    And I wait for 5 seconds
    Then I do not see chathead of contact <Contact2>

    Examples: 
      | Name      | Contact   | Contact2  | NewName  | Picture                      |
      | user1Name | user2Name | user3Name | CHATHEAD | aqaPictureContact600_800.jpg |

  @C2910 @regression @id3084
  Scenario Outline: Verify switching on/off chatheads [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given User <Contact2> change avatar picture to <Picture>
    Given User <Contact2> change name to <NewName>
    Given Myself is connected to <Contact>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    When I tap on Sound Alerts
    And I see the Sound alerts page
    And I switch on or off the chathead preview
    And I close the Settings
    And I close self profile
    When I tap on contact name <Contact>
    And I see dialog page
    Given User <Contact2> sends 1 encrypted message to user Myself
    Then I do not see chathead of contact <Contact2>
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    When I tap on Sound Alerts
    And I see the Sound alerts page
    And I switch on or off the chathead preview
    And I close the Settings
    And I close self profile
    When I tap on contact name <Contact>
    And I see dialog page
    Given User <Contact2> sends 1 encrypted message to user Myself
    Then I do not see chathead of contact <Contact2>

    Examples: 
      | Name      | Contact   | Contact2  | NewName  | Picture                      |
      | user1Name | user2Name | user3Name | CHATHEAD | aqaPictureContact600_800.jpg |

  @C2891 @regression @id2589
  Scenario Outline: Verify about screen contains all the required information [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given User me change accent color to <Color>
    Given I Sign in on tablet using my email
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
      | Name      | Contact   | Color  |
      | user1Name | user2Name | Violet |

  @C2911 @regression @id3156
  Scenario Outline: Verify about screen contains all the required information [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given User me change accent color to <Color>
    Given I Sign in on tablet using my email
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
      | Name      | Contact   | Color  |
      | user1Name | user2Name | Violet |
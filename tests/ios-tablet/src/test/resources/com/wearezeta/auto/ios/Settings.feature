Feature: Settings

  @staging @id2587
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

  @staging @id3021
  Scenario Outline: Verify user can access settings [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    Then I see settings page

    Examples: 
      | Name      |
      | user1Name |

  @staging @id2588
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

  @staging @id3022
  Scenario Outline: Attempt to open About screen in settings [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I click on Settings button on personal page
    And I click on About button on personal page
    Then I see About page
    And I close About page
    Then I see self profile page

    Examples: 
      | Name      |
      | user1Name |

  @staging @id2591
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

  @staging @id3023
  Scenario Outline: Verify reset password page is accessible from settings [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    And I click on Change Password button in Settings
    Then I see reset password page

    Examples: 
      | Name      |
      | user1Name |

  @staging @id2596
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

  @staging @id3024
  Scenario Outline: Verify default value for sound settings is all [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    When I tap on Sound Alerts
    And I see the Sound alerts page
    Then I verify that all is the default selected value

    Examples: 
      | Name      |
      | user1Name |

  @staging @id3019
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

  @staging @id3025
  Scenario Outline: Verify you can access Help site within the app [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I click on Settings button on personal page
    When I click on Help button from the options menu
    Then I see Support web page

    Examples: 
      | Name      |
      | user1Name |

  @staging @id2602
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
    And I swipe right on the personal page
    When I tap on contact name <Contact>
    And I see dialog page
    And Contact <Contact2> sends random message to user <Name>
    Then I do not see chathead of contact <Contact2>
    And I swipe right on Dialog page
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    When I tap on Sound Alerts
    And I see the Sound alerts page
    And I switch on or off the chathead preview
    And I close the Settings
    And I swipe right on the personal page
    When I tap on contact name <Contact>
    And I see dialog page
    And Contact <Contact2> sends random message to user <Name>
    Then I see chathead of contact <Contact2>
    And I wait for 5 seconds
    Then I do not see chathead of contact <Contact2>

    Examples: 
      | Name      | Contact   | Contact2  | NewName  | Picture                      |
      | user1Name | user2Name | user3Name | CHATHEAD | aqaPictureContact600_800.jpg |

  @staging @id3084
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
    When I tap on contact name <Contact>
    And I see dialog page
    And Contact <Contact2> sends random message to user <Name>
    Then I do not see chathead of contact <Contact2>
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    When I tap on Sound Alerts
    And I see the Sound alerts page
    And I switch on or off the chathead preview
    And I close the Settings
    When I tap on contact name <Contact>
    And I see dialog page
    And Contact <Contact2> sends random message to user <Name>
    Then I see chathead of contact <Contact2>
    And I wait for 5 seconds
    Then I do not see chathead of contact <Contact2>

    Examples: 
      | Name      | Contact   | Contact2  | NewName  | Picture                      |
      | user1Name | user2Name | user3Name | CHATHEAD | aqaPictureContact600_800.jpg |

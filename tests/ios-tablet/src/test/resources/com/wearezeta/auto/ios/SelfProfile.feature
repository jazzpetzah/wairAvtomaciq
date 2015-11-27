Feature: Self Profile

  @regression @id2586
  Scenario Outline: Self profile. Verify max limit in 64 chars [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I change name <Name> to <NewUsername>
    Then I see my new name <NewUsername1>
    When I close self profile
    Then I see Contact list with my name <NewUsername1>
    And I see my name <NewUsername1> first letter as label of Self Button
    When I tap on my name <Name>
    And I tap to edit my name
    And I change name <Name> to <NewUsername>
    Then I see my new name <NewUsername1>

    Examples: 
      | Name      | NewUsername                                                          | NewUsername1                                                     | Contact   |
      | user1Name | mynewusernamewithmorethan64characters3424245345345354353452345234535 | mynewusernamewithmorethan64characters342424534534535435345234523 | user2Name |

  @regression @id3157
  Scenario Outline: Self profile. Verify max limit in 64 chars [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I change name <Name> to <NewUsername>
    Then I see my new name <NewUsername1>
    When I close self profile
    Then I see Contact list with my name <NewUsername1>
    And I see my name <NewUsername1> first letter as label of Self Button
    When I tap on my name <Name>
    And I tap to edit my name
    And I change name <Name> to <NewUsername>
    Then I see my new name <NewUsername1>

    Examples: 
      | Name      | NewUsername                                                          | NewUsername1                                                     | Contact   |
      | user1Name | mynewusernamewithmorethan64characters3424245345345354353452345234535 | mynewusernamewithmorethan64characters342424534534535435345234523 | user2Name |

  @regression @id2581
  Scenario Outline: I verify I am unable to enter a name using only spaces or more than 80 chars [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I attempt to change name using only spaces
    And I see error message asking for more characters
    And I attempt to enter an 80 char name
    Then I verify my new name is only first 64 chars

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id3158
  Scenario Outline: I verify I am unable to enter a name using only spaces or more than 80 chars [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I attempt to change name using only spaces
    And I see error message asking for more characters
    And I attempt to enter an 80 char name
    Then I verify my new name is only first 64 chars

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @rc @id2574
  Scenario Outline: Change your profile picture [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap on personal screen
    And I press Camera button
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    And I return to personal page
    Then I see changed user picture <Picture>

    Examples: 
      | Name      | Picture                   | Contact   |
      | user1Name | userpicture_ios_check.png | user2Name |

  @regression @id3159
  Scenario Outline: Change your profile picture [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap on personal screen
    And I press Camera button
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    And I return to personal page
    Then I see changed user picture <Picture>

    Examples: 
      | Name      | Picture                             | Contact   |
      | user1Name | userpicture_ios_check_landscape.png | user2Name |

  @regression @rc @id2582
  Scenario Outline: Attempt to enter a name with 0 chars [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I attempt to input an empty name and press return
    And I see error message asking for more characters
    And I attempt to input an empty name and tap the screen
    And I see error message asking for more characters

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id3160
  Scenario Outline: Attempt to enter a name with 0 chars [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I attempt to input an empty name and press return
    And I see error message asking for more characters
    And I attempt to input an empty name and tap the screen
    And I see error message asking for more characters

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2583
  Scenario Outline: Verify 2 chars limit [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I attempt to enter <username1char> and press return
    Then I see error message asking for more characters
    And I attempt to enter <username1char> and tap the screen
    And I see error message asking for more characters
    And I attempt to enter <username2chars> and press return
    Then I see my new name <username2chars>

    Examples: 
      | Name      | username1char | username2chars | Contact   |
      | user1Name | c             | AB             | user2Name |

  @regression @id3161
  Scenario Outline: Verify 2 chars limit [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I attempt to enter <username1char> and press return
    Then I see error message asking for more characters
    And I attempt to enter <username1char> and tap the screen
    And I see error message asking for more characters
    And I attempt to enter <username2chars> and press return
    Then I see my new name <username2chars>

    Examples: 
      | Name      | username1char | username2chars | Contact   |
      | user1Name | c             | AB             | user2Name |

  @regression @id3162
  Scenario Outline: Verify name change [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Name> sent message TestMessage to conversation <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I change name <Name> to <NewUsername>
    And I close self profile
    And I see Contact list with my name <NewUsername>
    And I tap on contact name <Contact>
    Then I see my user name <NewUsername> in conversation

    Examples: 
      | Name      | NewUsername | Contact   |
      | user1Name | NewName     | user2Name |

  @regression @id3163
  Scenario Outline: Verify name change [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Name> sent message TestMessage to conversation <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I change name <Name> to <NewUsername>
    And I close self profile
    And I see Contact list with my name <NewUsername>
    And I tap on contact name <Contact>
    Then I see my user name <NewUsername> in conversation

    Examples: 
      | Name      | NewUsername | Contact   |
      | user1Name | NewName     | user2Name |

  @regression @rc @id2571
  Scenario Outline: Verify changing and applying accent color [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sent long message to conversation <Name>
    Given User <Name> change accent color to <Color1>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I slide my accent color via the colorpicker from <Color1> to <Color2>
    And I close self profile
    Then I see 5 unread message indicator in list for contact <Contact>

    Examples: 
      | Name      | NewName           | Color1 | Color2          | Contact   |
      | user1Name | AccentColorChange | Violet | StrongLimeGreen | user2Name |

  @regression @id3191
  Scenario Outline: Verify changing and applying accent color [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sent long message to conversation <Name>
    Given User <Name> change accent color to <Color1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I slide my accent color via the colorpicker from <Color1> to <Color2>
    And I close self profile
    Then I see 5 unread message indicator in list for contact <Contact>

    Examples: 
      | Name      | NewName           | Color1 | Color2          | Contact   |
      | user1Name | AccentColorChange | Violet | StrongLimeGreen | user2Name |

  @regression @id3850
  Scenario Outline: Verify adding phone number to the contact signed up with email [PORTRAIT]
    Given There is 1 users where <Name> is me with email only
    Given I Sign in on tablet using my email
    And I click Not Now to not add phone number
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to add my phone number
    And I see country picker button on Sign in screen
    And I enter phone number and verification code
    Then I see phone number attached to profile

    Examples: 
      | Name      |
      | user1Name |

  @regression @id3848
  Scenario Outline: Verify adding phone number to the contact signed up with email [LANDSCAPE]
    Given There is 1 users where <Name> is me with email only
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I click Not Now to not add phone number
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to add my phone number
    And I see country picker button on Sign in screen
    And I enter phone number and verification code
    Then I see phone number attached to profile

    Examples: 
      | Name      |
      | user1Name |

  @regression @noAcceptAlert @id3855
  Scenario Outline: Verify error message appears in case of entering a not valid phone number [PORTRAIT]
    Given There is 1 users where <Name> is me with email only
    Given I Sign in on tablet using my email
    And I accept alert
    When I click Not Now to not add phone number
    And I accept alert
    And I see Contact list with my name <Name>
    And I tap on my name <Name>
    And I tap to add my phone number
    And I see country picker button on Sign in screen
    And I enter invalid phone number
    Then I see invalid phone number alert

    Examples: 
      | Name      |
      | user1Name |

  @regression @noAcceptAlert @id3856
  Scenario Outline: Verify error message appears in case of entering a not valid phone number [LANDSCAPE]
    Given There is 1 users where <Name> is me with email only
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I accept alert
    When I click Not Now to not add phone number
    And I accept alert
    And I see Contact list with my name <Name>
    And I tap on my name <Name>
    And I tap to add my phone number
    And I see country picker button on Sign in screen
    And I enter invalid phone number
    Then I see invalid phone number alert

    Examples: 
      | Name      |
      | user1Name |

  @regression @noAcceptAlert @id3861
  Scenario Outline: Verify error message appears in case of registering already taken phone number [PORTRAIT]
    Given There is 1 users where <Name> is me with email only
    Given I Sign in on tablet using my email
    And I accept alert
    When I click Not Now to not add phone number
    And I accept alert
    And I see Contact list with my name <Name>
    And I tap on my name <Name>
    And I tap to add my phone number
    And I see country picker button on Sign in screen
    And I input phone number <Number> with code <Code>
    Then I see already registered phone number alert

    Examples: 
      | Name      | Number        | Code |
      | user1Name | 8301652248706 | +0   |
      
  @regression @noAcceptAlert @id3862
  Scenario Outline: Verify error message appears in case of registering already taken phone number [LANDSCAPE]
    Given There is 1 users where <Name> is me with email only
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I accept alert
    When I click Not Now to not add phone number
    And I accept alert
    And I see Contact list with my name <Name>
    And I tap on my name <Name>
    And I tap to add my phone number
    And I see country picker button on Sign in screen
    And I input phone number <Number> with code <Code>
    Then I see already registered phone number alert

    Examples: 
      | Name      | Number        | Code |
      | user1Name | 8301652248706 | +0   |

  @regression @rc @id3986
  Scenario Outline: Verify theme switcher is not shown on the self profile [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    Then I dont see theme switcher button on self profile page

    Examples: 
      | Name      |
      | user1Name |

  @regression @id3989
  Scenario Outline: Verify theme switcher is not shown on the self profile [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    Then I dont see theme switcher button on self profile page

    Examples: 
      | Name      |
      | user1Name |

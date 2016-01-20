Feature: Self Profile

  #smoke
  @C3211 @regression @id344
  Scenario Outline: Change your profile picture
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on my name <Name>
    And I tap on personal screen
    And I press Camera button
    And I choose a picture from camera roll
    And I press Confirm button
    And I wait for 5 seconds
    And I return to personal page
    Then I see changed user picture <Picture>

    Examples: 
      | Name      | Picture                       | Contact   |
      | user1Name | userpicture_ios_check_new.png | user2Name |

  @C1092 @regression @id1055
  Scenario Outline: Attempt to enter a name with 0 chars
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on my name <Name>
    And I tap to edit my name
    And I attempt to input an empty name and press return
    And I see error message asking for more characters
    And I attempt to input an empty name and tap the screen
    And I see error message asking for more characters

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C1093 @regression @id1056
  Scenario Outline: Attempt to enter a name with 1 char
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on my name <Name>
    And I tap to edit my name
    And I attempt to enter <username> and press return
    And I see error message asking for more characters
    And I attempt to enter <username> and tap the screen
    And I see error message asking for more characters

    Examples: 
      | Name      | username | Contact   |
      | user1Name | c        | user2Name |

  @C1097 @regression @rc @id1463
  Scenario Outline: Verify name change
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on my name <Name>
    And I tap to edit my name
    And I attempt to input an empty name and press return
    And I see error message asking for more characters
    And I change name <Name> to <NewUsername>
    And I close self profile
    And I see conversations list
    And I tap on my name <NewUsername>
    Then I see my new name <NewUsername>

    Examples: 
      | Name      | NewUsername | Contact   |
      | user1Name | New Name    | user2Name |

  @C1083 @regression @id667
  Scenario Outline: Verify changing and applying accent color
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sends 40 encrypted messages to user Myself
    Given User <Name> change accent color to <Color1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on my name <Name>
    And I slide my accent color via the colorpicker from <Color1> to <Color2>
    And I close self profile
    And User <Contact> sends encrypted message "<Message>" to user <Name>
    Then I see 1 unread message indicator in list for contact <Contact>

    Examples: 
      | Name      | Color1 | Color2          | Contact   | Message |
      | user1Name | Violet | StrongLimeGreen | user2Name | hidiho  |

  @C1085 @regression @id3849
  Scenario Outline: Verify adding phone number to the contact signed up with email
    Given There is 1 users where <Name> is me with email only
    Given I sign in using my email
    Given I click Not Now to not add phone number
    Given I see conversations list
    When I tap on my name <Name>
    And I tap to add my phone number
    And I see country picker button on Sign in screen
    And I enter phone number and verification code
    Then I see phone number attached to profile

    Examples: 
      | Name      |
      | user1Name |

  @C1087 @regression @noAcceptAlert @id3854
  Scenario Outline: Verify error message appears in case of entering a not valid phone number
    Given There is 1 users where <Name> is me with email only
    Given I sign in using my email
    And I accept alert
    When I click Not Now to not add phone number
    And I accept alert
    And I see conversations list
    And I tap on my name <Name>
    And I tap to add my phone number
    And I see country picker button on Sign in screen
    And I enter invalid phone number
    Then I see invalid phone number alert

    Examples: 
      | Name      |
      | user1Name |      

  @C1088 @regression @noAcceptAlert @id3860
  Scenario Outline: Verify error message appears in case of registering already taken phone number
    Given There is 1 users where <Name> is me with email only
    Given I sign in using my email
    And I accept alert
    When I click Not Now to not add phone number
    And I accept alert
    And I see conversations list
    And I tap on my name <Name>
    And I tap to add my phone number
    And I see country picker button on Sign in screen
    And I input phone number <Number> with code <Code>
    Then I see already registered phone number alert

    Examples: 
      | Name      | Number        | Code |
      | user1Name | 8301652248706 | +0   |

  @C1081 @regression @rc @id3990
  Scenario Outline: Verify theme switcher is shown on the self profile
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on my name <Name>
    Then I see theme switcher button on self profile page

    Examples: 
      | Name      | 
      | user1Name |
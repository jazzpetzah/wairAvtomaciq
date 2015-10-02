Feature: Self Profile

  #smoke
  @staging @id344
  Scenario Outline: Change your profile picture
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap on personal screen
    And I press Camera button
    And I choose a picture from camera roll
    And I press Confirm button
    And I return to personal page
    Then I see changed user picture <Picture>

    Examples: 
      | Name      | Picture                   | Contact   |
      | user1Name | userpicture_ios_check.png | user2Name |

  @regression @id1055
  Scenario Outline: Attempt to enter a name with 0 chars
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
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

  @regression @id1056
  Scenario Outline: Attempt to enter a name with 1 char
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I attempt to enter <username> and press return
    And I see error message asking for more characters
    And I attempt to enter <username> and tap the screen
    And I see error message asking for more characters

    Examples: 
      | Name      | username | Contact   |
      | user1Name | c        | user2Name |

  @regression @rc @id1463
  Scenario Outline: Verify name change
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I attempt to input an empty name and press return
    And I see error message asking for more characters
    And I change name <Name> to <NewUsername>
    And I close self profile
    And I see Contact list with my name <NewUsername>
    And I tap on my name <NewUsername>
    Then I see my new name <NewUsername>

    Examples: 
      | Name      | NewUsername | Contact   |
      | user1Name | New Name    | user2Name |

  @regression @id667
  Scenario Outline: Verify changing and applying accent color
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sent long message to conversation <Name>
    Given User <Name> change accent color to <Color1>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I slide my accent color via the colorpicker from <Color1> to <Color2>
    And I close self profile
    Then I see 5 unread message indicator in list for contact <Contact>

    Examples: 
      | Name      | Color1 | Color2          | Contact   |
      | user1Name | Violet | StrongLimeGreen | user2Name |

  @staging @id3849 
  Scenario Outline: Verify adding phone number to the contact signed up with email
    Given There is 1 users where <Name> is me with email only
    Given I sign in using my email
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

  @staging @noAcceptAlert @id3854
  Scenario Outline: Verify error message appears in case of entering a not valid phone number
    Given There is 1 users where <Name> is me with email only
    Given I sign in using my email
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

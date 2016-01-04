Feature: Utility

  @id
  Scenario: Verify buttons on invitation page for iphone
    When I use generic invitation link for invitation for iphone
    #And I wait for 10 seconds
    And I see You are invited page with agent
    Then I see button that sends me to App Store
    And I see button to connect for iphone including invitation code

  @utility
  Scenario: Verify buttons on invitation page for android
    When I use generic invitation link for invitation for android
    And I see You are invited page
    Then I see button that sends me to Play Store
    And I see button to connect for android including invitation code    
    
  @id
  Scenario Outline: test Verify buttons on invitation page for iphone
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see Contacts Upload dialog
    And I click Show Search button on Contacts Upload dialog
    And I see Bring Your Friends button on People Picker page
    When I click Bring Your Friends button on People Picker page
    Then I see Bring Your Friends popover
    When I click Invite button on Bring Your Friends popover
    And I remember invitation link on Bring Your Friends popover and add an agent
    Then I do not see Bring Your Friends popover
    When I navigate to previously remembered invitation link
    Then I see You are invited page with agent

    # We don't go further since invitation flow will be changed soon
    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @id1
  Scenario Outline: Verify buttons on download page
    When I navigate to download page
    And I wait 10 seconds
    Then I see button for iOS
    And I see button for Android
    And I see button for OS X
    #And I see button for Windows
    And I see button for Web
    
Feature: Share Location

  @C165160 @C165167 @rc @regression @fastLogin
  Scenario Outline: Receive and share location
    Given There are 3 user where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>, <Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact1> shares the default location to user Myself via device <DeviceName>
    Given I see conversations list
    When I tap on contact name <Contact1>
    Then I see location map container in the conversation view
    When I tap on group chat with name <GroupChatName>
    And I tap Share Location button from input tools
    And I accept alert if visible
    # Small delay waiting location detection animation to finish(animation for iPad takes longer)
    And I wait for 5 seconds
    And I tap Send location button from map view
    Then I see location map container in the conversation view
    When I tap on location map in conversation view
    # Wait for map application to be opened
    And I wait for 15 seconds
    And I accept alert if visible
    Then I see map application is opened

    Examples:
      | Name      | Contact1  | DeviceName | Contact2  | GroupChatName |
      | user1Name | user2Name | device1    | user3Name | ShareAddress  |
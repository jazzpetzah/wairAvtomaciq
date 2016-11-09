Feature: Share Location

  @C150025 @rc @regression @fastLogin
  Scenario Outline: Verify receiving shared location and opening map in the default Apple app
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> shares the default location to user Myself via device <DeviceName>
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see location map container in the conversation view
    When I tap on location map in conversation view
    # Wait until the map is loaded
    And I wait for 15 seconds
    Then I see map application is opened

    Examples:
      | Name      | Contact   | DeviceName |
      | user1Name | user2Name | device1    |

  @C150027 @regression @fastLogin
  Scenario Outline: Verify deleting shared location
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> shares the default location to user Myself via device <DeviceName>
    Given I see conversations list
    When I tap on contact name <Contact>
    # wait for sync
    And I wait for 5 seconds
    Then I see location map container in the conversation view
    And I long tap on location map in conversation view
    And I tap on Delete badge item
    And I select Delete for Me item from Delete menu
    Then I do not see location map container in the conversation view

    Examples:
      | Name      | Contact   | DeviceName |
      | user1Name | user2Name | device1    |

  @C165104 @rc @regression @fastLogin
  Scenario Outline: Verify sending location from a map view in and opening the map on clicking on map icon (1to1)
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <DeviceName1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Share Location button from input tools
    And I accept alert if visible
    # Small delay waiting location detection animation to finish
    And I wait for 5 seconds
    And I tap Send location button from map view
    Then I see location map container in the conversation view
    #TODO Stabilize sent address verification step
    #And I see the default sent Share Location address in the conversation view
    And I see "<DeliveredLabel>" on the message toolbox in conversation view

    Examples:
      | Name      | Contact   | DeviceName1 | DeliveredLabel |
      | user1Name | user2Name | device1     | Delivered      |

  @C165126 @regression @fastLogin
  Scenario Outline: Verify sending location from a map view (group conversation)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>, <Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I tap Share Location button from input tools
    And I accept alert if visible
    # Small delay waiting location detection animation to finish
    And I wait for 5 seconds
    And I tap Send location button from map view
    Then I see location map container in the conversation view
    #TODO Stabilize sent address verification step
    #And I see the default sent Share Location address in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ShareInGroup  |

  @C150032 @regression @fastLogin
  Scenario Outline: Verify copying and pasting the shared location
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> shares the default location to user Myself via device <DeviceName>
    Given I see conversations list
    When I tap on contact name <Contact>
    And I long tap on location map in conversation view
    And I tap on Copy badge item
    And I tap on text input
    And I long tap on text input
    And I tap on Paste badge item
    And I tap Send Message button in conversation view
    Then I see last message in the conversation view contains expected message <ExpectedText>

    Examples:
      | Name      | Contact   | DeviceName | ExpectedText |
      | user1Name | user2Name | device1    | Wirestan     |

  @C165116 @regression @fastLogin @forceReset
  Scenario Outline: Verify permissions are asked first time on the map opening
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Share Location button from input tools
    # Wait until map app is shown
    And I wait for 5 seconds
    Then I verify the alert contains text <ExpectedAlertText>
    When I dismiss alert
    And I tap Send location button from map view
    Then I see location map container in the conversation view

    Examples:
      | Name      | Contact   | ExpectedAlertText    |
      | user1Name | user2Name | access your location |
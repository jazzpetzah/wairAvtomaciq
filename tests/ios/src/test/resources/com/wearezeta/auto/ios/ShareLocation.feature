Feature: Share Location

  @C150025 @C150026 @staging
  Scenario Outline: Map is shown in the conversation view
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> shares the default location to user Myself via device <DeviceName>
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see Share Location container in the conversation view
    And I see the default Share Location address in the conversation view
    When I tap on location map in conversation view
    Then I see map application is opened

    Examples:
      | Name      | Contact   | DeviceName |
      | user1Name | user2Name | device1    |

  @C150027 @regression
  Scenario Outline: Verify deleting shared location
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> shares the default location to user Myself via device <DeviceName>
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see Share Location container in the conversation view
    And I see the default Share Location address in the conversation view
    And I long tap on location map in conversation view
    And I tap on Delete badge item
    # Sometimes the alert is not accepted automatically
    And I tap Delete button on the alert
    Then I do not see Share Location container in the conversation view
    Then I do not see the default Share Location address in the conversation view

    Examples:
      | Name      | Contact   | DeviceName |
      | user1Name | user2Name | device1    |

  @C165104 @C165105 @staging
  Scenario Outline: Verify sending location from a map view (1to1)
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Share Location button from input tools
    # Small delay waiting location detection animation to finish
    And I wait for 5 seconds
    And I tap Send location button from map view
    Then I see Share Location container in the conversation view
    And I see the default Share Location address in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C165126 @staging
  Scenario Outline: Verify sending location from a map view (group conversation)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>, <Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I tap Share Location button from input tools
    # Small delay waiting location detection animation to finish
    And I wait for 5 seconds
    And I tap Send location button from map view
    Then I see Share Location container in the conversation view
    And I see the default Share Location address in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ShareInGroup  |

  @C150032 @staging
  Scenario Outline: Verify copying and pasting the shared location
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> shares the default location to user Myself via device <DeviceName>
    Given I see conversations list
    When I tap on contact name <Contact>
    And I long tap on location map in conversation view
    And I tap on Copy badge item
    And I paste and commit the text
    Then I verify that pasted message contains <ExpectedText>

    Examples:
      | Name      | Contact   | DeviceName | ExpectedText |
      | user1Name | user2Name | device1    | Wirestan     |

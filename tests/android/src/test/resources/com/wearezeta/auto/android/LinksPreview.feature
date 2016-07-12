Feature: Links Preview

  @C165143 @staging @C165147
  Scenario Outline: I see preview for sent link and no preview for dead link
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I type the message "<DeadLink>" and send it
    # C165147
    Then I do not see Link Preview container in the conversation view
    When I type the message "<Link>" and send it
    # C165143
    Then I see Link Preview container in the conversation view

    Examples:
      | Name      | Contact   | Link                                                                                               | DeadLink      |
      | user1Name | user2Name | http://www.lequipe.fr/Football/Actualites/L-olympique-lyonnais-meilleur-centre-de-formation/703676 | http://q.qqqq |

  @C165144 @staging @C165148
  Scenario Outline: I can receive preview for link mixed with text
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> send encrypted message "<Message>" to user Myself
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    Then I see Link Preview container in the conversation view
    And I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message                                                                                                       |
      | user1Name | user2Name | My message http://www.lequipe.fr/Football/Actualites/L-olympique-lyonnais-meilleur-centre-de-formation/703676 |

  @C165151 @staging
  Scenario Outline: Link sent from offline should have correct preview when I back to online
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I enable Airplane mode on the device
    And I see No Internet bar in 15 seconds
    And I type the message "<Link>" and send it
    And I disable Airplane mode on the device
    And I do not see No Internet bar in 15 seconds
    Then I see Link Preview container in the conversation view
    And I do not see the message "<Link>" in the conversation view

    Examples:
      | Name      | Contact   | Link                                                                                               |
      | user1Name | user2Name | http://www.lequipe.fr/Football/Actualites/L-olympique-lyonnais-meilleur-centre-de-formation/703676 |

  @C165152 @staging
  Scenario Outline: Show different link for link preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I type the message "<TextLink>" and send it
    Then I see Link Preview container in the conversation view
    And I see the message "<TextLink>" in the conversation view
    When I long tap Link Preview container in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    And I type the message "<LinkText>" and send it
    Then I see Link Preview container in the conversation view
    And I see the message "<LinkText>" in the conversation view
    When I long tap Link Preview container in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    And I type the message "<TextLinkText>" and send it
    Then I see Link Preview container in the conversation view
    And I see the message "<TextLinkText>" in the conversation view
    When I long tap Link Preview container in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    And I type the message "<url4>" and send it
    Then I see Link Preview container in the conversation view
    And I do not see the message "<url4>" in the conversation view
    When I long tap Link Preview container in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    And I type the message "<url5>" and send it
    Then I see Link Preview container in the conversation view
    And I do not see the message "<url5>" in the conversation view

    Examples:
      | Name      | Contact   | TextLink                 | LinkText                  | TextLinkText                      | url4                | url5                |
      | user1Name | user2Name | text http://facebook.com | https://facebook.com text | text http://www.facebook.com text | Http://facebook.com | HTTP://FACEBOOK.COM |


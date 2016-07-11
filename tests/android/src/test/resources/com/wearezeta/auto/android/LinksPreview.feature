Feature: Links Preview

  @C165143 @staging
  Scenario Outline: I see preview for sent link
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I type the message "<Link>" and send it
    Then I see Link Preview container in the conversation view

    Examples:
      | Name      | Contact   | Link                                                                                               |
      | user1Name | user2Name | http://www.lequipe.fr/Football/Actualites/L-olympique-lyonnais-meilleur-centre-de-formation/703676 |

  @C165144 @staging
  Scenario Outline: I see preview for received link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> send encrypted message "<Link>" to user Myself
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    Then I see Link Preview container in the conversation view

    Examples:
      | Name      | Contact   | Link                                                                                               |
      | user1Name | user2Name | http://www.lequipe.fr/Football/Actualites/L-olympique-lyonnais-meilleur-centre-de-formation/703676 |

  @C165151 @staging @torun
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



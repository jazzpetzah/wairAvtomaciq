Feature: Links Preview

  @C165143 @regression @rc
  Scenario Outline: I see/do not see link preview in different condition(normal url/dead url/shorten url)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I type the message "<DeadLink>" and send it by cursor Send button
    # C165147
    Then I do not see Link Preview container in the conversation view
    And I see the message "<DeadLink>" in the conversation view
    When I type the message "<ShortenUrl>" and send it by cursor Send button
    # C169222
    Then I see Link Preview container in the conversation view
    And I do not see the message "<ShortenUrl>" in the conversation view
    When I type the message "<Link>" and send it by cursor Send button
    # C165143
    Then I see Link Preview container in the conversation view
    And I do not see the message "<Link>" in the conversation view

    Examples:
      | Name      | Contact   | Link                                                                                               | DeadLink      | ShortenUrl           |
      | user1Name | user2Name | http://www.lequipe.fr/Football/Actualites/L-olympique-lyonnais-meilleur-centre-de-formation/703676 | http://q.qqqq | http://goo.gl/bnKrzm |

  @C165144 @regression @rc
  Scenario Outline: I see preview for received link which mixed with text
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> send encrypted message "<Message>" to user Myself
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    Then I see Link Preview container in the conversation view
    And I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message                         |
      | user1Name | user2Name | My message https://www.wire.com |

  @C165151 @regression
  Scenario Outline: Link sent from offline should have correct preview when I back to online
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I enable Airplane mode on the device
    And I see No Internet bar in 15 seconds
    And I type the message "<Link>" and send it by cursor Send button
    And I disable Airplane mode on the device
    And I do not see No Internet bar in 15 seconds
    And I resend all the visible messages in conversation view
    Then I see Link Preview container in the conversation view
    And I do not see the message "<Link>" in the conversation view

    Examples:
      | Name      | Contact   | Link                 |
      | user1Name | user2Name | https://www.wire.com |

  @C165152 @regression
  Scenario Outline: Show different link for link preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I type the message "<TextLink>" and send it by cursor Send button
    Then I see Link Preview container in the conversation view
    And I see the message "<TextLink>" in the conversation view
    When I long tap Link Preview container in the conversation view
    And I tap Delete button on the message bottom menu
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    And I type the message "<LinkText>" and send it by cursor Send button
    Then I see Link Preview container in the conversation view
    And I see the message "<LinkText>" in the conversation view
    When I long tap Link Preview container in the conversation view
    And I tap Delete button on the message bottom menu
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    And I type the message "<TextLinkText>" and send it by cursor Send button
    Then I see Link Preview container in the conversation view
    And I see the message "<TextLinkText>" in the conversation view
    When I long tap Link Preview container in the conversation view
    And I tap Delete button on the message bottom menu
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    And I type the message "<url4>" and send it by cursor Send button
    Then I see Link Preview container in the conversation view
    And I do not see the message "<url4>" in the conversation view
    When I long tap Link Preview container in the conversation view
    And I tap Delete button on the message bottom menu
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    And I type the message "<url5>" and send it by cursor Send button
    Then I see Link Preview container in the conversation view
    And I do not see the message "<url5>" in the conversation view

    Examples:
      | Name      | Contact   | TextLink                 | LinkText                  | TextLinkText                      | url4                | url5                |
      | user1Name | user2Name | text http://facebook.com | https://facebook.com text | text http://www.facebook.com text | Http://facebook.com | HTTP://FACEBOOK.COM |

  @C202305 @regression
  Scenario Outline: Show correct Url in link preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I type the message "<Link1>" and send it by cursor Send button
    Then I see Link Preview URL <LinkUrl>
    When I long tap Link Preview container in the conversation view
    And I tap Delete button on the message bottom menu
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    And I type the message "<Link2>" and send it by cursor Send button
    Then I see Link Preview URL <LinkUrl>
    When I long tap Link Preview container in the conversation view
    And I tap Delete button on the message bottom menu
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    And I type the message "<Link3>" and send it by cursor Send button
    Then I see Link Preview URL <LinkUrl>
    When I long tap Link Preview container in the conversation view
    And I tap Delete button on the message bottom menu
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    And I type the message "<Link4>" and send it by cursor Send button
    Then I see Link Preview URL <Link2Url>

    Examples:
      | Name      | Contact   | Link1               | Link2                    | Link3                    | LinkUrl      | Link4                       | Link2Url        |
      | user1Name | user2Name | http://facebook.com | https://www.facebook.com | http://www.facebook.com/ | facebook.com | HTTP://WWW.FRANCE24.COM/FR/ | france24.com/FR |

  @C169223 @regression
  Scenario Outline: Verify resend icon appears for unsent link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I enable Airplane mode on the device
    And I see No Internet bar in 10 seconds
    And I type the message "<LinkUrl>" and send it by cursor Send button
    Then I see Message status with expected text "<MessageStatus>" in conversation view
    And I disable Airplane mode on the device

    Examples:
      | Name      | Contact   | LinkUrl             | MessageStatus          |
      | user1Name | user2Name | http://facebook.com | Sending failed. Resend |

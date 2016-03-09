Feature: Upgrade

  @C1 @upgrade @torun
  Scenario Outline: Upgrade
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    Given User <Contact> sends 1 encrypted message to user Myself
    # To let the content to be synchronized
    Given I wait for 5 seconds
    Given I upgrade Wire to the recent version
    And I see conversations list
    When I tap on contact name <Contact>
    Then I see 1 photo in the dialog
    And I see 1 default message in the dialog
    When I type the default message and send it
    Then I see 2 default messages in the dialog

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

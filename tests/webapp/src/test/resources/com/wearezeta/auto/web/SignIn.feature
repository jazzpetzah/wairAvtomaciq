Feature: Sign In

  @regression @id1788
  Scenario Outline: Sign in to Wire for Web
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    Given I see Sign In page
    When I enter email <Email>
    And I enter password "<Password>"
    And I press Sign In button
    Then I am signed in properly
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @smoke @id1792
  Scenario Outline: Verify sign in error appearance in case of wrong credentials
    Given There is 1 user where user1Name is me
    Given I switch to sign in page
    When I enter email <Email>
    And I enter password "<Password>"
    And I press Sign In button
    Then the sign in error message reads <Error>
    And a red dot is shown inside the email field on the sign in form
    And a red dot is shown inside the password field on the sign in form

    Examples: 
      | Email      | Password      | Error                                      |
      | user1Email |               | WRONG EMAIL OR PASSWORD. PLEASE TRY AGAIN. |
      | user1Email | wrongPassword | WRONG EMAIL OR PASSWORD. PLEASE TRY AGAIN. |

  @staging @id2714
  Scenario Outline: Verify you can sign in with a phone number with correct credentials
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I switch to phone number sign in page
    When I sign in using phone number of user <Name>
    And I click on forward button on phone number sign in
    And I enter phone verification code for user <Name>
    Then I am signed in properly
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list

    Examples: 
      | Name      |
      | user1Name |


  @staging @id2715
  Scenario Outline: Verify you see correct error message when sign in with incorrect phone number
    Given I switch to sign in page
    When I switch to phone number sign in page
    And I enter country code <CountryCode> on phone number sign in
    And I enter phone number <PhoneNumber> on phone number sign in
    And I click on forward button on phone number sign in
    Then I see invalid phone number error message saying <Error>

    Examples: 
      | CountryCode | PhoneNumber | Error        |
      | +0          | 12341234    | INVALID PHONE NUMBER |
      | +49         | qwerqwer    | INVALID PHONE NUMBER |
      | +49         | !@$!@$      | INVALID PHONE NUMBER |

  @staging @id2716
  Scenario Outline: Verify you see correct error message when sign in with a phone number with incorrect code
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I switch to phone number sign in page
    When I sign in using phone number of user <Name>
    And I click on forward button on phone number sign in
    And I enter wrong phone verification code for user <Name>
    Then I see invalid phone code error message saying <Error>

    Examples: 
      | Name      | Error        |
      | user1Name | INVALID CODE |

  @staging @id2227
  Scenario Outline: Show invitation button when Gmail import on registration has no suggestions
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    Given I see Sign In page
    When I enter email <Email>
    And I enter password "<Password>"
    And I press Sign In button
    Then I am signed in properly
    And I see Contacts Upload dialog
    And I click button to import Gmail Contacts
    And I see Google login popup
    When I sign up at Google with email <Gmail> and password <GmailPassword>
    Then I see Search is opened
    And I see Send Invitation button on People Picker page

    Examples: 
      | Email      | Password      | Name      | Gmail                       | GmailPassword |
      | user1Email | user1Password | user1Name | smoketester.wire2@gmail.com | aqa123456     |

Feature: Registration

  @C1761 @smoke @useSpecialEmail @localytics
  Scenario Outline: Verify new user can be registered
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And Create Account button is disabled
    And I accept the Terms of Use
    And I start activation email monitoring
    And I submit registration form
    Then I verify that an envelope icon is shown
    And I see email <Email> on Verification page
    When I activate user by URL
    And User <Name> is Me without avatar
    And I see first time experience with watermark
    And I open preferences by clicking the gear button
    Then I see user name on self profile page <Name>
    Then I see user email on self profile page <Email>
    And I see localytics event <Event> with attributes <Attributes>
    And I open preferences by clicking the gear button
    And I select Log out menu item on self profile page

    Examples: 
      | Email      | Password      | Name      | Event                  | Attributes              |
      | user1Email | user1Password | user1Name | registration.succeeded | {\"content\":\"email\"} |

  @C1822 @regression @useSpecialEmail
  Scenario Outline: Verify I can accept personal invitation
    Given There is 1 user where <Name> is me
    When me sends personal invitation to mail <ContactMail> with message <Message>
    Then I verify user <Contact> has received an email invitation
    When <Contact> navigates to personal invitation registration page
    Then <Contact> verifies email is correct on Registration page
    And <Contact> verifies username is correct on Registration page
    And I verify text about Wire is visible
    And I see intro about Wire saying <TextWire>
    And I enter user password "<Password>" on Registration page
    And I accept the Terms of Use
    And I submit registration form
    And I see first time experience with watermark
    And I see Contact list with name <Name>

    Examples: 
      | Password      | Name      | ContactMail | Contact    | Message | TextWire                                                                                     |
      | user1Password | user1Name | user2Email  | user2Name  | Hello   | Simple, private & secure messenger for chat, calls, sharing pics, music, videos, GIFs and more. |

  @C1762 @regression
  Scenario Outline: I want to be notified if the email address I entered during registration has already been registered
    Given There is 1 user where user1Name is me without avatar picture
    When I enter user name <Name> on Registration page
    And I enter user email <UsedEmail> on Registration page
    And I enter user password "<NewPassword>" on Registration page
    And I accept the Terms of Use
    And I submit registration form
    Then I see error "Email address already taken" on Verification page
    And I verify that the email field on the registration form is marked as error
    When I enter user email <UnusedEmail> on Registration page
    Then I verify that the email field on the registration form is not marked as error
    When I submit registration form
    Then I verify that an envelope icon is shown
    And I see email <UnusedEmail> on Verification page

    Examples: 
      | Name      | UsedEmail  | UnusedEmail |
      | user1Name | user1Email | user2Email  |

  @C1763 @regression
  Scenario Outline: I want to see an error message if the email address is forbidden
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And I accept the Terms of Use
    And I submit registration form
    Then I verify that the email field on the registration form is marked as error
    And I see error "Sorry. This email address is forbidden." on Verification page

    Examples: 
      | Name      | Email              | Password      |
      | user1Name | nope@wearezeta.com | user1Password |

  @C3219 @mute
  Scenario Outline: I want to see an error message if Terms of Use are not accepted
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And I submit registration form
    And I see error "Please accept Wire Terms of Use." on Verification page

    Examples: 
      | Name      | Email      | Password      |
      | user1Name | user1Email | user1Password |

  @C1768 @regression
  Scenario Outline: Register using already registered but not verified yet email
    Given I enter user name <Name> on Registration page
    Given I enter user email <Email> on Registration page
    Given I enter user password "<Password>" on Registration page
    Given I accept the Terms of Use
    When I submit registration form
    Then I verify that an envelope icon is shown
    And I see email <Email> on Verification page
    And I open Sign In page
    When I see Sign In page
    When I Sign in using login <Email> and password <Password>
    Then I verify that an envelope icon is shown
    And I see email <Email> on pending page

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C1760 @regression
  Scenario Outline: I want to see an error message if email or password are not valid
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And I accept the Terms of Use
    And I submit registration form
    Then I verify that the email field on the registration form is marked as error
    And I see error "Please enter a valid email address." on Verification page

    Examples: 
      | Email        | Password      | Name      |
      | @example.com | user1Password | user1Name |
      | example@     | user1Password | user1Name |
      | @            | user1Password | user1Name |
      | @            | 12            | user1Name |